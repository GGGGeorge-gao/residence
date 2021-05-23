package com.anju.residence.service.ele;

import com.anju.residence.dto.ele.DeviceDTO;
import com.anju.residence.entity.ele.Device;
import com.anju.residence.vo.DeviceListItemVO;

import java.util.List;
import java.util.Optional;

/**
 * @author cygao
 * @date 2020/11/25 21:11
 **/
public interface DeviceService {

  /**
   * 查找出用户所有的设备ID
   *
   * @param userId 用户id
   * @return 所有设备ID
   */
  List<Integer> listDeviceIdByUserId(Integer userId);

  /**
   * 查找出用户所有的设备
   *
   * @param userId 用户id
   * @return 设备列表
   */
  List<DeviceListItemVO> listVoByUserId(Integer userId);

  /**
   * 根据设备id查找该设备
   */
  Optional<Device> getById(Integer deviceId);

  /**
   * 删除一个设备
   *
   * @param deviceId 设备id
   */
  void deleteDevice(Integer deviceId);

  /**
   * 删除插孔，将插孔所有设备的插孔id置空
   *
   * @param jackId 插孔id
   */
  void clearJack(int jackId);

  /**
   * 检验是否存在该设备Id
   * @param deviceId 设备id
   * @return 是否存在
   */
  boolean existsById(Integer deviceId);

  /**
   * 校验该用户是否可以修改该设备的信息
   *
   * @param deviceId 设备ID
   * @param userId   用户ID
   * @return 是否属于
   */
  boolean checkPermission(Integer deviceId, Integer userId);

  /**
   * 保存设备实体
   *
   * @param device 设备实体类对象
   */
  void save(Device device);

  /**
   * PUT 修改设备
   * @param deviceDTO 设备dto对象
   * @param deviceId  设备id
   */
  void putDevice(DeviceDTO deviceDTO, Integer deviceId);

  /**
   * POST 添加设备
   *
   * @param deviceDTO 设备dto对象
   */
  void addDevice(DeviceDTO deviceDTO);

  /**
   * 更新设备状态
   * @param deviceId 设备id
   * @param status 设备状态
   */
  void updateStatus(int deviceId, int status);

}
