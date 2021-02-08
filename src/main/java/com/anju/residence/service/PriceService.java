package com.anju.residence.service;

import com.anju.residence.dto.UserPriceDTO;
import com.anju.residence.entity.UserPrice;

/**
 * @author cygao
 * @date 2021/2/8 10:16 上午
 **/
public interface PriceService {

  /**
   * 添加用户价格
   * @param userPriceDTO 价格传输实体类
   */
  void addPrice(UserPriceDTO userPriceDTO);

  /**
   * 修改用户价格
   * @param userPriceDTO 价格传输实体类
   */
  void putPrice(UserPriceDTO userPriceDTO);

  /**
   * 查询用户价格
   * @param userId 用户id
   * @return 价格实体类
   */
  UserPrice getByUserId(Integer userId);

  /**
   * 查询用户价格是否存在
   * @param userId 用户id
   * @return 是否存在
   */
  boolean existsByUserId(Integer userId);

  /**
   * 保存价格类
   * @param price 价格实体类对象
   */
  void save(UserPrice price);
}
