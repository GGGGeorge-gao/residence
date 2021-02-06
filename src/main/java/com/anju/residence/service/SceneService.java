package com.anju.residence.service;

import com.anju.residence.dto.ele.SceneDTO;
import com.anju.residence.entity.ele.Scene;

import java.util.List;
import java.util.Optional;

/**
 * @author cygao
 * @date 2020/11/25 20:52
 **/
public interface SceneService {

  /**
   * 保存场景
   * @param scene 场景实体类对象
   */
  void save(Scene scene);

  /**
   * 添加一个场景
   * @param sceneDTO 场景数据传输对象
   */
  void addScene(SceneDTO sceneDTO);

  /**
   * PUT 修改场景
   * @param sceneDTO 场景数据传输对象
   */
  void putScene(SceneDTO sceneDTO, Integer sceneId);

  /**
   * DELETE 删除一个场景
   * 删除过程:
   * 1.将所有插座的场景ID置空
   * 2.删除该场景的所有预警信息
   * 3.在场景表中删除该场景
   * @param sceneId 场景id
   */
  void deleteScene(Integer sceneId);

  /**
   * 更新一个场景的父场景
   * @param sceneId 场景id
   * @param parentId 父场景id
   */
  void updateParentId(Integer sceneId, Integer parentId);

  /**
   * 校验场景是否属于该用户
   * @param sceneId 设备ID
   * @param userId   用户ID
   * @return 是否属于
   */
  boolean matchSceneAndUser(Integer sceneId, Integer userId);

  /**
   * 检验是否存在该场景
   * @param sceneId 场景id
   * @return 是否存在
   */
  boolean existsById(Integer sceneId);

  /**
   * 获得一个场景
   *
   * @param sceneId 场景ID
   * @return 场景实体类对象
   */
  Optional<Scene> getById(Integer sceneId);

  /**
   * 获得用户的所有场景
   *
   * @param userId 用户ID
   * @return 场景列表
   */
  List<Scene> listByUserId(Integer userId);

  /**
   * 获取用户的所有场景dto对象，并按照树的形式返回
   * @param userId 用户id
   * @return 场景dto对象
   */
  List<SceneDTO> listTreeByUserId(Integer userId);

}
