package com.anju.residence.controller;

import com.anju.residence.annotation.AnonymousAccess;
import com.anju.residence.dto.wx.WxUserDTO;
import com.anju.residence.manager.WechatManager;
import com.anju.residence.service.WxUserService;
import com.anju.residence.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


/**
 * @author cygao
 * @date 2021/2/19 03:23 下午
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/wxUser")
public class WxUserController {

  private final WxUserService wxUserService;
  private final WechatManager wechatManager;

  @Autowired
  public WxUserController(WxUserService wxUserService, WechatManager wechatManager) {
    this.wxUserService = wxUserService;
    this.wechatManager = wechatManager;
  }

  @AnonymousAccess
  @GetMapping("/add")
  public ResultVO<String> addWxUser(@RequestBody @Valid WxUserDTO wxUserDTO, @RequestParam String code, HttpServletResponse response) {
    wxUserService.addWxUser(wxUserDTO);
    wechatManager.setToken(response, code);

    return new ResultVO<>("success");
  }

  @AnonymousAccess
  @GetMapping("/login")
  public ResultVO<String> login(@RequestParam String code, HttpServletResponse response) {
    wechatManager.setToken(response, code);

    return new ResultVO<>("success");
  }
}










