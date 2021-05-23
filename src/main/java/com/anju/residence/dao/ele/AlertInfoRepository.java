package com.anju.residence.dao.ele;

import com.anju.residence.entity.ele.AlertInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author cygao
 * @date 2020/11/28 14:35
 **/
@Repository
public interface AlertInfoRepository extends JpaRepository<AlertInfo, Integer> {

  @Modifying
  @Query(nativeQuery = true, value = "delete from alert_info as ai where ai.jack_id = ?1")
  void deleteAllByJackId(Integer jackId);

  @Modifying
  @Query(nativeQuery = true, value = "delete from alert_info as ai where ai.scene_id = ?1")
  void deleteAllBySceneId(Integer sceneId);

  @Modifying
  @Query(nativeQuery = true, value = "delete from alert_info as ai where ai.device_id = ?1")
  void deleteAllByDeviceId(int deviceId);

  @Query(nativeQuery = true, value = "select * from alert_info as ai where ai.device_id = ?1")
  List<AlertInfo> getAllByDeviceId(int deviceId);

}
