package com.anju.residence.dao.ele;

import com.anju.residence.entity.ele.Device;
import com.anju.residence.entity.ele.Jack;
import com.anju.residence.vo.DeviceVO;
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
public interface DeviceRepository extends JpaRepository<Device, Integer> {

  @Query(value = "select new com.anju.residence.vo.DeviceVO(d.id, d.user.id, d.jack.id, d.name, d.type, d.status, d.createTime, d.updateTime) from Device as d where d.id = ?1")
  Optional<DeviceVO> findVoById(Integer deviceId);

  /**
   * 查询该用户所有设备ID
   */
  @Query(nativeQuery = true, value = "select d.id from device as d where d.user_id = ?1")
  List<Integer> findAllDeviceIdByUserId(Integer userId);

  /**
   * 查询该用户所有设备
   *
   * @param userId 用户id
   * @return 设备vo列表
   */
  @Query(value = "select new com.anju.residence.vo.DeviceVO(d.id, d.user.id, d.jack.id, d.name, d.type, d.status, d.createTime, d.updateTime) from Device as d where d.user.id = ?1")
  List<DeviceVO> findAllVoByUserId(Integer userId);

  /**
   * 查询一个插孔是否有设备
   *
   * @param jack 插孔实体类对象
   * @return 是否存在
   */
  boolean existsByJack(Jack jack);

  /**
   * 查询设备id和用户id是否匹配
   *
   * @param deviceId 设备id
   * @param userId   用户id
   * @return 该设备id的Optional对象
   */
  @Query(nativeQuery = true, value = "select d.id from device as d where d.id = ?1 and d.user_id = ?2")
  Optional<Integer> findIdByIdAndUser(Integer deviceId, Integer userId);

  /**
   * 删除某个插孔，将设备上的插孔字段置空
   *
   * @param jackId 插孔id
   */
  @Modifying
  @Query(nativeQuery = true, value = "update device as d set d.jack_id = null where d.jack_id = ?1")
  void clearJack(Integer jackId);

  /**
   * 删除该用户所有的设备
   * @param userId 用户id
   */
  @Modifying
  @Query(nativeQuery = true, value = "delete from device as d where d.user_id = ?1")
  void deleteAllByUserId(Integer userId);

  /**
   * 更新设备状态
   * @param deviceId 设备id
   * @param status 状态
   */
  @Modifying
  @Query(nativeQuery = true, value = "update device as d set d.status = ?2 where d.id = ?1")
  void updateStatus(Integer deviceId, Integer status);
}
