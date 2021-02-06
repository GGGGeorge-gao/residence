package com.anju.residence.service.ele;

import com.anju.residence.dto.ele.RecLogDTO;
import com.anju.residence.entity.ele.ElectricLog;
import com.anju.residence.entity.ele.ReceptacleLog;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author cygao
 * @date 2020/12/1 20:07
 **/
public interface ReceptacleLogService {

  /**
   * 通过接收到的耗电日志更新插座的用电日志
   *
   * @param log 耗电日志
   */
  void updateByElectricLog(ElectricLog log);

  /**
   * 获取用户一段时间内所有插座的用电日志
   *
   * @param userId 用户id
   * @param from   开始时间
   * @param to     结束时间
   * @return 该时间段内属于该用户所有的插座用电日志
   */
  List<ReceptacleLog> listByUserIdBetween(Integer userId, Date from, Date to);

  /**
   * 获取用户当天所有插座的用电日志
   *
   * @param userId 用户id
   * @return 所有插座当天的用电日志列表
   */
  List<ReceptacleLog> listTodayLogByUserId(Integer userId);

  /**
   * 获取用户在某一天所有插座的用电日志
   *
   * @param userId 用户id
   * @param date   日期
   * @return 所有插座在该天的用电日志列表
   */
  List<ReceptacleLog> listLogByUserIdAndDate(Integer userId, Date date);

  /**
   * 获取该插座的所有用电日志
   *
   * @param receptacleId 插座id
   * @return 用电日志列表
   */
  List<ReceptacleLog> listByReceptacleId(Integer receptacleId);

  /**
   * 获取插座一段时间内的用电日志
   *
   * @param receptacleId 用户id
   * @param from         开始时间
   * @param to           结束时间
   * @return 该时间段内属于该插座所有的用电日志
   */
  List<ReceptacleLog> listByReceptacleIdBetween(Integer receptacleId, Date from, Date to);

  /**
   * 查询插座某一天的用电日志
   *
   * @param receptacleId 插座实体类对象
   * @param date         日期
   * @return 插座在该天的用电日志的Optional对象
   */
  Optional<ReceptacleLog> getByReceptacleIdAndDate(Integer receptacleId, Date date);

  /**
   * 获取用户今日总能耗
   *
   * @param userId 用户id
   * @return 今日总能耗(单位 : 焦耳)
   */
  int getTodayTotalConsumption(Integer userId);

  /**
   * 删除该插座的所有日志
   *
   * @param receptacleId 插座id
   */
  void deleteByReceptacleId(Integer receptacleId);

  /**
   * 查询该日志id是否存在
   *
   * @param logId 日志id
   * @return 是否存在
   */
  boolean existsById(Integer logId);


}
