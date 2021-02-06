package com.anju.residence.dao.ele;

import com.anju.residence.entity.ele.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author cygao
 * @date 2020/11/28 14:44
 **/
@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

  @Modifying
  @Query(nativeQuery = true, value = "delete from task as t where t.jack_id = ?1")
  void deleteAllByJackId(Integer jackId);

  @Modifying
  @Query(nativeQuery = true, value = "delete from task as t where t.device_id = ?1")
  void deleteAllByDeviceId(Integer deviceId);

}
