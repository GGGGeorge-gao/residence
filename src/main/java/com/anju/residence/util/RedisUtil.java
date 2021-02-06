package com.anju.residence.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author cygao
 * @date 2020/11/26 20:27
 **/
@Component
public class RedisUtil {

  private final RedisTemplate<String, Object> redisTemplate;

  @Autowired
  public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

}
