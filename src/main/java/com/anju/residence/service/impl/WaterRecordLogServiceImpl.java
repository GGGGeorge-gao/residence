package com.anju.residence.service.impl;

import com.anju.residence.dao.OcrRepository;
import com.anju.residence.dao.water.WaterRecordLogRepository;
import com.anju.residence.dto.OcrResult;
import com.anju.residence.dto.water.WaterRecordLogDTO;
import com.anju.residence.entity.water.WaterMeter;
import com.anju.residence.entity.water.WaterRecordLog;
import com.anju.residence.enums.ResultCode;
import com.anju.residence.exception.ApiException;
import com.anju.residence.service.water.WaterMeterService;
import com.anju.residence.service.water.WaterRecordLogService;
import com.anju.residence.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
  private final OcrRepository ocrRepo;

  private WaterMeterService waterMeterService;

  @Autowired
  public WaterRecordLogServiceImpl(WaterRecordLogRepository waterRecordLogRepo, OcrRepository ocrRepo) {
    this.waterRecordLogRepo = waterRecordLogRepo;
    this.ocrRepo = ocrRepo;
  }

  @Autowired
  public void setWaterMeterService(WaterMeterService waterMeterService) {
    this.waterMeterService = waterMeterService;
  }

  @Override
  public Optional<WaterRecordLog> getById(Integer logId) {
    if (!exitsById(logId)) {
      throw new ApiException(ResultCode.WATER_RECORD_LOG_ERROR, "水表抄表日志不存在");
    }
    return waterRecordLogRepo.findById(logId);
  }

  @Override
  public List<WaterRecordLog> listByWaterMeterId(Integer waterMeterId) {
    if (!exitsById(waterMeterId)) {
      throw new ApiException(ResultCode.WATER_RECORD_LOG_ERROR, "水表抄表日志不存在");
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
      throw new ApiException(ResultCode.INVALID_ARGUMENT, "无效的日期参数");
    }
    if (!exitsById(waterMeterId)) {
      throw new ApiException(ResultCode.WATER_RECORD_LOG_ERROR, "水表抄表日志不存在");
    }
    return waterRecordLogRepo.findAllByWaterMeterIdBetween(waterMeterId, from, to);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public void addOcrResult(OcrResult ocrResult, int waterMeterId) {
    WaterRecordLog waterRecordLog = WaterRecordLog.builder()
            .waterMeter(WaterMeter.builder().id(waterMeterId).build())
            .time(new Date())
            .count(new BigDecimal(ocrResult.getResultNum()[0]))
            .build();

    save(waterRecordLog);
    ocrRepo.save(ocrResult.buildOcr(waterMeterId));
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public void addWaterRecordLog(WaterRecordLogDTO logDTO) {
    if (!waterMeterService.exitsById(logDTO.getWaterMeterId())) {
      throw new ApiException(ResultCode.WATER_METER_ERROR, "水表id不存在");
    }
    WaterRecordLog waterRecordLog = logDTO.build();

    save(waterRecordLog);
    waterMeterService.updateWaterMeterCount(logDTO);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public void deleteById(Integer logId) {
    if (!exitsById(logId)) {
      throw new ApiException(ResultCode.WATER_RECORD_LOG_ERROR, "水表抄表日志不存在");
    }
    waterRecordLogRepo.deleteById(logId);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public void deleteByWaterMeterId(Integer waterMeterId) {
    if (!waterMeterService.exitsById(waterMeterId)) {
      throw new ApiException(ResultCode.WATER_METER_ERROR, "水表id不存在");
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
