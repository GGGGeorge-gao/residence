package com.anju.residence.service.ele;

import com.anju.residence.dto.ele.ReceptacleDTO;
import com.anju.residence.entity.ele.Receptacle;
import com.anju.residence.entity.ele.Scene;
import com.anju.residence.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * @author cygao
 * @date 2020/11/25 20:50
 **/
public interface ReceptacleService {

  /**
   * 查询该用户所有的插座
   *
   * @param user 用户实体类对象
   * @return 插座列表
   */
  List<Receptacle> getAllByUser(User user);

  /**
   * 查询该用户某个场景的所有插座
   *
   * @param scene 场景实体类对象
   * @return 插座列表
   */
  List<Receptacle> getAllByScene(Scene scene);

  /**
   * 查询该用户处于特定状态的所有插座
   *
   * @param user   用户实体类对象
   * @param status 状态
   * @return 插座列表
   */
  List<Receptacle> getAllByUserAndStatus(User user, int status);

  /**
   * 查询该用户在某个场景中处于特定状态的所有插座
   *
   * @param scene  场景
   * @param status 状态
   * @return 插座列表
   */
  List<Receptacle> getAllBySceneAndStatus(Scene scene, int status);

  /**
   * 通过插座id查询
   *
   * @param receptacleId 插座id
   * @return 插座的Optional对象
   */
  Optional<Receptacle> getById(int receptacleId);

  /**
   * 通过插孔id查询
   *
   * @param jackId 插孔id
   * @return 插座的Optional对象
   */
  Optional<Receptacle> getByJackId(int jackId);

  /**
   * 判断插座是否属于某个用户
   *
   * @param receptacleId 插座id
   * @param userId       用户id
   * @return 是否属于
   */
  boolean matchReceptacleAndUser(Integer receptacleId, Integer userId);

  /**
   * 判断是否存在该插座id
   * @param receptacleId 插座id
   * @return 是否存在
   */
  boolean existsById(Integer receptacleId);

  /**
   * 添加一个插座
   * @param receptacleDTO 插座dto
   */
  void addReceptacle(ReceptacleDTO receptacleDTO);

  /**
   * PUT 修改插座信息
   *
   * @param receptacleDTO 插座dto对象
   * @param receptacleId 插座id
   */
  void putReceptacle(ReceptacleDTO receptacleDTO, Integer receptacleId);

  /**
   * 删除一个插座
   * @param receptacleId 插座id
   */
  void deleteReceptacle(Integer receptacleId);

  /**
   * 保存插座信息
   *
   * @param receptacle 插座实体类对象
   */
  void save(Receptacle receptacle);

  /**
   * 删除场景，将该场景所有插座的场景ID设为null
   *
   * @param sceneId 场景ID
   * @return 是否清除成功
   */
  boolean clearScene(int sceneId);
}
