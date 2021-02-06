package com.anju.residence.service.water;

import com.anju.residence.dto.water.WaterMeterDTO;
import com.anju.residence.entity.water.WaterMeter;

import java.util.List;
import java.util.Optional;

/**
 * @author cygao
 * @date 2021/1/25 14:38
 **/
public interface WaterMeterService {

  /**
   * 通过 水表id 查询
   *
   * @param waterMeterId 水表id
   * @return 水表实体类的Optional对象
   */
  Optional<WaterMeter> getById(Integer waterMeterId);

  /**
   * 通过 用户id 查询
   *
   * @param userId 用户id
   * @return 水表实体类列表
   */
  List<WaterMeter> listByUserId(Integer userId);

  /**
   * 判断水表id是否存在
   *
   * @param waterMeterId 水表id
   * @return 是否存在
   */
  boolean exitsById(Integer waterMeterId);

  /**
   * 添加一个水表
   *
   * @param waterMeterDTO 水表dto
   */
  WaterMeter addWaterMeter(WaterMeterDTO waterMeterDTO);

  /**
   * 修改水表信息
   *
   * @param waterMeterDTO 水表dto
   * @param waterMeterId 水表id
   */
  void updateWaterMeter(WaterMeterDTO waterMeterDTO, Integer waterMeterId);

  /**
   * 删除一个水表
   *
   * @param waterMeterId 水表ID
   */
  void deleteById(Integer waterMeterId);

  /**
   * 保存水表实体类
   *
   * @param waterMeter 水表实体类
   * @return 水表实体类
   */
  WaterMeter save(WaterMeter waterMeter);
}
