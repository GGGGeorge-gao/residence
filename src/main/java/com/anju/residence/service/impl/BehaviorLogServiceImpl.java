package com.anju.residence.service.impl;

import com.anju.residence.dao.ele.BehaviorLogRepository;
import com.anju.residence.dto.ele.BehaviorLogDTO;
import com.anju.residence.entity.ele.BehaviorLog;
import com.anju.residence.enums.ResultCode;
import com.anju.residence.exception.ApiException;
import com.anju.residence.service.ele.BehaviorLogService;
import com.anju.residence.service.ele.DeviceService;
import com.anju.residence.service.ele.JackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author cygao
 * @date 2020/12/8 13:00
 **/
@Service
public class BehaviorLogServiceImpl implements BehaviorLogService {

  private final BehaviorLogRepository logRepo;

  private DeviceService deviceService;
  private JackService jackService;

  @Autowired
  public BehaviorLogServiceImpl(BehaviorLogRepository logRepo) {
    this.logRepo = logRepo;
  }
  @Autowired
  public void setJackService(JackService jackService) {
    this.jackService = jackService;
  }
  @Autowired
  public void setDeviceService(DeviceService deviceService) {
    this.deviceService = deviceService;
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public void add(BehaviorLogDTO behaviorLogDTO) {
    if (behaviorLogDTO.getDeviceId() != null) {
      deviceService.updateStatus(behaviorLogDTO.getDeviceId(), behaviorLogDTO.getDeviceStatus());
    }
    jackService.updateStatus(behaviorLogDTO.getJackId(), behaviorLogDTO.getJackStatus());

    BehaviorLog behaviorLog = behaviorLogDTO.build();
    logRepo.save(behaviorLog);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public void deleteByJackId(int jackId) {
    logRepo.deleteAllByJackId(jackId);
  }

  @Override
  public void deleteByDeviceId(int deviceId) {
    logRepo.deleteAllByDeviceId(deviceId);
  }

  @Override
  public List<BehaviorLog> listByDeviceIdBetween(Integer deviceId, Date from, Date to) {
    if (from == null || to == null || from.after(to)) {
      throw new ApiException(ResultCode.INVALID_DATE);
    }
    if (!deviceService.existsById(deviceId)) {
      throw new ApiException(ResultCode.DEVICE_ID_NOT_EXISTS);
    }
    return logRepo.findAllByDeviceIdBetween(deviceId, from, to);
  }

  @Override
  public List<BehaviorLog> listByJackIdBetween(Integer jackId, Date from, Date to) {
    return null;
  }
}
