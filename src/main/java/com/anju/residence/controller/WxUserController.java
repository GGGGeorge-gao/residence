package com.anju.residence.controller;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import com.anju.residence.annotation.AnonymousAccess;
import com.anju.residence.dto.wx.WxUserDTO;
import com.anju.residence.entity.WxUser;
import com.anju.residence.enums.ResultCode;
import com.anju.residence.exception.ApiException;
import com.anju.residence.manager.WechatManager;
import com.anju.residence.security.model.WxSession;
import com.anju.residence.service.WxUserService;
import com.anju.residence.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


/**
 * @author cygao
 * @date 2021/2/19 03:23 下午
 */
@Api(tags = "微信API")
@Slf4j
@RestController
@RequestMapping("/api/v1/wx")
public class WxUserController {

  private final WxUserService wxUserService;
  private final WechatManager wechatManager;

  @Autowired
  public WxUserController(WxUserService wxUserService, WechatManager wechatManager) {
    this.wxUserService = wxUserService;
    this.wechatManager = wechatManager;
  }

  @ApiOperation(value = "添加一个用户，并在header中返回token")
  @AnonymousAccess
  @GetMapping("/add")
  public ResultVO<String> addWxUser(@RequestBody @Valid WxUserDTO wxUserDTO, @RequestParam String code, HttpServletRequest request, HttpServletResponse response) {
    wxUserService.addWxUser(wxUserDTO);
    wechatManager.setToken(request, response, code);

    return new ResultVO<>("success");
  }

  @ApiOperation(value = "登录，可调用wx.getUserInfo，并在header中返回token", tags = "可通过此接口新建或修改用户")
  @AnonymousAccess
  @PostMapping("/login")
  public ResultVO<String> login(@RequestParam(value = "resultCode") String code,
                                @RequestParam(value = "rawData", required = false) String rawData,
                                @RequestParam(value = "signature", required = false) String signature,
                                @RequestParam(value = "encryptedData", required = false) String encryptedData,
                                @RequestParam(value = "iv", required = false) String iv,
                                HttpServletRequest request, HttpServletResponse response) {
    log.info(code);
    log.info(rawData);
    log.info(signature);
    log.info(encryptedData);
    log.info(iv);
    WxSession wxSession = wechatManager.setToken(request, response, code);

    if (rawData != null && signature != null && encryptedData != null) {
      // 校验签名
      String trueSignature = DigestUtil.sha1Hex(rawData + wxSession.getSessionKey());
      if (!trueSignature.equals(signature)) {
        throw new ApiException(ResultCode.INVALID_WECHAT_SIGNATURE);
      }
      WxUserDTO wxUserDTO = JSON.parseObject(rawData, WxUserDTO.class);
      wxUserDTO.setOpenId(wxSession.getOpenId());
      wxUserDTO.setSkey(wxSession.getSkey());

      wxUserService.login(wxUserDTO);
    }

    return new ResultVO<>("success");
  }

  @ApiOperation(value = "获取用户信息,需在请求header中带上token")
  @PreAuthorize("hasRole('wx_user')")
  @GetMapping("/info")
  public ResultVO<WxUser> fetch() {
    return new ResultVO<>(wxUserService.getWxUserByToken().orElseThrow(() -> new ApiException(ResultCode.OPEN_ID_NOT_EXISTS)));
  }
}










