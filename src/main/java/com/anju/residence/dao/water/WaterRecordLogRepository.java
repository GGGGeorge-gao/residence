package com.anju.residence.dao.water;

import com.anju.residence.entity.water.WaterRecordLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author cygao
 * @date 2021/1/25 14:34
 **/
@Repository
public interface WaterRecordLogRepository extends JpaRepository<WaterRecordLog, Integer> {

  /**
   * 查询水表所有的抄表日志
   *
   * @param waterMeterId 水表id
   * @return 抄表日志列表
   */
  @Query(nativeQuery = true, value = "select * from water_record_log as wrl where wrl.water_meter_id = ?1")
  List<WaterRecordLog> findAllByWaterMeterId(Integer waterMeterId);

  /**
   * 查询水表在一段时间范围内的所有抄表日志
   *
   * @param waterMeterId 水表id
   * @param from 起始时间
   * @param to 终止时间
   * @return 抄表日志列表
   */
  @Query(nativeQuery = true, value = "select * from water_record_log as wrl where wrl.water_meter_id = ?1 and wrl.time between ?2 and ?3")
  List<WaterRecordLog> findAllByWaterMeterIdBetween(Integer waterMeterId, Date from, Date to);

  /**
   * 删除某个水表的所有抄表日志
   *
   * @param waterMeterId 水表id
   */
  @Modifying
  @Query(nativeQuery = true, value = "delete from water_record_log as wrl where wrl.water_meter_id = ?1")
  void deleteAllByWaterMeterId(Integer waterMeterId);
}
