package com.anju.residence.controller.ele;

import com.anju.residence.annotation.OperationLog;
import com.anju.residence.dto.ele.RecLogDTO;
import com.anju.residence.entity.ele.ReceptacleLog;
import com.anju.residence.enums.OperationType;
import com.anju.residence.enums.ResultCode;
import com.anju.residence.exception.ApiException;
import com.anju.residence.service.ele.ReceptacleLogService;
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
 * @date 2020/12/15 13:16
 **/
@Api(tags = "插座用电日志API（智能插座）")
@Slf4j
@RequestMapping("/receptacle_log")
@RestController
public class ReceptacleLogController {

  private final ReceptacleLogService receptacleLogService;
  private final UserService userService;

  @Autowired
  public ReceptacleLogController(ReceptacleLogService receptacleLogService, UserService userService) {
    this.receptacleLogService = receptacleLogService;
    this.userService = userService;
  }

  @ApiOperation(value = "获取用户在当天的耗电量", notes = "耗电量单位为焦耳，插座用电日志记录的单位跨度为天，用电日志会在添加耗电日志时自动生成")
  @ApiImplicitParams({@ApiImplicitParam(name = "userId", value = "用户id", paramType = "path", dataTypeClass = Integer.class, required = true)})
  @GetMapping("/consumption/today/{userId}")
  public ResultVO<Integer> getTodayAllConsumptionByUserId(@PathVariable Integer userId) {
    if (!userService.existsById(userId)) {
      throw new ApiException(ResultCode.USER_ERROR, "用户id不存在");
    }
    return new ResultVO<>(receptacleLogService.getTodayTotalConsumption(userId));
  }

  @ApiOperation(value = "获取插座在一个时段内的用电日志", notes = "插座用电日志记录的单位跨度为天，用电日志会在添加耗电日志时自动生成")
  @ApiImplicitParams({@ApiImplicitParam(name = "from", value = "起始时间，格式为yyyy-MM-dd", paramType = "query", dataTypeClass = Date.class, required = true),
                      @ApiImplicitParam(name = "to", value = "终止时间，格式为yyyy-MM-dd", paramType = "query", dataTypeClass = Date.class, required = true),
                      @ApiImplicitParam(name = "receptacleId", value = "插座id", paramType = "path", dataTypeClass = Integer.class, required = true)})
  @GetMapping("/between/{receptacleId}")
  public ResultVO<List<RecLogDTO>> getLogBetween(@PathVariable Integer receptacleId,
                                              @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
                                              @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date to) {
    if (from == null || to == null || from.after(to)) {
      throw new ApiException(ResultCode.INVALID_ARGUMENT, "无效的日期参数");
    }

    List<ReceptacleLog> logs = receptacleLogService.listByReceptacleIdBetween(receptacleId, from, to);

    List<RecLogDTO> res = logs.stream().map(l -> RecLogDTO.build(l, receptacleId)).collect(Collectors.toList());

    return new ResultVO<>(res);
  }

  @OperationLog(type = OperationType.DELETE, description = "删除一条日志")
  @ApiOperation(value = "删除一条日志", notes = "url路径参数为日志id")
  @DeleteMapping("/{logId}")
  public ResultVO<String> deleteById(@PathVariable Integer logId) {
    receptacleLogService.deleteByReceptacleId(logId);

    return new ResultVO<>("success");
  }

}
