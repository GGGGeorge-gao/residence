package com.anju.residence.dao.water;

import com.anju.residence.entity.water.WaterMeter;
import com.anju.residence.vo.WaterMeterVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
   * @return 水表vo列表
   */
  @Query(value = "select new com.anju.residence.vo.WaterMeterVO(wm.id, wm.name, wm.user.id, wm.status, wm.currentCount, wm.lastRecordTime, wm.collectIntervalMin, wm.updateTime, wm.createTime, wm.description) " +
          "from WaterMeter as wm where wm.user.id = ?1")
  List<WaterMeterVO> findAllByUserId(Integer userId);

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

  /**
   * 查询水表详细信息
   *
   * @param waterMeterId 水表id
   * @return vo对象
   */
  @Query(value = "select new com.anju.residence.vo.WaterMeterVO(wm.id, wm.name, wm.user.id, wm.status, wm.currentCount, wm.lastRecordTime, wm.collectIntervalMin, wm.updateTime, wm.createTime, wm.description) " +
          "from WaterMeter as wm where wm.id = ?1")
  Optional<WaterMeterVO> getVoById(Integer waterMeterId);
}
