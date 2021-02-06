package com.anju.residence;

import cn.hutool.core.util.RandomUtil;
import com.anju.residence.entity.ele.Jack;
import com.anju.residence.entity.ele.Receptacle;
import com.anju.residence.entity.ele.ReceptacleLog;
import com.anju.residence.entity.ele.Scene;
import com.anju.residence.service.ele.AlertNoticeService;
import com.anju.residence.service.ele.DeviceLogService;
import com.anju.residence.service.ele.ElectricLogService;
import com.anju.residence.service.ele.JackService;
import com.anju.residence.service.ele.ReceptacleLogService;
import com.anju.residence.service.ele.ReceptacleService;
import com.anju.residence.service.SceneService;
import com.anju.residence.service.UserService;
import com.anju.residence.service.impl.AlertNoticeServiceImpl;
import com.anju.residence.service.impl.DeviceLogServiceImpl;
import com.anju.residence.service.impl.ElectricLogServiceImpl;
import com.anju.residence.service.impl.ReceptacleLogServiceImpl;
import com.anju.residence.service.impl.ReceptacleServiceImpl;
import com.anju.residence.service.impl.SceneServiceImpl;
import com.anju.residence.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

/**
 * @author cygao
 * @date 2020/11/24 16:04
 **/
@Slf4j
@SpringBootTest
class ServiceTests {

  private final ReceptacleService receptacleService;
  private final UserService userService;
  private final AlertNoticeService alertService;
  private final SceneService sceneService;
  private final ElectricLogService eleLogService;
  private final DeviceLogService deviceLogService;
  private final ReceptacleLogService recLogService;
  @Autowired
  private JackService jackService;

  private static final Random random = new Random();

  @Autowired
  public ServiceTests(ReceptacleServiceImpl receptacleServiceImpl, UserServiceImpl userServiceImpl, AlertNoticeServiceImpl alertServiceImpl, SceneServiceImpl sceneServiceImpl, ElectricLogServiceImpl eleLogService, DeviceLogServiceImpl deviceLogService, ReceptacleLogServiceImpl recLogService) {
    this.receptacleService = receptacleServiceImpl;
    this.userService = userServiceImpl;
    this.alertService = alertServiceImpl;
    this.sceneService = sceneServiceImpl;
    this.eleLogService = eleLogService;
    this.deviceLogService = deviceLogService;
    this.recLogService = recLogService;
  }

  @Transactional
  @Rollback(value = false)
  @Test
  void deleteSceneTest() {
    log.info("GOGO");
    Scene scene = sceneService.getById(2177).orElse(new Scene());
    log.info(scene.toString());
    if (scene.getId() == 0) return;
    receptacleService.clearScene(scene.getId());
    sceneService.deleteScene(scene.getId());

    log.info(sceneService.getById(2177).orElse(new Scene()).toString());
  }

  @Transactional
  @Rollback(value = false)
  @Test
  public void deleteReceptacleTest() {
    int id = RandomUtil.randomInt(2000, 3000);
    log.info(String.valueOf(id));
    Receptacle receptacle = receptacleService.getById(id).orElse(new Receptacle());
    log.info(receptacle.toString());
    if (receptacle.getId() == 0) return;

    List<Jack> jacks = jackService.listJacksByReceptacle(receptacle.getId());
    List<ReceptacleLog> logs = recLogService.listByReceptacleId(receptacle.getId());
    log.info(String.valueOf(jacks.size()));
    for (Jack jack : jacks) {
      log.info(jack.toString());
    }
    log.info(String.valueOf(logs.size()));
    for (ReceptacleLog rl : logs) {
      log.info(rl.toString());
    }

    receptacleService.deleteReceptacle(receptacle.getId());

    jacks = jackService.listJacksByReceptacle(receptacle.getId());
    logs = recLogService.listByReceptacleId(receptacle.getId());
    log.info(String.valueOf(jacks.size()));
    for (Jack jack : jacks) {
      log.info(jack.toString());
    }
    log.info(String.valueOf(logs.size()));
    for (ReceptacleLog rl : logs) {
      log.info(rl.toString());
    }
    log.info(receptacleService.getById(id).orElse(new Receptacle()).toString());
  }

  @Test
  void asd() {
    int userId = 1958;
    System.out.println(jackService.listJacksByUserId(userId));
  }
}