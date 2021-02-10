package com.anju.residence.controller.water;

import com.anju.residence.annotation.AnonymousAccess;
import com.anju.residence.dto.water.WaterMeterDTO;
import com.anju.residence.entity.water.WaterMeter;
import com.anju.residence.enums.ResultCode;
import com.anju.residence.exception.ApiException;
import com.anju.residence.service.water.WaterMeterService;
import com.anju.residence.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author cygao
 * @date 2021/1/26 1:51 下午
 **/
@Api(tags = "水表API（智能水表）")
@Slf4j
@RestController
@RequestMapping("/api/v1/water/meter")
public class WaterMeterController {

  private final WaterMeterService waterMeterService;

  @Autowired
  public WaterMeterController(WaterMeterService waterMeterService) {
    this.waterMeterService = waterMeterService;
  }

  @ApiOperation(value = "添加一个水表")
  @PostMapping("/add")
  public ResultVO<Integer> addWaterMeter(@RequestBody @Valid WaterMeterDTO waterMeterDTO) {
    log.info("Got water meter dto: {}", waterMeterDTO.toString());
    WaterMeter waterMeter = waterMeterService.addWaterMeter(waterMeterDTO);

    return new ResultVO<>(waterMeter.getId());
  }

  @ApiOperation(value = "查询水表的详细信息", notes = "url路径参数为该水表的id")
  @GetMapping("/{waterMeterId}")
  public ResultVO<WaterMeter> getWaterMeter(@PathVariable Integer waterMeterId) {
    if (waterMeterId == null || waterMeterId <= 0) {
      throw new ApiException(ResultCode.WATER_METER_ID_NOT_EXISTS);
    }
    return new ResultVO<>(waterMeterService.getById(waterMeterId).orElseThrow(() -> new ApiException(ResultCode.WATER_METER_ID_NOT_EXISTS)));
  }

  @ApiOperation(value = "删除一个水表", notes = "url路径参数为该水表的id")
  @DeleteMapping("/{waterMeterId}")
  public ResultVO<String> deleteWaterMeter(@PathVariable Integer waterMeterId) {
    waterMeterService.deleteById(waterMeterId);

    return new ResultVO<>("success");
  }

  @ApiOperation(value = "修改水表信息", notes = "url路径参数为该水表的id")
  @PutMapping("/{waterMeterId}")
  public ResultVO<String> putWaterMeter(@PathVariable Integer waterMeterId, @RequestBody @Valid WaterMeterDTO waterMeterDTO) {
    waterMeterService.updateWaterMeter(waterMeterDTO, waterMeterId);

    return new ResultVO<>("success");
  }
}
