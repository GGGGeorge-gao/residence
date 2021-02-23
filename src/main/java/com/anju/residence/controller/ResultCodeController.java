package com.anju.residence.controller;

import com.anju.residence.annotation.AnonymousAccess;
import com.anju.residence.annotation.OperationLog;
import com.anju.residence.enums.OperationType;
import com.anju.residence.enums.ResultCode;
import com.anju.residence.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 查询状态码含义
 *
 * @author cygao
 * @date 2021/1/30 4:58 下午
 **/
@Api(tags = "状态码含义查询API")
@RestController
@RequestMapping("/code")
public class ResultCodeController {

  @OperationLog(type = OperationType.OPERATION, description = "查询状态码")
  @GetMapping("/{code}")
  public ResultVO<String> getResultCodeInfo(@PathVariable Integer code) {
    if (!ResultCode.RESULT_CODE_MAP.containsKey(code)) {
      return new ResultVO<>("该状态码不存在!");
    }
    return new ResultVO<>(ResultCode.RESULT_CODE_MAP.get(code).getMsg());
  }

  @AnonymousAccess
  @ApiOperation(value = "测试后端服务有没有挂，是否可用")
  @OperationLog(type = OperationType.OPERATION, description = "test")
  @GetMapping("/test")
  public ResultVO<String> test() {
    return new ResultVO<>("success");
  }
}
