package com.anju.residence.service.ele;

import com.anju.residence.dto.ele.JackDTO;
import com.anju.residence.entity.ele.Jack;

import java.util.List;
import java.util.Optional;

/**
 * @author cygao
 * @date 2020/11/25 20:48
 **/
public interface JackService {

  /**
   * 保存Jack实体类对象
   */
  void save(Jack jack);

  /**
   * 批量添加插孔
   * @param jacks 插孔dto列表
   * @param receptacleId 插座id
   */
  void addJacks(List<JackDTO> jacks, Integer receptacleId);

  /**
   * 删除Jack实体类对象
   * @param jackId 插孔id
   */
  void deleteJack(Integer jackId);

  /**
   * 判断某个插孔id是否存在
   *
   * @param jackId 插孔id
   * @return 是否存在
   */
  boolean existsByJackId(int jackId);

  /**
   * 判断插孔是否属于某个用户
   *
   * @param jackId 插孔id
   * @param userId 用户id
   * @return 是否属于
   */
  boolean existsJackIdByUserId(Integer jackId, Integer userId);

  /**
   * 获取插孔实例
   *
   * @param jackId 插孔id
   * @return 插孔实体类对象
   */
  Optional<Jack> getById(Integer jackId);

  /**
   * 获取某个用户的所有插孔
   *
   * @param userId 用户ID
   * @return 插孔列表
   */
  List<Jack> listJacksByUserId(int userId);

  /**
   * 获取插座的所有插孔
   *
   * @param receptacleId 插座id
   * @return 插孔列表
   */
  List<Jack> listJacksByReceptacle(Integer receptacleId);

  /**
   * 获取插座的所有插孔id
   * @param receptacleId 插座id
   * @return id列表
   */
  List<Integer> listIdByReceptacle(Integer receptacleId);

  /**
   * 更新插孔状态
   * @param jackId 插孔id
   * @param status 插孔状态
   */
  void updateStatus(int jackId, int status);
}
