package com.anju.residence;

import com.anju.residence.dao.ele.AlertNoticeRepository;
import com.anju.residence.dao.ele.BehaviorLogRepository;
import com.anju.residence.dao.ele.DeviceLogRepository;
import com.anju.residence.dao.ele.DeviceRepository;
import com.anju.residence.dao.ele.ElectricLogRepository;
import com.anju.residence.dao.ele.JackRepository;
import com.anju.residence.dao.UserLogRepository;
import com.anju.residence.dao.ele.ReceptacleRepository;
import com.anju.residence.dao.RoleRepository;
import com.anju.residence.dao.ele.SceneRepository;
import com.anju.residence.dao.UserRepository;
import com.anju.residence.entity.ele.AlertNotice;
import com.anju.residence.entity.ele.Device;
import com.anju.residence.entity.ele.ElectricLog;
import com.anju.residence.entity.ele.Receptacle;
import com.anju.residence.entity.Role;
import com.anju.residence.entity.ele.Scene;
import com.anju.residence.entity.User;
import com.anju.residence.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@SpringBootTest
@Slf4j
class RepositoryTests {

  private final AlertNoticeRepository alertRepo;
  private final DeviceRepository deviceRepo;
  private final JackRepository jackRepo;
  private final UserLogRepository loginRepo;
  private final ReceptacleRepository receptacleRepo;
  private final RoleRepository roleRepo;
  private final SceneRepository sceneRepo;
  private final UserRepository userRepo;
  private final DeviceLogRepository deviceLogRepo;
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
  public RepositoryTests(AlertNoticeRepository alertRepo, DeviceRepository deviceRepo, JackRepository jackRepo, UserLogRepository loginRepo, ReceptacleRepository receptacleRepo, RoleRepository roleRepo, SceneRepository sceneRepo, UserRepository userRepo, DeviceLogRepository deviceLogRepo, ElectricLogRepository electricLogRepo, BehaviorLogRepository behaviorLogRepo) {
    this.alertRepo = alertRepo;
    this.deviceRepo = deviceRepo;
    this.jackRepo = jackRepo;
    this.loginRepo = loginRepo;
    this.receptacleRepo = receptacleRepo;
    this.roleRepo = roleRepo;
    this.sceneRepo = sceneRepo;
    this.userRepo = userRepo;
    this.deviceLogRepo = deviceLogRepo;
    this.electricLogRepo = electricLogRepo;
    this.behaviorLogRepo = behaviorLogRepo;
  }

  /**
   * Role模板层测试
   */
  @Test
  public void roleRepoTest() {
    // 初始化两种权限
    Role ordinary = Role.builder().roleName("ORDINARY").build();
    Role vip = Role.builder().roleName("VIP").build();

    // 判断添加
    if (!roleRepo.existsByRoleName(ordinary.getRoleName())) roleRepo.save(ordinary);
    if (!roleRepo.existsByRoleName(vip.getRoleName())) roleRepo.save(vip);

    Role testNull = roleRepo.findByRoleName("AAA");
    if (testNull != null) log.info(testNull.toString());

    // 查找权限
    log.info(roleRepo.findByRoleName("ORDINARY").toString());
    log.info(roleRepo.findByRoleName("VIP").toString());
  }

  @Test
  public void userTest() {
    Role ordinary = roleRepo.findByRoleName("ORDINARY");
    Role vip = roleRepo.findByRoleName("VIP");

    if (!userRepo.existsByUsername("TEST")) {
      userRepo.save(User.builder().username("TEST").password("TEST").email("TEST").phone("123123123").address("TEST").roles(Arrays.asList(ordinary, vip)).build());
    }

    for (int i = 0; i < 300; i++) {
      String name = "TEST" + random.nextInt(10000);
      if (userRepo.existsByUsername(name)) continue;
      User u1 = User.builder().username(name).password("TEST").email("TEST").phone(String.valueOf(random.nextInt(99999999))).address("TEST").roles(Arrays.asList(ordinary, vip)).build();
      log.info(u1.toString());
      userRepo.save(u1);
      log.info(userRepo.findUserByUsername(name).orElse(new User()).toString());
    }
  }

  @Test
  public void userUpdateTest() throws Exception {
    User u = userRepo.findUserByUsername("TEST").orElseThrow(Exception::new);
    if (u != null) {
      u.setGender(1);
      userRepo.save(u);
      log.info(userRepo.findUserByUsername(u.getUsername()).orElse(new User()).toString());
    }
  }

  @Test
  @Transactional
  public void userDeleteTest() {
    String name = "TEST2100";
    if (userRepo.existsByUsername(name)) {
      log.info("exists username: {}", name);
      userRepo.deleteUserByUsername(name);
      log.info("delete success");
    }
  }

  @Test
  public void alertTest() {
    String name = "TEST";
    User u;
    if (!userRepo.existsByUsername(name)) {
      u = User.builder().username(name).password("TEST").email("TEST").address("TEST").roles(Arrays.asList(roleRepo.findByRoleName("ORDINARY"))).build();
      userRepo.save(u);
    }
    u = userRepo.findUserByUsername(name).orElse(null);

    AlertNotice alertNoticeInfo = AlertNotice.builder().user(u).dingToken("dingding").emailAuth("email").build();
    if (!alertRepo.existsByUser(u)) {
      alertRepo.save(alertNoticeInfo);
    }
    log.info(alertRepo.findByUser(u).toString());
  }

  @Test
  public void sceneTest() {
    User u = userRepo.findUserByUsername("TEST").orElse(null);
    if (u == null) {
      log.error("User:TEST does not exist");
      return;
    }
    log.info(u.toString());
    String name = "scene" + random.nextInt(1000);
    Scene scene1 = Scene.builder().name(name).description("TEST").user(u).build();

    sceneRepo.save(scene1);

//    log.info(sceneRepo.findFirstByUserAndIdName(u.getId(), name).toString());
  }

  @Test
  public void receptacleTest() {
    User user = userRepo.findUserByUsername("TEST").orElse(null);
    if (user == null) {
      log.error("User:TEST does not exist");
      return;
    }
    Scene scene = sceneRepo.findAllByUserId(user.getId()).get(0);
    if (!receptacleRepo.findIdByUserIdAndName(user.getId(), "TEST").isPresent()) {
      receptacleRepo.save(Receptacle.builder().name("TEST").status(0).scene(scene).user(user).build());
    }

    for (int i = 0; i < 50; i++) {
      String name = "TEST" + random.nextInt(1000);
      if (receptacleRepo.findIdByUserIdAndName(user.getId(), name).isPresent()) continue;
      Receptacle receptacle1 = Receptacle.builder().name(name).scene(scene).status(0).user(user).build();
      log.info(receptacle1.toString());
      receptacleRepo.save(receptacle1);
      log.info(receptacleRepo.findFirstByUserAndName(user, name).toString());
    }
  }

  @Transactional
  @Rollback(value = false)
  @Test
  void script() {
    User user = userRepo.findById(1800).orElseThrow(RuntimeException::new);
    List<Device> devices = deviceRepo.findAllDeviceByUserId(user.getId());
    log.info(devices.toString());

    for (Device device : devices) {
      int num = random.nextInt(5);
      for (int i = 0; i < num; i++) {
        int power = random.nextInt(300);
        ElectricLog electricLog = ElectricLog.builder().power(power).consumption(power * 5).time(DateUtil.getDateMinusMinute(i * 5)).device(device).build();
        electricLogRepo.save(electricLog);
      }
    }
  }

  @Test
  void saveTest() {
  }


}
