package com.anju.residence.dao.ele;

import com.anju.residence.entity.ele.Receptacle;
import com.anju.residence.entity.ele.Scene;
import com.anju.residence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author cygao
 * @date 2020/11/23 10:44
 **/
@Repository
public interface ReceptacleRepository extends JpaRepository<Receptacle, Integer> {

  /**
   * 根据用户和插座名查询插座
   *
   * @param user 用户实体类对象
   * @param name 插座名
   * @return 插座实体类对象
   */
  Receptacle findFirstByUserAndName(User user, String name);

  /**
   * 根据插孔id查询插座
   *
   * @param jackId 插孔id
   * @return 插座的Optional对象
   */
  @Query(nativeQuery = true, value = "select * from receptacle as r, jack as j where r.id = (select j.receptacle_id where j.id = ?1 limit 1)")
  Optional<Receptacle> findByJackId(Integer jackId);

  /**
   * 查询用户的所有插座
   *
   * @param user 用户实体类对象
   * @return 插座列表
   */
  List<Receptacle> findAllByUser(User user);

  /**
   * 查询场景的所有插座
   *
   * @param scene 场景实体类对象
   * @return 插座列表
   */
  List<Receptacle> findAllByScene(Scene scene);

  /**
   * 查询某个用户处于特定状态的所有插座
   *
   * @param user   用户实体类对象
   * @param status 状态变量
   * @return 插座列表
   */
  List<Receptacle> findAllByUserAndStatus(User user, Integer status);

  /**
   * 查询某个场景处于特定状态的所有插座
   *
   * @param scene  场景实体类对象
   * @param status 状态变量
   * @return 插座列表
   */
  List<Receptacle> findAllBySceneAndStatus(Scene scene, Integer status);

  /**
   * 根据用户id和插座名查询该插座是否存在
   *
   * @param userId 用户id
   * @param name   插座名
   * @return 插座id的Optional对象
   */
  @Query(nativeQuery = true, value = "select r.id from receptacle as r where r.user_id = ?1 and r.name = ?2")
  Optional<Integer> findIdByUserIdAndName(Integer userId, String name);

  /**
   * 查询某个插座的用户id
   *
   * @param receptacleId 插座id
   * @return 用户id的Optional对象
   */
  @Query(nativeQuery = true, value = "select r.user_id from receptacle as r where r.id = ?1")
  Optional<Integer> findUserIdByReceptacleId(Integer receptacleId);

  /**
   * 删除某个场景，将插座的场景字段置空
   *
   * @param sceneId 场景id
   */
  @Modifying
  @Query(nativeQuery = true, value = "update receptacle as r set r.scene_id = null where r.scene_id = ?1")
  void clearScene(Integer sceneId);

}
