package com.anju.residence;

import com.anju.residence.dao.AlertInfoRepository;
import com.anju.residence.dao.DeviceRepository;
import com.anju.residence.dao.JackRepository;
import com.anju.residence.dao.LoginInfoRepository;
import com.anju.residence.dao.ReceptacleRepository;
import com.anju.residence.dao.RoleRepository;
import com.anju.residence.dao.SceneRepository;
import com.anju.residence.dao.UserRepository;
import com.anju.residence.entity.AlertInfo;
import com.anju.residence.entity.Receptacle;
import com.anju.residence.entity.Role;
import com.anju.residence.entity.Scene;
import com.anju.residence.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Random;

@SpringBootTest
@Slf4j
class RepositoryTests {

  private static AlertInfoRepository alertRepo;
  private static DeviceRepository deviceRepo;
  private static JackRepository jackRepo;
  private static LoginInfoRepository loginRepo;
  private static ReceptacleRepository receptacleRepo;
  private static RoleRepository roleRepo;
  private static SceneRepository sceneRepo;
  private static UserRepository userRepo;

  private static final Random random = new Random();

  @Autowired
  public RepositoryTests(AlertInfoRepository alertRepo, DeviceRepository deviceRepo, JackRepository jackRepo, LoginInfoRepository loginRepo, ReceptacleRepository receptacleRepo, RoleRepository roleRepo, SceneRepository sceneRepo, UserRepository userRepo) {
    RepositoryTests.alertRepo = alertRepo;
    RepositoryTests.deviceRepo = deviceRepo;
    RepositoryTests.jackRepo = jackRepo;
    RepositoryTests.loginRepo = loginRepo;
    RepositoryTests.receptacleRepo = receptacleRepo;
    RepositoryTests.roleRepo = roleRepo;
    RepositoryTests.sceneRepo = sceneRepo;
    RepositoryTests.userRepo = userRepo;
  }

  /**
   * Role模板层测试
   */
  @Test
  public void roleRepoTest() {
    // 初始化两种权限
    Role ordinary = Role.builder().name("ORDINARY").build();
    Role vip = Role.builder().name("VIP").build();

    // 判断添加
    if (!roleRepo.existsByName(ordinary.getName())) roleRepo.save(ordinary);
    if (!roleRepo.existsByName(vip.getName())) roleRepo.save(vip);

    Role testNull = roleRepo.findByName("AAA");
    if (testNull != null) log.info(testNull.toString());

    // 查找权限
    log.info(roleRepo.findByName("ORDINARY").toString());
    log.info(roleRepo.findByName("VIP").toString());
  }

  @Test
  public void userTest() {
    Role ordinary = roleRepo.findByName("ORDINARY");
    Role vip = roleRepo.findByName("VIP");
    String name = "TEST" + random.nextInt(10000);
    User u1 = User.builder().username(name).password("TEST").email("TEST").address("TEST").roles(Arrays.asList(ordinary, vip)).build();
    log.info(u1.toString());
    userRepo.save(u1);
    log.info(userRepo.findUserByUsername(name).toString());
  }

  @Test
  public void userUpdateTest() {
    User u = userRepo.findUserByUsername("TEST2260");
    u.setGender(0);
    userRepo.save(u);
    log.info(userRepo.findUserByUsername(u.getUsername()).toString());
  }

  @Test
  public void alertTest() {
    String name = "TEST";
    User u;
    if (!userRepo.existsUserByUsername(name)) {
      u = User.builder().username(name).password("TEST").email("TEST").address("TEST").roles(Arrays.asList(roleRepo.findByName("ORDINARY"))).build();
      userRepo.save(u);
    }
    u = userRepo.findUserByUsername(name);

    AlertInfo alertInfo1 = AlertInfo.builder().user(u).dingToken("dingding").emailAuth("email").build();
    log.info(alertInfo1.toString());
    alertRepo.save(alertInfo1);
  }

  @Test
  public void sceneTest() {
    User u = userRepo.findUserByUsername("TEST");
    log.info(u.toString());
    String name = "scene" + random.nextInt(1000);
    Scene scene1 = Scene.builder().name(name).description("TEST").user(u).build();

    sceneRepo.save(scene1);

    log.info(sceneRepo.findFirstByUserAndName(u, name).toString());
  }

  @Test
  public void receptacleTest() {
    User user = userRepo.findUserByUsername("TEST");
    String name = "TEST" + random.nextInt(1000);
    Receptacle receptacle1 = Receptacle.builder().name(name).status(0).user(user).build();
    log.info(receptacle1.toString());
    receptacleRepo.save(receptacle1);
    log.info(receptacleRepo.findFirstByUserAndName(user, name).toString());
  }


}
