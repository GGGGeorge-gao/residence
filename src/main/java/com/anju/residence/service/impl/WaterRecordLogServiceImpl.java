package com.anju.residence.service.impl;

import com.anju.residence.dao.water.WaterRecordLogRepository;
import com.anju.residence.dto.water.WaterRecordLogDTO;
import com.anju.residence.entity.water.WaterRecordLog;
import com.anju.residence.enums.ResultCode;
import com.anju.residence.exception.ApiException;
import com.anju.residence.service.water.WaterMeterService;
import com.anju.residence.service.water.WaterRecordLogService;
import com.anju.residence.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author cygao
 * @date 2021/1/25 23:07
 **/
@Service
public class WaterRecordLogServiceImpl implements WaterRecordLogService {

  private final WaterRecordLogRepository waterRecordLogRepo;

  private WaterMeterService waterMeterService;

  @Autowired
  public WaterRecordLogServiceImpl(WaterRecordLogRepository waterRecordLogRepo) {
    this.waterRecordLogRepo = waterRecordLogRepo;
  }

  @Autowired
  public void setWaterMeterService(WaterMeterService waterMeterService) {
    this.waterMeterService = waterMeterService;
  }

  @Override
  public Optional<WaterRecordLog> getById(Integer logId) {
    if (!exitsById(logId)) {
      throw new ApiException(ResultCode.WATER_RECORD_LOG_ID_NOT_EXISTS);
    }
    return waterRecordLogRepo.findById(logId);
  }

  @Override
  public List<WaterRecordLog> listByWaterMeterId(Integer waterMeterId) {
    if (!exitsById(waterMeterId)) {
      throw new ApiException(ResultCode.WATER_RECORD_LOG_ID_NOT_EXISTS);
    }
    return waterRecordLogRepo.findAllByWaterMeterId(waterMeterId);
  }

  @Override
  public List<WaterRecordLog> listByWaterMeterIdAt(Integer waterMeterId, Date date) {
    return listByWaterMeterIdBetween(waterMeterId, DateUtil.getFirstSecondDate(date), DateUtil.getLastSecondDate(date));
  }

  @Override
  public List<WaterRecordLog> listByWaterMeterIdBetween(Integer waterMeterId, Date from, Date to) {
    if (from == null || to == null || from.after(to)) {
      throw new ApiException(ResultCode.INVALID_DATE);
    }
    if (!exitsById(waterMeterId)) {
      throw new ApiException(ResultCode.WATER_RECORD_LOG_ID_NOT_EXISTS);
    }
    return waterRecordLogRepo.findAllByWaterMeterIdBetween(waterMeterId, from, to);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public void addWaterRecordLog(WaterRecordLogDTO logDTO) {
    if (!waterMeterService.exitsById(logDTO.getWaterMeterId())) {
      throw new ApiException(ResultCode.WATER_METER_ID_NOT_EXISTS);
    }
    WaterRecordLog waterRecordLog = logDTO.build();

    save(waterRecordLog);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public void deleteById(Integer logId) {
    if (!exitsById(logId)) {
      throw new ApiException(ResultCode.WATER_RECORD_LOG_ID_NOT_EXISTS);
    }
    waterRecordLogRepo.deleteById(logId);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public void deleteByWaterMeterId(Integer waterMeterId) {
    if (!waterMeterService.exitsById(waterMeterId)) {
      throw new ApiException(ResultCode.WATER_METER_ID_NOT_EXISTS);
    }
    waterRecordLogRepo.deleteAllByWaterMeterId(waterMeterId);
  }

  @Override
  public boolean exitsById(Integer logId) {
    if (logId == null || logId <= 0) {
      return false;
    }
    return waterRecordLogRepo.existsById(logId);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public void save(WaterRecordLog waterRecordLog) {
    waterRecordLogRepo.save(waterRecordLog);
  }
}
