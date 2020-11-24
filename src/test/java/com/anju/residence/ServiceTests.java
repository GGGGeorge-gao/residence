package com.anju.residence;

import com.anju.residence.entity.User;
import com.anju.residence.service.AlertService;
import com.anju.residence.service.ReceptacleService;
import com.anju.residence.service.SceneService;
import com.anju.residence.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

/**
 * @author cygao
 * @date 2020/11/24 16:04
 **/
@Slf4j
@SpringBootTest
class ServiceTests {
  
  private final ReceptacleService receptacleService;
  private final UserService userService;
  private final AlertService alertService;
  private final SceneService sceneService;

  @Autowired
  public ServiceTests(ReceptacleService receptacleService, UserService userService, AlertService alertService, SceneService sceneService) {
    this.receptacleService = receptacleService;
    this.userService = userService;
    this.alertService = alertService;
    this.sceneService = sceneService;
  }

  @Test
  void receptacleServiceTest() {
    String username = "TEST";
    User user = userService.findUserByName(username);
    log.info(new ArrayList<>(receptacleService.findAll(user)).toString());
    log.info(new ArrayList<>(receptacleService.findAll(user.getId())).toString());

  }
}
