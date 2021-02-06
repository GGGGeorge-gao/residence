package com.anju.residence.service.ele;

import com.anju.residence.entity.ele.ElectricLog;

import java.util.List;
import java.util.Optional;

/**
 * @author cygao
 * @date 2020/11/28 19:05
 **/
public interface ElectricLogService {

  /**
   * 获取设备最新的耗电日志
   *
   * @param deviceId 设备Id
   * @return 最新的耗电日志
   */
  Optional<ElectricLog> getLatestLogByDeviceId(Integer deviceId);

  /**
   * 获取设备实时的耗电日志
   *
   * @param deviceId 设备id
   * @return 实时的耗电日志
   */
  Optional<ElectricLog> getRealTimeLogByDeviceId(int deviceId);

  /**
   * 获取设备小时内的所有用电日志
   *
   * @param deviceId 设备
   * @param hour 小时数
   * @return 今日所有用电日志
   */
  List<ElectricLog> listLogByDeviceIdBetweenHour(int deviceId, int hour);

  /**
   * 查询该用户当前实时功耗
   *
   * @param userId 用户id
   * @return 实时功率
   */
  int getRealTimePower(Integer userId);

  /**
   * 根据耗电日志id查找该日志所属设备id
   *
   * @param electricLogId 耗电日志id
   * @return 设备id
   */
  Integer getDeviceIdByElectricLogId(long electricLogId);

  /**
   * 删除指定设备的所有耗电日志
   *
   * @param deviceId 设备ID
   * @return 是否删除成功
   */
  boolean deleteByDeviceId(int deviceId);

  /**
   * 添加耗电日志
   *
   * @param log 耗电日志
   * @return 是否添加成功
   */
  boolean addLog(ElectricLog log);

  /**
   * 保存耗电日志
   *
   * @param log 耗电日志
   */
  void save(ElectricLog log);

}
