package com.anju.residence.service.impl;

import com.anju.residence.dao.water.WaterMeterRepository;
import com.anju.residence.dto.water.WaterMeterDTO;
import com.anju.residence.dto.water.WaterRecordLogDTO;
import com.anju.residence.entity.water.WaterMeter;
import com.anju.residence.enums.ResultCode;
import com.anju.residence.exception.ApiException;
import com.anju.residence.util.JwtTokenUtil;
import com.anju.residence.service.UserService;
import com.anju.residence.service.water.WaterMeterService;
import com.anju.residence.service.water.WaterRecordLogService;
import com.anju.residence.vo.WaterMeterVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * @author cygao
 * @date 2021/1/25 17:58
 **/
@Slf4j
@Service
public class WaterMeterServiceImpl implements WaterMeterService {

    private final WaterMeterRepository waterMeterRepo;

    private final UserService userService;

    private WaterRecordLogService waterRecordLogService;

    @Autowired
    public WaterMeterServiceImpl(WaterMeterRepository waterMeterRepo, UserService userService) {
        this.waterMeterRepo = waterMeterRepo;
        this.userService = userService;
    }

    @Autowired
    public void setWaterRecordLogService(WaterRecordLogService waterRecordLogService) {
        this.waterRecordLogService = waterRecordLogService;
    }

    @Override
    public Optional<WaterMeterVO> getById(Integer waterMeterId) {
        if (!exitsById(waterMeterId)) {
            throw new ApiException(ResultCode.WATER_METER_ERROR, "水表id不存在");
        }
        return waterMeterRepo.getVoById(waterMeterId);
    }

    @Override
    public boolean exitsById(Integer waterMeterId) {
        if (waterMeterId == null) {
            return false;
        }
        return waterMeterRepo.existsById(waterMeterId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public WaterMeter addWaterMeter(WaterMeterDTO waterMeterDTO) {
        if (!JwtTokenUtil.checkUserAuthentication(waterMeterDTO.getUserId())) {
            throw new ApiException(ResultCode.UNAUTHORIZED_REQUEST);
        }
        if (!userService.existsById(waterMeterDTO.getUserId())) {
            throw new ApiException(ResultCode.USER_ERROR, "用户id不存在");
        }
        WaterMeter waterMeter = waterMeterDTO.build();

        return save(waterMeter);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateWaterMeter(WaterMeterDTO waterMeterDTO, Integer waterMeterId) {
        if (!JwtTokenUtil.checkUserAuthentication(waterMeterDTO.getUserId())) {
            throw new ApiException(ResultCode.UNAUTHORIZED_REQUEST);
        }
        if (!userService.existsById(waterMeterDTO.getUserId())) {
            throw new ApiException(ResultCode.USER_ERROR, "用户id不存在");
        }
        if (waterMeterId == null || !waterMeterId.equals(waterMeterDTO.getId()) || exitsById(waterMeterId)) {
            throw new ApiException(ResultCode.WATER_METER_ERROR, "水表id不存在");
        }

        WaterMeter waterMeter = waterMeterDTO.build();

        save(waterMeter);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(Integer waterMeterId) {
        if (!exitsById(waterMeterId)) {
            throw new ApiException(ResultCode.WATER_METER_ERROR, "水表id不存在");
        }
        // OperationLog 记得删除
        waterRecordLogService.deleteByWaterMeterId(waterMeterId);

        waterMeterRepo.deleteById(waterMeterId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public WaterMeter save(WaterMeter waterMeter) {
        return waterMeterRepo.save(waterMeter);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateWaterMeterCount(WaterRecordLogDTO logDTO) {
        log.info(logDTO.toString());
        waterMeterRepo.updateCount(logDTO.getWaterMeterId(), new BigDecimal(logDTO.getCount()), logDTO.getTime());
    }

    @Override
    public List<WaterMeterVO> getAllVoByUserId(int userId) {
        return waterMeterRepo.findAllByUserId(userId);
    }
}
