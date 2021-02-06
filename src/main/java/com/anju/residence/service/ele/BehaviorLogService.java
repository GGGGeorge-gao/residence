package com.anju.residence.service.ele;

import com.anju.residence.dto.ele.BehaviorLogDTO;
import com.anju.residence.entity.ele.BehaviorLog;

import java.util.Date;
import java.util.List;

/**
 * @author cygao
 * @date 2020/12/8 12:58
 **/
public interface BehaviorLogService {

  /**
   * 添加一条操作日志
   * @param behaviorLogDTO 操作日志dto对象
   */
  void add(BehaviorLogDTO behaviorLogDTO);

  /**
   * 删除该插孔的所有日志，<p>应保证 jackId 存在且不为null</p>
   *
   * @param jackId 插孔ID
   */
  void deleteByJackId(int jackId);

  /**
   * 删除该设备的所有日志，<p>应保证 deviceId 存在且不为null</p>
   * @param deviceId 设备id
   */
  void deleteByDeviceId(int deviceId);

  /**
   * 获取设备一段时间内的操作日志
   * @param deviceId 设备id
   * @param from 起始时间
   * @param to 终止时间
   * @return 操作日志dto列表
   */
  List<BehaviorLog> listByDeviceIdBetween(Integer deviceId, Date from, Date to);

  /**
   * 获取插孔一段时间内的操作日志
   * @param jackId 插孔id
   * @param from 起始时间
   * @param to 终止时间
   * @return 操作日志dto列表
   */
  List<BehaviorLog> listByJackIdBetween(Integer jackId, Date from, Date to);
}
