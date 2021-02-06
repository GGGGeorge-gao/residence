package com.anju.residence.controller.ele;

import com.anju.residence.dto.ele.DeviceLogDTO;
import com.anju.residence.enums.ResultCode;
import com.anju.residence.exception.ApiException;
import com.anju.residence.service.ele.DeviceLogService;
import com.anju.residence.service.UserService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cygao
 * @date 2020/12/19 13:49
 **/
@Api(tags = "设备用电日志API（智能插座）")
@Slf4j
@RestController
@RequestMapping("/device_log")
public class DeviceLogController {

  private final DeviceLogService deviceLogService;
  private final UserService userService;

  @Autowired
  public DeviceLogController(DeviceLogService deviceLogService, UserService userService) {
    this.deviceLogService = deviceLogService;
    this.userService = userService;
  }

  @ApiOperation(value = "获取设备在一个段日期内的用电日志", notes = "设备用电日志记录的单位跨度为天，用电日志会在添加耗电日志时自动生成")
  @ApiImplicitParams({@ApiImplicitParam(name = "from", value = "起始时间，格式为yyyy-MM-dd", paramType = "query", dataTypeClass = Date.class, required = true),
          @ApiImplicitParam(name = "to", value = "终止时间，格式为yyyy-MM-dd", paramType = "query", dataTypeClass = Date.class, required = true),
          @ApiImplicitParam(name = "deviceId", value = "设备id", paramType = "path", dataTypeClass = Integer.class, required = true)})
  @GetMapping("/between/{deviceId}")
  public ResultVO<List<DeviceLogDTO>> getLogBetween(@PathVariable Integer deviceId,
                                                    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
                                                    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date to) {
    if (from == null || to == null || from.after(to)) {
      throw new ApiException(ResultCode.INVALID_DATE);
    }

    return new ResultVO<>(deviceLogService.listLogByDeviceIdBetween(deviceId, from, to).stream().map(DeviceLogDTO::build).collect(Collectors.toList()));
  }

  @ApiOperation(value = "删除一条日志")
  @DeleteMapping("/{logId}")
  public ResultVO<String> deleteByLogId(@PathVariable Integer logId) {
    deviceLogService.deleteByDeviceId(logId);

    return new ResultVO<>("success");
  }


}
