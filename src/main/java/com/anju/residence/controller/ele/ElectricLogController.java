package com.anju.residence.controller.ele;

import com.anju.residence.annotation.AnonymousAccess;
import com.anju.residence.annotation.OperationLog;
import com.anju.residence.dto.ele.EleLogDTO;
import com.anju.residence.entity.ele.Device;
import com.anju.residence.entity.ele.ElectricLog;
import com.anju.residence.enums.OperationType;
import com.anju.residence.enums.ResultCode;
import com.anju.residence.exception.ApiException;
import com.anju.residence.service.ele.DeviceService;
import com.anju.residence.service.ele.ElectricLogService;
import com.anju.residence.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cygao
 * @date 2020/12/15 13:15
 **/
@Api(tags = "耗电日志API（智能插座）")
@Slf4j
@RestController
@RequestMapping("/ele_log")
public class ElectricLogController {

  private final ElectricLogService eleLogService;
  private final DeviceService deviceService;

  @Autowired
  public ElectricLogController(ElectricLogService eleLogService, DeviceService deviceService) {
    this.eleLogService = eleLogService;
    this.deviceService = deviceService;
  }

  @AnonymousAccess
  @OperationLog(type = OperationType.ADD, description = "添加一条耗电日志")
  @ApiOperation(value = "添加一条耗电日志", tags = "无需权限认证")
  @ApiImplicitParams({@ApiImplicitParam(name = "eleLogDTO", value = "耗电日志传输实体类", dataTypeClass = EleLogDTO.class, paramType = "body", required = true)})
  @PostMapping("/add")
  public ResultVO<String> addElectricLog(@RequestBody @Valid EleLogDTO eleLogDTO) {
    log.info("ElectricLogDTO got {}.", eleLogDTO.toString());
    Device device = deviceService.getById(eleLogDTO.getDeviceId()).orElseThrow(() -> new ApiException(ResultCode.DEVICE_ID_NOT_EXISTS));

    ElectricLog electricLog = eleLogDTO.createElectricLog();
    electricLog.setDevice(device);
    eleLogService.addLog(electricLog);

    return new ResultVO<>("success");
  }

  @ApiOperation(value = "获取设备实时耗电日志")
  @GetMapping("/realtime/{deviceId}")
  public ResultVO<EleLogDTO> getRealtimeElectricLog(@PathVariable Integer deviceId) {
    if (!deviceService.existsById(deviceId)) {
      throw new ApiException(ResultCode.DEVICE_ID_NOT_EXISTS);
    }
    ElectricLog electricLog = eleLogService.getRealTimeLogByDeviceId(deviceId).orElseThrow(() -> new ApiException(ResultCode.NOT_REALTIME_ELECTRIC_LOG_EXISTS));

    return new ResultVO<>(EleLogDTO.build(electricLog));
  }

  @ApiOperation(value = "获取设备最近一次的耗电日志")
  @GetMapping("/latest/{deviceId}")
  public ResultVO<EleLogDTO> getLatestElectricLog(@PathVariable Integer deviceId) {
    if (!deviceService.existsById(deviceId)) {
      throw new ApiException(ResultCode.DEVICE_ID_NOT_EXISTS);
    }

    ElectricLog electricLog = eleLogService.getLatestLogByDeviceId(deviceId).orElseThrow(() -> new ApiException(ResultCode.NOT_ELECTRIC_LOG_EXISTS));

    return new ResultVO<>(EleLogDTO.build(electricLog));
  }

  @ApiOperation(value = "获取设备在数小时内所有的耗电日志", notes = "具体小时数由请求参数决定")
  @ApiImplicitParams({@ApiImplicitParam(name = "deviceId", value = "设备id", paramType = "path", dataTypeClass = Integer.class, required = true),
                      @ApiImplicitParam(name = "hour", value = "查询的小时数", paramType = "query", dataTypeClass = Integer.class, required = true)})
  @GetMapping("/hour/{deviceId}")
  public ResultVO<List<EleLogDTO>> listElectricLogBetweenHour(@PathVariable Integer deviceId, @RequestParam(name = "hour") Integer hour) {
    if (hour == null || hour <= 0) {
      throw new ApiException(ResultCode.ARGUMENT_NEGATIVE_OR_ZERO);
    }
    if (!deviceService.existsById(deviceId)) {
      throw new ApiException(ResultCode.DEVICE_ID_NOT_EXISTS);
    }
    List<ElectricLog> electricLogs = eleLogService.listLogByDeviceIdBetweenHour(deviceId, hour);

    return new ResultVO<>(electricLogs.stream().map(EleLogDTO::build).collect(Collectors.toList()));
  }

  @ApiOperation(value = "获取设备的实时功率")
  @GetMapping("/realtime/power/{userId}")
  public ResultVO<Integer> getRealtimePower(@PathVariable("userId") Integer userId) {
    log.info("Query realtime power by userId: {}.", userId);

    int realtimePower = eleLogService.getRealTimePower(userId);

    log.info("User: {} realtime power is {}.", userId, realtimePower);
    return new ResultVO<>(realtimePower);
  }
}
