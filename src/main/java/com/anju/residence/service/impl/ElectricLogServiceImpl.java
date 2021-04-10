package com.anju.residence.service.impl;

import com.anju.residence.dao.ele.ElectricLogRepository;
import com.anju.residence.entity.ele.ElectricLog;
import com.anju.residence.enums.ResultCode;
import com.anju.residence.exception.ApiException;
import com.anju.residence.service.ele.DeviceLogService;
import com.anju.residence.service.ele.DeviceService;
import com.anju.residence.service.ele.ElectricLogService;
import com.anju.residence.service.ele.ReceptacleLogService;
import com.anju.residence.service.UserService;
import com.anju.residence.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.Positive;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author cygao
 * @date 2020/12/1 16:19
 **/
@Slf4j
@Service
public class ElectricLogServiceImpl implements ElectricLogService {

  private final ElectricLogRepository eleLogRepo;

  private final UserService userService;
  private final ReceptacleLogService receptacleLogService;

  private DeviceService deviceService;
  private DeviceLogService deviceLogService;

  @Autowired
  public ElectricLogServiceImpl(ElectricLogRepository eleLogRepo, UserService userService, ReceptacleLogService receptacleLogService) {
    this.eleLogRepo = eleLogRepo;
    this.userService = userService;
    this.receptacleLogService = receptacleLogService;
  }

  @Autowired
  public void setDeviceService(DeviceService deviceService) {
    this.deviceService = deviceService;
  }

  @Autowired
  public void setDeviceLogService(DeviceLogService deviceLogService) {
    this.deviceLogService = deviceLogService;
  }

  @Override
  public Optional<ElectricLog> getLatestLogByDeviceId(Integer deviceId) {
    return eleLogRepo.findLatestLogByDeviceId(deviceId);
  }

  @Override
  public Optional<ElectricLog> getRealTimeLogByDeviceId(int deviceId) {
    Date from = DateUtil.getDateMinusUploadInterval();
    Date to = DateUtil.now();

    return Optional.ofNullable(eleLogRepo.findLatestLogByDeviceAndTimeBetween(deviceId, from, to));
  }

  @Override
  public List<ElectricLog> listLogByDeviceIdBetweenHour(int deviceId, @Positive(message = "hour must be positive") int hour) {
    Date from = DateUtil.getDateMinusHour(hour);
    Date to = DateUtil.now();

    return eleLogRepo.findAllByDeviceAndTimeBetween(deviceId, from, to);
  }

  @Override
  public int getRealTimePower(Integer userId) {
    if (userId == null || !userService.existsById(userId)) {
      throw new ApiException(ResultCode.USER_ERROR, "用户id不存在");
    }
    List<Integer> devices = deviceService.listDeviceIdByUserId(userId);
    if (devices.size() == 0) {
      return 0;
    }

    Date to = DateUtil.now();
    Date from = DateUtil.getDateMinusUploadInterval();

    return devices.stream().mapToInt(id -> eleLogRepo.findLatestPowerByDeviceId(id, from, to).orElse(0)).sum();
  }

  @Override
  public Integer getDeviceIdByElectricLogId(long electricLogId) {
    return eleLogRepo.findDeviceIdByElectricLogId(electricLogId);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public boolean deleteByDeviceId(int deviceId) {
    eleLogRepo.deleteAllByDeviceId(deviceId);
    return true;
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public boolean addLog(ElectricLog eleLog) {
    if (!deviceService.existsById(eleLog.getDevice().getId())) {
      throw new ApiException(ResultCode.DEVICE_ERROR, "设备id不存在");
    }

    eleLogRepo.save(eleLog);
    deviceLogService.updateByElectricLog(eleLog);
    receptacleLogService.updateByElectricLog(eleLog);
    return true;
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public void save(ElectricLog log) {
    eleLogRepo.save(log);
  }

}
