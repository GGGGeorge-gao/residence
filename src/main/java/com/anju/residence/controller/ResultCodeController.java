package com.anju.residence.controller;

import com.anju.residence.enums.ResultCode;
import com.anju.residence.vo.ResultVO;
import io.swagger.annotations.Api;
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
@RequestMapping("/api/v1/code")
public class ResultCodeController {

  @GetMapping("/{code}")
  public ResultVO<String> getResultCodeInfo(@PathVariable Integer code) {
    if (!ResultCode.RESULT_CODE_MAP.containsKey(code)) {
      return new ResultVO<>("该状态码不存在!");
    }
    return new ResultVO<>(ResultCode.RESULT_CODE_MAP.get(code).getMsg());
  }
}
