package com.anju.residence.service.impl;

import com.anju.residence.dao.water.WaterMeterRepository;
import com.anju.residence.dto.water.WaterMeterDTO;
import com.anju.residence.entity.water.WaterMeter;
import com.anju.residence.enums.ResultCode;
import com.anju.residence.exception.ApiException;
import com.anju.residence.security.jwt.JwtAuthenticationToken;
import com.anju.residence.security.jwt.JwtTokenUtil;
import com.anju.residence.security.model.UserDetailsImpl;
import com.anju.residence.service.UserService;
import com.anju.residence.service.water.WaterMeterService;
import com.anju.residence.service.water.WaterRecordLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author cygao
 * @date 2021/1/25 17:58
 **/
@Service
public class WaterMeterServiceImpl implements WaterMeterService {

  private final WaterMeterRepository waterMeterRepo;

  private final UserService userService;

  private WaterRecordLogService waterRecordLogService;

  @Autowired
  public WaterMeterServiceImpl(WaterMeterRepository waterMeterRepo, UserService userService) {
    this.waterMeterRepo = waterMeterRepo;
    this.userService = userService;
  }

  @Autowired
  public void setWaterRecordLogService(WaterRecordLogService waterRecordLogService) {
    this.waterRecordLogService = waterRecordLogService;
  }

  @Override
  public Optional<WaterMeter> getById(Integer waterMeterId) {
    if (!exitsById(waterMeterId)) {
      throw new ApiException(ResultCode.WATER_METER_ID_NOT_EXISTS);
    }
    return waterMeterRepo.findById(waterMeterId);
  }

  @Override
  public List<WaterMeter> listByUserId(Integer userId) {
    if (!userService.existsById(userId)) {
      throw new ApiException(ResultCode.USER_ID_NOT_EXISTS);
    }

    return waterMeterRepo.findAllByUserId(userId);
  }

  @Override
  public boolean exitsById(Integer waterMeterId) {
    if (waterMeterId == null) {
      return false;
    }
    return waterMeterRepo.existsById(waterMeterId);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public WaterMeter addWaterMeter(WaterMeterDTO waterMeterDTO) {
    if (!JwtTokenUtil.checkUserAuthentication(waterMeterDTO.getUserId())) {
      throw new ApiException(ResultCode.UNAUTHORIZED_REQUEST);
    }
    if (!userService.existsById(waterMeterDTO.getUserId())) {
      throw new ApiException(ResultCode.USER_ID_NOT_EXISTS);
    }
    WaterMeter waterMeter = waterMeterDTO.build();

    return save(waterMeter);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public void updateWaterMeter(WaterMeterDTO waterMeterDTO, Integer waterMeterId) {
    if (!JwtTokenUtil.checkUserAuthentication(waterMeterDTO.getUserId())) {
      throw new ApiException(ResultCode.UNAUTHORIZED_REQUEST);
    }
    if (!userService.existsById(waterMeterDTO.getUserId())) {
      throw new ApiException(ResultCode.USER_ID_NOT_EXISTS);
    }
    if (waterMeterId == null || !waterMeterId.equals(waterMeterDTO.getId()) || exitsById(waterMeterId)) {
      throw new ApiException(ResultCode.WATER_METER_ID_NOT_EXISTS);
    }

    WaterMeter waterMeter = waterMeterDTO.build();

    save(waterMeter);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public void deleteById(Integer waterMeterId) {
    if (!exitsById(waterMeterId)) {
      throw new ApiException(ResultCode.WATER_METER_ID_NOT_EXISTS);
    }
    // OperationLog 记得删除
    waterRecordLogService.deleteByWaterMeterId(waterMeterId);

    waterMeterRepo.deleteById(waterMeterId);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public WaterMeter save(WaterMeter waterMeter) {
    return waterMeterRepo.save(waterMeter);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public void updateWaterMeterCount(Integer waterMeterId, Double count) {
    waterMeterRepo.updateCount(waterMeterId, count);
  }
}
