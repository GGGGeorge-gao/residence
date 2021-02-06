package com.anju.residence;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

  @Autowired
  public RedisTests(RedisTemplate<String, Object> template) {
    this.redisTemplate = template;
  }

  @Test
  void redisTest1() {
    log.info(String.valueOf(redisTemplate.boundListOps("5").size()));
    log.info(String.valueOf(redisTemplate.delete("k2")));

  }
}
