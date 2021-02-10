package com.anju.residence.controller.water;

import com.anju.residence.annotation.AnonymousAccess;
import com.anju.residence.dto.water.WaterRecordLogDTO;
import com.anju.residence.enums.ResultCode;
import com.anju.residence.exception.ApiException;
import com.anju.residence.service.water.WaterRecordLogService;
import com.anju.residence.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cygao
 * @date 2021/1/26 2:06 下午
 **/
@Api(tags = "水表抄表日志API（智能水表）")
@Slf4j
@RestController
@RequestMapping("/api/v1/water/log")
public class WaterRecordLogController {

  private final WaterRecordLogService waterRecordLogService;

  @Autowired
  public WaterRecordLogController(WaterRecordLogService waterRecordLogService) {
    this.waterRecordLogService = waterRecordLogService;
  }

  @ApiOperation(value = "查询某一水表某一天所有的抄表日志", notes = "url路径参数为水表id，请求头数 date 格式为yyyy-MM-dd")
  @ApiImplicitParams({@ApiImplicitParam(name = "waterMeterId", value = "水表id", paramType = "path", dataTypeClass = Integer.class, required = true),
                      @ApiImplicitParam(name = "date", value = "查询日期，格式为 yyyy-MM-dd", paramType = "query", dataTypeClass = Date.class, required = true)})
  @GetMapping("/date/{waterMeterId}")
  public ResultVO<List<WaterRecordLogDTO>> getLogAt(@PathVariable Integer waterMeterId,
                                                 @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
    return new ResultVO<>(waterRecordLogService.listByWaterMeterIdAt(waterMeterId, date)
                          .stream().map(WaterRecordLogDTO::build).collect(Collectors.toList()));
  }

  @ApiOperation(value = "查询某一水表在一段时间范围内的所有日志", notes = "url路径参数为水表id，请求头参数 from、to 格式均为yyyy-MM-dd")
  @ApiImplicitParams({@ApiImplicitParam(name = "from", value = "起始时间，格式为yyyy-MM-dd", paramType = "query", dataTypeClass = Date.class, required = true),
                      @ApiImplicitParam(name = "to", value = "终止时间，格式为yyyy-MM-dd", paramType = "query", dataTypeClass = Date.class, required = true),
                      @ApiImplicitParam(name = "waterMeterId", value = "水表id", paramType = "path", dataTypeClass = Integer.class, required = true)})
  @GetMapping("/date/between/{waterMeterId}")
  public ResultVO<List<WaterRecordLogDTO>> getLogBetween(@PathVariable Integer waterMeterId,
                                                      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
                                                      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date to) {
    return new ResultVO<>(waterRecordLogService.listByWaterMeterIdBetween(waterMeterId, from, to)
          .stream().map(WaterRecordLogDTO::build).collect(Collectors.toList()));
  }

  @AnonymousAccess
  @ApiOperation(value = "添加一条抄表日志")
  @ApiImplicitParam(name = "waterRecordLogDTO", value = "抄表日志传输实体类", dataTypeClass = WaterRecordLogDTO.class, paramType = "body", required = true)
  @PostMapping("/add")
  public ResultVO<String> addWaterRecordLog(@RequestBody @Valid WaterRecordLogDTO waterRecordLogDTO) {
    waterRecordLogService.addWaterRecordLog(waterRecordLogDTO);

    return new ResultVO<>("success");
  }

  @ApiOperation(value = "获取某一条抄表日志详情", notes = "url路径参数为抄表日志id")
  @ApiImplicitParam(name = "recordLogId", value = "日志id", paramType = "path", dataTypeClass = Integer.class, required = true)
  @GetMapping("/{recordLogId}")
  public ResultVO<WaterRecordLogDTO> getWaterRecordLog(@PathVariable Integer recordLogId) {
    return new ResultVO<>(WaterRecordLogDTO.build(waterRecordLogService.getById(recordLogId)
            .orElseThrow(() -> new ApiException(ResultCode.WATER_METER_ID_NOT_EXISTS))));
  }

  @ApiOperation(value = "删除某一条抄表日志", notes = "url路径参数为抄表日志id")
  @ApiImplicitParam(name = "recordLogId", value = "日志id", paramType = "path", dataTypeClass = Integer.class, required = true)
  @DeleteMapping("/{recordLogId}")
  public ResultVO<String> deleteWaterRecordLog(@PathVariable Integer recordLogId) {
    waterRecordLogService.deleteById(recordLogId);
    return new ResultVO<>("success");
  }
}
