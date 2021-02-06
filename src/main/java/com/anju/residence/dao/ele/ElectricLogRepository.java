package com.anju.residence.dao.ele;

import com.anju.residence.entity.ele.ElectricLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author cygao
 * @date 2020/11/28 14:42
 **/
@Repository
public interface ElectricLogRepository extends JpaRepository<ElectricLog, Long> {

  /**
   * 查找设备最新的耗电日志
   *
   * @param deviceId 设备设备ID
   */
  @Query(nativeQuery = true, value = "select * from electric_log e where e.device_id = ?1 order by e.time desc limit 1")
  Optional<ElectricLog> findLatestLogByDeviceId(Integer deviceId);

  /**
   * 根据设备id查找最新的耗电日志
   */
  @Query(nativeQuery = true, value = "select * from electric_log e where e.device_id = ?1 and e.time between ?2 and ?3 order by e.time desc limit 1")
  ElectricLog findLatestLogByDeviceAndTimeBetween(Integer deviceId, Date from, Date to);

  /**
   * 查找设备一段时间内的耗电日志
   */
  @Query(nativeQuery = true, value = "select * from electric_log e where e.device_id = ?1 and e.time between ?2 and ?3")
  List<ElectricLog> findAllByDeviceAndTimeBetween(Integer deviceId, Date from, Date to);

  /**
   * 根据耗电日志id查找当前设备id
   */
  @Query(value = "select device_id from electric_log e where e.id = ?1", nativeQuery = true)
  Integer findDeviceIdByElectricLogId(Long electricLogId);

  /**
   * 根据设备ID查询该一段时间内最新的耗电日志
   */
  @Query(nativeQuery = true, value = "select el.power from electric_log as el where el.device_id = ?1 and el.time between ?2 and ?3 limit 1")
  Optional<Integer> findLatestPowerByDeviceId(Integer deviceId, Date from, Date to);

  /**
   * 删除指定设备的所有耗电日志
   */
  @Modifying
  @Query(nativeQuery = true, value = "delete from electric_log as el where el.device_id = ?1")
  void deleteAllByDeviceId(Integer deviceId);
}
