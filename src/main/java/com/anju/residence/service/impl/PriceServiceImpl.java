package com.anju.residence.service.impl;

import com.anju.residence.dao.UserPriceRepository;
import com.anju.residence.dto.UserPriceDTO;
import com.anju.residence.entity.UserPrice;
import com.anju.residence.enums.ResultCode;
import com.anju.residence.exception.ApiException;
import com.anju.residence.util.JwtTokenUtil;
import com.anju.residence.service.PriceService;
import com.anju.residence.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author cygao
 * @date 2021/2/8 10:21 上午
 **/
@Service
public class PriceServiceImpl implements PriceService {

  private final UserPriceRepository userPriceRepo;

  private final UserService userService;

  public PriceServiceImpl(UserPriceRepository userPriceRepo, UserService userService) {
    this.userPriceRepo = userPriceRepo;
    this.userService = userService;
  }

  @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
  @Override
  public void addPrice(UserPriceDTO userPriceDTO) {
    if (!JwtTokenUtil.checkUserAuthentication(userPriceDTO.getUserId())) {
      throw new ApiException(ResultCode.UNAUTHORIZED_REQUEST);
    }
    if (existsByUserId(userPriceDTO.getUserId())) {
      throw new ApiException(ResultCode.USER_ERROR, "该用户价格信息已存在");
    }
    save(userPriceDTO.build());
  }

  @Override
  public void putPrice(UserPriceDTO userPriceDTO) {
    if (!JwtTokenUtil.checkUserAuthentication(userPriceDTO.getUserId())) {
      throw new ApiException(ResultCode.UNAUTHORIZED_REQUEST);
    }
    UserPrice price = userPriceRepo.findByUserId(userPriceDTO.getUserId()).orElseThrow(() -> new ApiException(ResultCode.USER_ERROR, "用户价格信息不存在"));
    userPriceDTO.putUserPrice(price);
    save(price);
  }

  @Override
  public UserPrice getByUserId(Integer userId) {
    return userPriceRepo.findByUserId(userId).orElseThrow(() -> new ApiException(ResultCode.USER_ERROR, "用户价格信息不存在"));
  }

  @Override
  public boolean existsByUserId(Integer userId) {
    return userId != null && userPriceRepo.countByUserId(userId) >= 1;
  }

  @Override
  public void save(UserPrice price) {
    userPriceRepo.save(price);
  }
}
