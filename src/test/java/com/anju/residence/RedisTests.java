package com.anju.residence;

import com.anju.residence.entity.User;
import com.anju.residence.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author cygao
 * @date 2020/11/26 19:09
 **/
@SpringBootTest
@Slf4j
class RedisTests {

  private final RedisTemplate<String, Object> redisTemplate;
  private final UserService userService;

  @Autowired
  public RedisTests(RedisTemplate<String, Object> template, UserService userService) {
    this.redisTemplate = template;
    this.userService = userService;
  }

  @Test
  void redisTest1() {
    log.info(String.valueOf(redisTemplate.boundListOps("5").size()));
    log.info(String.valueOf(redisTemplate.delete("k2")));

  }

  @Test
  void redisServiceTest() {
    log.info(userService.getUserById(4).orElse(new User()).toString());
    log.info(userService.getUserById(4).orElse(new User()).toString());
  }
}
