package com.anju.residence.service.impl;

import com.anju.residence.dao.ele.DeviceLogRepository;
import com.anju.residence.entity.ele.Device;
import com.anju.residence.entity.ele.DeviceLog;
import com.anju.residence.entity.ele.ElectricLog;
import com.anju.residence.enums.ResultCode;
import com.anju.residence.exception.ApiException;
import com.anju.residence.service.ele.DeviceLogService;
import com.anju.residence.service.UserService;
import com.anju.residence.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author cygao
 * @date 2020/12/1 19:25
 **/
@Service
public class DeviceLogServiceImpl implements DeviceLogService {

  private final DeviceLogRepository deviceLogRepo;

  private final UserService userService;

  @Autowired
  public DeviceLogServiceImpl(DeviceLogRepository deviceLogRepo, UserService userService) {
    this.deviceLogRepo = deviceLogRepo;
    this.userService = userService;
  }

  /**
   * 接收到用电日志后更新设备的耗电日志
   */
  @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.MANDATORY, rollbackFor = Exception.class)
  @Override
  public void updateByElectricLog(ElectricLog eleLog) {

    Device device = eleLog.getDevice();

    DeviceLog preLog = getByDeviceIdAndDate(device.getId(), eleLog.getTime()).orElse(DeviceLog.builder().device(device).time(eleLog.getTime()).others(eleLog.getOthers()).build());

    preLog.addConsumption(eleLog.getConsumption());
    deviceLogRepo.save(preLog);

  }

  @Override
  public Optional<DeviceLog> getByDeviceIdAndDate(Integer deviceId, Date date) {
    Date from = DateUtil.getFirstSecondDate(date);
    Date to = DateUtil.getLastSecondDate(date);

    return deviceLogRepo.findByDeviceIdAndTime(deviceId, from, to);
  }

  @Override
  public List<DeviceLog> listLogByDeviceIdBetween(Integer deviceId, Date from, Date to) {
    if (!existsById(deviceId)) {
      throw new ApiException(ResultCode.DEVICE_ERROR, "设备id不存在");
    }
    return deviceLogRepo.findAllByDeviceIdAndTime(deviceId, DateUtil.getFirstSecondDate(from), DateUtil.getLastSecondDate(to));
  }

  @Override
  public List<DeviceLog> listLogByUserIdBetween(Integer userId, Date from, Date to) {
    if (!userService.existsById(userId)) {
      throw new ApiException(ResultCode.USER_ERROR, "用户id不存在");
    }

    return deviceLogRepo.findAllByUserAndTimeBetween(userId, from, to);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public boolean deleteByDeviceId(Integer deviceId) {
    deviceLogRepo.deleteAllByDeviceId(deviceId);
    return true;
  }

  @Override
  public boolean existsById(Integer deviceId) {
    return deviceId != null && deviceLogRepo.existsById(deviceId);
  }
}
