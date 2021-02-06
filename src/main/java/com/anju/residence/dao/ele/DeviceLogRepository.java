package com.anju.residence.dao.ele;

import com.anju.residence.entity.ele.DeviceLog;
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
public interface DeviceLogRepository extends JpaRepository<DeviceLog, Integer> {

  /**
   * 查找该设备某一天的用电日志，from应当为当天的0点，to应为次日的0点
   *
   * @param deviceId 设备id
   * @param from     起使时间，from应当为当天的0点
   * @param to       终止时间，to应为当天最后一秒
   * @return 设备用电日志的Optional对象
   */
  @Query(nativeQuery = true, value = "select * from device_log as dl where dl.device_id = ?1 and dl.time between ?2 and ?3 limit 1")
  Optional<DeviceLog> findByDeviceIdAndTime(Integer deviceId, Date from, Date to);

  /**
   * 查找该设备一段时间内的用电日志
   *
   * @param deviceId 设备id
   * @param from     起使时间
   * @param to       终止时间
   * @return 用电日志列表
   */
  @Query(nativeQuery = true, value = "select * from device_log as dl where dl.device_id = ?1 and dl.time between ?2 and ?3")
  List<DeviceLog> findAllByDeviceIdAndTime(Integer deviceId, Date from, Date to);

  /**
   * 查询该用户在一段时间内所有的用电日志，from应当为当天的0点，to应为当天最后一秒
   *
   * @param userId 用户id
   * @param from   起始时间
   * @param to     终止时间
   * @return 设备用电日志列表
   */
  @Query(value = "select * from device_log as dl where " +
          "dl.device_id = (select d.id from device as d where d.user_id = ?1) " +
          "and " +
          "dl.time between ?2 and ?3"
          , nativeQuery = true)
  List<DeviceLog> findAllByUserAndTimeBetween(Integer userId, Date from, Date to);

  /**
   * 删除指定设备的所有日志
   *
   * @param deviceId 设备id
   */
  @Modifying
  @Query(nativeQuery = true, value = "delete from device_log as dl where dl.device_id = ?1")
  void deleteAllByDeviceId(Integer deviceId);

}
