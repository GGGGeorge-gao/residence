package com.anju.residence.controller;

import com.anju.residence.dto.UserPriceDTO;
import com.anju.residence.entity.UserPrice;
import com.anju.residence.service.PriceService;
import com.anju.residence.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cygao
 * @date 2021/2/8 10:59 上午
 **/
@Api(tags = "用户价格api")
@Slf4j
@RestController
@RequestMapping("/user/price")
public class UserPriceController {

  private final PriceService priceService;

  @Autowired
  public UserPriceController(PriceService priceService) {
    this.priceService = priceService;
  }

  @ApiOperation(value = "添加用户价格表")
  @PostMapping(value = "/add")
  public ResultVO<String> addPrice(@RequestBody UserPriceDTO userPriceDTO) {
    priceService.addPrice(userPriceDTO);
    return new ResultVO<>("success");
  }

  @ApiOperation(value = "修改用户价格表")
  @PutMapping(value = "/{userId}")
  public ResultVO<String> putPrice(@PathVariable Integer userId, @RequestBody UserPriceDTO userPriceDTO) {
    priceService.putPrice(userPriceDTO);
    return new ResultVO<>("success");
  }

  @ApiOperation(value = "获取用户价格表")
  @GetMapping(value = "/{userId}")
  public ResultVO<UserPrice> getByUserId(@PathVariable Integer userId) {
    return new ResultVO<>(priceService.getByUserId(userId));
  }
}
