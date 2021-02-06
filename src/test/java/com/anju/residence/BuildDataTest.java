package com.anju.residence;

import cn.hutool.core.util.RandomUtil;
import com.anju.residence.dao.LoginInfoRepository;
import com.anju.residence.dao.RoleRepository;
import com.anju.residence.dao.UserRepository;
import com.anju.residence.dao.ele.AlertNoticeRepository;
import com.anju.residence.dao.ele.BehaviorLogRepository;
import com.anju.residence.dao.ele.DeviceRepository;
import com.anju.residence.dao.ele.ElectricLogRepository;
import com.anju.residence.dao.ele.JackRepository;
import com.anju.residence.dao.ele.ReceptacleRepository;
import com.anju.residence.dao.ele.SceneRepository;
import com.anju.residence.entity.Role;
import com.anju.residence.entity.User;
import com.anju.residence.entity.ele.Device;
import com.anju.residence.entity.ele.ElectricLog;
import com.anju.residence.entity.ele.Jack;
import com.anju.residence.entity.ele.Receptacle;
import com.anju.residence.entity.ele.Scene;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author cygao
 * @date 2021/1/26 11:08 上午
 **/
@Slf4j
@SpringBootTest
public class BuildDataTest {

  private final AlertNoticeRepository alertRepo;
  private final DeviceRepository deviceRepo;
  private final JackRepository jackRepo;
  private final LoginInfoRepository loginRepo;
  private final ReceptacleRepository receptacleRepo;
  private final RoleRepository roleRepo;
  private final SceneRepository sceneRepo;
  private final UserRepository userRepo;
  private final ElectricLogRepository electricLogRepo;
  private final BehaviorLogRepository behaviorLogRepo;

  private static final Random random = new Random();

  private final int RANDOM_USER_NUMBER = 400;
  private final int RANDOM_SCENE_NUMBER = 1000;
  private final int RANDOM_RECEPTACLE_NUMBER = 1600;
  private final int RANDOM_MAX_JACK_PER_RECEPTACLE = 8;
  private final int RANDOM_DEVICE_NUMBBER = 1300;
  private final int RANDOM_ELELOG_NUMBER = 10000;

  @Autowired
  public BuildDataTest(AlertNoticeRepository alertRepo, DeviceRepository deviceRepo, JackRepository jackRepo, LoginInfoRepository loginRepo, ReceptacleRepository receptacleRepo, RoleRepository roleRepo, SceneRepository sceneRepo, UserRepository userRepo, ElectricLogRepository electricLogRepo, BehaviorLogRepository behaviorLogRepo) {
    this.alertRepo = alertRepo;
    this.deviceRepo = deviceRepo;
    this.jackRepo = jackRepo;
    this.loginRepo = loginRepo;
    this.receptacleRepo = receptacleRepo;
    this.roleRepo = roleRepo;
    this.sceneRepo = sceneRepo;
    this.userRepo = userRepo;
    this.electricLogRepo = electricLogRepo;
    this.behaviorLogRepo = behaviorLogRepo;
  }

