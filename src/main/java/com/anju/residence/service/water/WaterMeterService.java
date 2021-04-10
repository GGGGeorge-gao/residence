package com.anju.residence.service.water;

import com.anju.residence.dto.water.WaterMeterDTO;
import com.anju.residence.dto.water.WaterRecordLogDTO;
import com.anju.residence.entity.water.WaterMeter;
import com.anju.residence.vo.WaterMeterVO;

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
     * @return 水表实体VO类的Optional对象
     */
    Optional<WaterMeterVO> getById(Integer waterMeterId);

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
     * @return 包含id值的水表实体类对象
     */
    WaterMeter addWaterMeter(WaterMeterDTO waterMeterDTO);

    /**
     * 修改水表信息
     *
     * @param waterMeterDTO 水表dto
     * @param waterMeterId  水表id
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

    /**
     * 更新水表读数
     *
     * @param logDTO 抄表日志传输实体类对象
     */
    void updateWaterMeterCount(WaterRecordLogDTO logDTO);

    /**
     * 获取应该用户所有的水表信息
     *
     * @param userId 用户id
     * @return 水表vo列表
     */
    List<WaterMeterVO> getAllVoByUserId(int userId);
}
