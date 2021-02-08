package com.anju.residence.dao.water;

import com.anju.residence.entity.water.WaterMeter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 水表实体类DAO
 *
 * @author cygao
 * @date 2021/1/25 14:32
 **/
@Repository
public interface WaterMeterRepository extends JpaRepository<WaterMeter, Integer> {

  /**
   * 查询一个用户所有的水表
   *
   * @param userId 用户id
   * @return 水表实体列表
   */
  @Query(nativeQuery = true, value = "select * from water_meter as wm where wm.user_id = ?1")
  List<WaterMeter> findAllByUserId(Integer userId);

  /**
   * 更新水表读数
   *
   * @param waterMeterId 水表id
   * @param currentCount 水表读数
   * @param time 抄表时间
   */
  @Modifying
  @Query(nativeQuery = true, value = "update water_meter as wm " +
          "set wm.current_count = (wm.current_count + ?2), " +
          "wm.last_record_time = ?3 where wm.id = ?1")
  void updateCount(Integer waterMeterId, BigDecimal currentCount, Date time);
}