  /**
   * 构建测试用数据
   * 构建顺序：Role->User->Scene->Receptacle->jack->device->eleLog
   */
  @Transactional(rollbackFor = Exception.class)
  @Rollback(value = false)
  @Test
  public void buildData() {
    Role ordinary = Role.builder().roleName("ordinary").build();
    Role vip = Role.builder().roleName("vip").build();
    if (!roleRepo.existsByRoleName(ordinary.getRoleName())) roleRepo.save(ordinary);
    if (!roleRepo.existsByRoleName(vip.getRoleName())) roleRepo.save(vip);
    ordinary = roleRepo.findByRoleName(ordinary.getRoleName());
    vip = roleRepo.findByRoleName(vip.getRoleName());

    // 构建用户
    User startUser = User.builder().username("startUser").gender(0).password(RandomUtil.randomString(15)).email(RandomUtil.randomNumbers(8) + "@gmail.com").phone(RandomUtil.randomNumbers(12)).address(RandomUtil.randomString(20)).roles(Arrays.asList(ordinary, vip)).build();
    startUser = userRepo.save(startUser);
    int startUserId = startUser.getId();
    int endUserId = startUserId + RANDOM_USER_NUMBER;
    for (int i = 0; i < RANDOM_USER_NUMBER; i++) {
      User user = User.builder().username(RandomUtil.randomString(10)).gender(random.nextInt(1)).password(RandomUtil.randomString(15)).email(RandomUtil.randomString(12) + "@gmail.com").phone(RandomUtil.randomNumbers(12)).address(RandomUtil.randomString(20)).roles(Collections.singletonList(ordinary)).build();
      user = userRepo.save(user);
      log.info("createElectricLog User:{} successfully", user.getId());
    }

    // 构建场景
    Scene startScene = Scene.builder().name("StartScene").description(RandomUtil.randomString(12)).user(startUser).build();
    startScene = sceneRepo.save(startScene);
    int startSceneId = startScene.getId();
    int endSceneId = startSceneId + RANDOM_SCENE_NUMBER;
    for (int i = 0; i < RANDOM_SCENE_NUMBER; i++) {
      Scene scene = Scene.builder().name(RandomUtil.randomString(8)).description(RandomUtil.randomString(12)).user(userRepo.findById(RandomUtil.randomInt(startUserId, endUserId)).orElse(null)).build();
      sceneRepo.save(scene);
      log.info("createElectricLog Scene:{} successfully", scene.getId());
    }

    // 构建插座和插孔
    Receptacle startReceptacle = Receptacle.builder().name("StartReceptacle").status(0).scene(startScene).user(startUser).build();
    startReceptacle = receptacleRepo.save(startReceptacle);
    Jack startJack = Jack.builder().name("StartJack").receptacle(startReceptacle).status(0).type(0).build();
    startJack = jackRepo.save(startJack);
    int startJackId = startJack.getId();
    int endJackId = startJackId;
    int startReceptacleId = startReceptacle.getId();
    int endReceptacleId = startReceptacleId + RANDOM_RECEPTACLE_NUMBER;
    for (int i = 0; i < RANDOM_RECEPTACLE_NUMBER; i++) {
      User user;
      while ((user = userRepo.findById(RandomUtil.randomInt(startUserId, endUserId)).orElse(null)) == null || sceneRepo.findAllByUserId(user.getId()).size() == 0) {
        user = userRepo.findById(RandomUtil.randomInt(startUserId, endUserId)).orElse(null);
      }
      log.info("find User:{}", user.toString());

      List<Scene> scenes = sceneRepo.findAllByUserId(user.getId());

      Receptacle r = Receptacle.builder().name(RandomUtil.randomString(8)).status(0).user(user).scene(RandomUtil.randomEle(scenes)).build();
      r = receptacleRepo.save(r);
      log.info("createElectricLog receptacle:{} successfully", r.getId());

      int jack_num = RandomUtil.randomInt(0, RANDOM_MAX_JACK_PER_RECEPTACLE);
      for (int j = 0; j < jack_num; j++) {
        Jack jack = Jack.builder().name(RandomUtil.randomString(8)).receptacle(r).status(0).type(random.nextInt(2)).build();
        jack = jackRepo.save(jack);
        log.info("createElectricLog jack:{} successfully", jack.getId());
        endJackId++;
      }
    }

    // 构建设备
    Device startDevice = Device.builder().user(startJack.getReceptacle().getScene().getUser()).jack(startJack).name("StartDevice").type("0").status(0).build();
    startDevice = deviceRepo.save(startDevice);
    int startDeviceId = startDevice.getId();
    int endDeviceId = startDeviceId + RANDOM_DEVICE_NUMBBER;
    for (int i = 0; i < RANDOM_DEVICE_NUMBBER; i++) {
      int jackId = RandomUtil.randomInt(startJackId, endJackId);
      while (deviceRepo.existsByJack(jackRepo.findById(jackId).orElse(null))) {
        jackId = RandomUtil.randomInt(startJackId, endJackId);
      }
      Jack jack = jackRepo.findById(jackId).orElse(null);
      if (jack == null || jack.getReceptacle() == null || jack.getReceptacle().getScene().getUser() == null) continue;
      Device device = Device.builder().jack(jack).user(jack.getReceptacle().getScene().getUser()).name(RandomUtil.randomString(8)).status(0).type("0").build();
      deviceRepo.save(device);
    }

    // 构建耗电日志
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DATE, -45);
    long from = calendar.getTime().getTime();
    long to = System.currentTimeMillis();

    for (int i = 0; i < RANDOM_ELELOG_NUMBER; i++) {
      Device device = deviceRepo.findById(RandomUtil.randomInt(startDeviceId, endDeviceId)).orElse(null);
      if (device == null) {
        continue;
      }
      int consumption = RandomUtil.randomInt(0, 150000);
      int power = consumption / 300;
      Date date = new Date(RandomUtil.randomLong(from, to));
      ElectricLog log = ElectricLog.builder().device(device).consumption(consumption).power(power).time(date).build();
      electricLogRepo.save(log);
    }
  }
}
