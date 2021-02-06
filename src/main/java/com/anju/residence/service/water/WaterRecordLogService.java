package com.anju.residence.service.water;

import com.anju.residence.dto.water.WaterRecordLogDTO;
import com.anju.residence.entity.water.WaterRecordLog;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author cygao
 * @date 2021/1/25 14:38
 **/
public interface WaterRecordLogService {

  /**
   * 通过id查询日志
   *
   * @param logId 日志
   * @return 抄表实体类的Optional对象
   */
  Optional<WaterRecordLog> getById(Integer logId);

  /**
   * 获取水表的所有抄表记录
   *
   * @param waterMeterId 水表id
   * @return 抄表实体类列表
   */
  List<WaterRecordLog> listByWaterMeterId(Integer waterMeterId);

  /**
   * 获取水表在某一天的抄表日志
   *
   * @param waterMeterId 水表id
   * @param date 日期
   * @return 抄表实体类列表
   */
  List<WaterRecordLog> listByWaterMeterIdAt(Integer waterMeterId, Date date);

  /**
   * 获取水表在一段时间范围内的所有抄表日志
   * @param waterMeterId 水表id
   * @param from 起始时间
   * @param to 终止时间
   * @return 抄表实体类列表
   */
  List<WaterRecordLog> listByWaterMeterIdBetween(Integer waterMeterId, Date from, Date to);

  /**
   * 添加一条抄表日志
   *
   * @param logDTO 抄表日志dto
   */
  void addWaterRecordLog(WaterRecordLogDTO logDTO);

  /**
   * 删除某条抄表日志
   *
   * @param logId 日志id
   */
  void deleteById(Integer logId);

  /**
   * 删除某个水表的所有抄表日志
   *
   * @param waterMeterId 水表id
   */
  void deleteByWaterMeterId(Integer waterMeterId);

  /**
   * 判断该日志id是否存在
   *
   * @param logId 日志id
   * @return 是否存在
   */
  boolean exitsById(Integer logId);

  /**
   * 保存抄表日志实体类对象
   *
   * @param waterRecordLog 抄表日志实体类对象
   */
  void save(WaterRecordLog waterRecordLog);

}
