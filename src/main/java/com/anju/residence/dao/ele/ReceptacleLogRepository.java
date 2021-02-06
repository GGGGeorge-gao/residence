package com.anju.residence.dao.ele;

import com.anju.residence.entity.ele.ReceptacleLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author cygao
 * @date 2020/11/28 14:43
 **/
@Repository
public interface ReceptacleLogRepository extends JpaRepository<ReceptacleLog, Integer> {

  /**
   * 查询插座用电日志
   *
   * @param receptacleId 插座
   * @return 插座用电日志列表
   */
  @Query(nativeQuery = true, value = "select * from receptacle_log as rl where rl.receptacle_id = ?1")
  List<ReceptacleLog> findAllByReceptacleId(Integer receptacleId);

  /**
   * 查询插座一段时间里的用电日志
   *
   * @param receptacleId 插座id
   * @param from         起始时间
   * @param to           终止时间
   * @return 插座用电日志列表
   */
  @Query(nativeQuery = true, value = "select * from receptacle_log as rl where rl.receptacle_id = ?1 and rl.time between ?2 and ?3")
  List<ReceptacleLog> findAllByReceptacleIdBetween(Integer receptacleId, Date from, Date to);

  /**
   * 查找该插座某一天的用电日志
   *
   * @param receptacleId 插座id
   * @param from         起使时间，from应当为当天的0点
   * @param to           终止时间，to应为当天最后一秒
   * @return 插座用电日志的Optional对象
   */
  @Query(nativeQuery = true, value = "select * from receptacle_log as rl where rl.receptacle_id = ?1 and rl.time between ?2 and ?3 limit 1")
  Optional<ReceptacleLog> findReceptacleLogByTime(Integer receptacleId, Date from, Date to);

  /**
   * 查询该用户在某一天所有的用电日志
   * from应当为当天的0点，to应为当天最后一秒
   *
   * @param userId 用户ID
   * @param from   起使时间
   * @param to     终止时间
   * @return 设备用电日志列表
   */
  @Query(nativeQuery = true,
          value = "select * from receptacle_log as rl where " +
                  "rl.receptacle_id in (select r.id from receptacle as r where r.user_id = ?1) " +
                  "and " +
                  "rl.time between ?2 and ?3")
  List<ReceptacleLog> findAllByUserAndTimeBetween(Integer userId, Date from, Date to);

  /**
   * 删除插座的所有用电日志
   *
   * @param receptacleId 插座id
   */
  @Modifying
  @Query(nativeQuery = true, value = "delete from receptacle_log as rl where rl.receptacle_id = ?1")
  void deleteAllByReceptacleId(Integer receptacleId);

}
