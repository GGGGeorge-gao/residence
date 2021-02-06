package com.anju.residence.service.ele;

import com.anju.residence.entity.ele.DeviceLog;
import com.anju.residence.entity.ele.ElectricLog;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author cygao
 * @date 2020/11/28 19:03
 **/
public interface DeviceLogService {

  /**
   * 通过接收到的耗电日志更新设备的用电日志
   *
   * @param log 耗电日志
   */
  void updateByElectricLog(ElectricLog log);

  /**
   * 获取设备某一天的用电日志
   *
   * @param deviceId 设备id
   * @param date     日期
   * @return 设备在该天的用电日志的Optional对象
   */
  Optional<DeviceLog> getByDeviceIdAndDate(Integer deviceId, Date date);

  /**
   * 获取设备一段时间内所有的用电日志
   *
   * @param deviceId 设备id
   * @param from  开始时间
   * @param to  结束时间
   * @return 该时间段内该设备所有的用电日志
   */
  List<DeviceLog> listLogByDeviceIdBetween(Integer deviceId, Date from, Date to);

  /**
   * 获取用户一段时间内所有设备的用电日志
   *
   * @param userId 用户id
   * @param from   开始时间
   * @param to     结束时间
   * @return 该时间段内属于该用户所有的设备用电日志
   */
  List<DeviceLog> listLogByUserIdBetween(Integer userId, Date from, Date to);

  /**
   * 删除指定设备的所有用电日志
   *
   * @param deviceId 设备ID
   * @return 是否删除成功
   */
  boolean deleteByDeviceId(Integer deviceId);

  /**
   * 是否存在该设备id
   * @param deviceId 设备id
   * @return 是否存在
   */
  boolean existsById(Integer deviceId);

}
