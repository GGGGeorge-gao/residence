package com.anju.residence.controller.ele;

import com.anju.residence.dto.ele.BehaviorLogDTO;
import com.anju.residence.entity.ele.BehaviorLog;
import com.anju.residence.service.ele.BehaviorLogService;
import com.anju.residence.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
 * @date 2020/12/26 13:25
 **/
@Api(tags = "用户操作日志API（智能插座）")
@RestController
@RequestMapping("/behavior")
public class BehaviorController {

  private final BehaviorLogService behaviorLogService;

  @Autowired
  public BehaviorController(BehaviorLogService behaviorLogService) {
    this.behaviorLogService = behaviorLogService;
  }

  @ApiOperation(value = "添加一条操作日志")
  @PostMapping("/add")
  public ResultVO<String> add(@RequestBody @Valid BehaviorLogDTO behaviorLogDTO) {
    behaviorLogService.add(behaviorLogDTO);

    return new ResultVO<>("success");
  }

  @ApiOperation(value = "获取设备一段时间内的操作日志")
  @ApiImplicitParams({@ApiImplicitParam(name = "deviceId", value = "设备id", paramType = "path", dataTypeClass = Integer.class, required = true
  ),
                      @ApiImplicitParam(name = "from", value = "起始时间，格式为yyyy-MM-dd HH:mm:ss", paramType = "query", dataTypeClass = Date.class, required = true),
                      @ApiImplicitParam(name = "to", value = "终止时间，格式为yyyy-MM-dd HH:mm:ss", paramType = "query", dataTypeClass = Date.class, required = true)})
  @GetMapping("/device/{deviceId}")
  public ResultVO<List<BehaviorLogDTO>> listByDeviceBetween(@PathVariable Integer deviceId,
                                        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date from,
                                        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date to) {
    List<BehaviorLog> logs = behaviorLogService.listByDeviceIdBetween(deviceId, from, to);

    return new ResultVO<>(logs.stream().map(BehaviorLogDTO::buildDTO).collect(Collectors.toList()));
  }

  @ApiOperation(value = "获取插座一段时间内的操作日志")
  @ApiImplicitParams({@ApiImplicitParam(name = "jackId", value = "插座id", paramType = "path", dataTypeClass = Integer.class, required = true
  ),
          @ApiImplicitParam(name = "from", value = "起始时间，格式为yyyy-MM-dd HH:mm:ss", paramType = "query", dataTypeClass = Date.class, required = true),
          @ApiImplicitParam(name = "to", value = "终止时间，格式为yyyy-MM-dd HH:mm:ss", paramType = "query", dataTypeClass = Date.class, required = true)})
  @GetMapping("/jack/{jackId}")
  public ResultVO<List<BehaviorLogDTO>> listByJackBetween(@PathVariable Integer jackId,
                                        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date from,
                                        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date to) {
    List<BehaviorLog> logs = behaviorLogService.listByJackIdBetween(jackId, from, to);

    return new ResultVO<>(logs.stream().map(BehaviorLogDTO::buildDTO).collect(Collectors.toList()));
  }
}
