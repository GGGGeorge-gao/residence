package com.anju.residence.service.ele;

/**
 * @author cygao
 * @date 2020/12/8 11:14
 **/
public interface AlertInfoService {

  /**
   * 删除一个场景，清理该场景的所有预警信息
   *
   * @param sceneId 场景ID
   * @return 是否删除成功
   */
  boolean deleteBySceneId(int sceneId);

  /**
   * 删除一个插孔，清理该插孔的所有预警信息
   *
   * @param jackId 插孔ID
   * @return 是否删除成功
   */
  boolean deleteByJackId(int jackId);

  /**
   * 删除一个设备，清理该设备的所有预警信息
   *
   * @param deviceId 设备ID
   * @return 是否删除成功
   */
  boolean deleteByDeviceId(int deviceId);
}
