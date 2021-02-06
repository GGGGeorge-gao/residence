package com.anju.residence.dao.ele;

import com.anju.residence.entity.ele.Jack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author cygao
 * @date 2020/11/23 10:46
 **/
@Repository
public interface JackRepository extends JpaRepository<Jack, Integer> {

  /**
   * 查询某个插座的所有插孔实例
   *
   * @param receptacleId 插座实体类对象
   * @return 插孔实体列表
   */
  @Query(nativeQuery = true, value = "select * from jack as j where j.receptacle_id = ?1")
  List<Jack> findAllByReceptacleId(Integer receptacleId);

  /**
   * 查询用户的所有插孔
   *
   * @param userId 用户ID
   * @return 插孔实体列表
   */
  @Query(nativeQuery = true, value = "select * from jack as j where j.receptacle_id in (select distinct r.id from receptacle as r where r.user_id = ?1)")
  List<Jack> findAllByUserId(Integer userId);

  /**
   * 查询插座的所有插孔id
   * @param receptacleId 插座id
   * @return id列表
   */
  @Query(nativeQuery = true, value = "select id from jack as j where j.receptacle_id = ?1")
  List<Integer> findAllIdByReceptacleId(Integer receptacleId);

  /**
   * 查询插座有多少插孔
   *
   * @param receptacleId 插座ID
   * @return 插孔数量
   */
  @Query(nativeQuery = true, value = "select count(*) from jack as j where j.receptacle_id = ?1")
  Integer countJacksByReceptacle(Integer receptacleId);

  /**
   * 查询该插孔的插座id
   *
   * @param jackId 插孔id
   * @return 插座id的Optional对象
   */
  @Query(nativeQuery = true, value = "select j.receptacle_id from jack as j where j.id = ?1")
  Optional<Integer> findReceptacleIdByJackId(Integer jackId);

  /**
   * 删除插座的所有插孔
   *
   * @param receptacleId 插座ID
   */
  @Modifying
  @Query(nativeQuery = true, value = "delete from jack as j where j.receptacle_id = ?1")
  void deleteAllByReceptacleId(Integer receptacleId);

  /**
   * 更新插孔状态
   * @param jackId 插孔id
   * @param status 插孔状态
   */
  @Modifying
  @Query(nativeQuery = true, value = "update jack as j set j.status = ?2 where j.id = ?1")
  void updateStatus(Integer jackId, Integer status);
}
