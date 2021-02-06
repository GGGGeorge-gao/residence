package com.anju.residence.dao.ele;

import com.anju.residence.entity.ele.Scene;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author cygao
 * @date 2020/11/23 10:43
 **/
@Repository
public interface SceneRepository extends JpaRepository<Scene, Integer> {

  /**
   * 查询场景名是否已存在
   * @param userId 用户id
   * @param name 场景名
   * @return 场景ID的Optional对象
   */
  @Query(nativeQuery = true, value = "select id from scene as s where s.user_id = ?1 and s.name = ?2 limit 1")
  Optional<Integer> findIdByUserIdAndName(Integer userId, String name);

  /**
   * 查询场景id和用户id是否匹配
   *
   * @param sceneId 场景id
   * @param userId   用户id
   * @return 该场景id的Optional对象
   */
  @Query(nativeQuery = true, value = "select id from scene as s where s.id = ?1 and s.user_id = ?2")
  Optional<Integer> findByIdAndUser(Integer sceneId, Integer userId);

  /**
   * 查询某个用户的所有场景
   * @param userId 用户id
   * @return 场景列表
   */
  @Query(nativeQuery = true, value = "select * from scene as s where s.user_id = ?1")
  List<Scene> findAllByUserId(Integer userId);

  /**
   * 根据用户id和指定场景名删除场景
   * @param userId 用户id
   * @param name 场景名
   */
  @Modifying
  @Query(nativeQuery = true, value = "delete from scene as s where s.user_id = ?1 and s.name = ?2")
  void deleteSceneByUserIdAndName(Integer userId, String name);

  /**
   * 更新一个场景的父id
   * @param id 当前场景的id
   * @param parentId 父场景id
   */
  @Modifying
  @Query(nativeQuery = true, value = "update scene as s set s.parent_id = ?2 where s.id = ?1")
  void updateParentId(Integer id, Integer parentId);
}
