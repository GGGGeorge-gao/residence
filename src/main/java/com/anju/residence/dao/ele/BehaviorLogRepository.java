package com.anju.residence.dao.ele;

import com.anju.residence.entity.ele.BehaviorLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author cygao
 * @date 2020/11/28 14:35
 **/
@Repository
public interface BehaviorLogRepository extends JpaRepository<BehaviorLog, Integer> {

  /**
   * 删除一个设备所有的操作日志
   * @param deviceId 设备id
   */
  @Modifying
  @Query(nativeQuery = true, value = "delete from behavior_log as bl where bl.device_id = ?1")
  void deleteAllByDeviceId(Integer deviceId);

  /**
   * 删除一个插孔所有的操作日志
   * @param jackId 插孔id
   */
  @Modifying
  @Query(nativeQuery = true, value = "delete from behavior_log as bl where bl.jack_id = ?1")
  void deleteAllByJackId(Integer jackId);

  /**
   * 获取一个设备一段时间内的所有操作日志
   * @param deviceId 设备id
   * @param from 起始时间
   * @param to 终止时间
   * @return 操作日志列表
   */
  @Query(nativeQuery = true, value = "select * from behavior_log as bl where bl.device_id = ?1 and bl.time between ?2 and ?3")
  List<BehaviorLog> findAllByDeviceIdBetween(Integer deviceId, Date from, Date to);

  /**
   * 获取一个插孔一段时间内所有的操作日志
   * @param jackId 插孔id
   * @param from 起始时间
   * @param to 终止时间
   * @return 操作日志列表
   */
  @Query(nativeQuery = true, value = "select * from behavior_log as bl where bl.jack_id = ?1 and bl.time between ?2 and ?3")
  List<BehaviorLog> findAllByJackIdBetween(Integer jackId, Date from, Date to);
}
