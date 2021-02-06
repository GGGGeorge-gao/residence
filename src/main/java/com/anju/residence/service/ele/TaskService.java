package com.anju.residence.service.ele;

/**
 * @author cygao
 * @date 2020/12/8 13:03
 **/
public interface TaskService {

  /**
   * 删除该插孔的所有任务
   *
   * @param jackId 插孔id
   * @return 是否删除成功
   */
  boolean deleteByJackId(int jackId);

  /**
   * 删除该设备的所有任务
   *
   * @param deviceId 设备id
   * @return 是否删除成功
   */
  boolean deleteByDeviceId(int deviceId);
}
