package com.anju.residence.controller;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import com.anju.residence.annotation.AnonymousAccess;
import com.anju.residence.annotation.OperationLog;
import com.anju.residence.dto.wx.WxUserDTO;
import com.anju.residence.entity.WxUser;
import com.anju.residence.enums.OperationType;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


/**
 * @author cygao
 * @date 2021/2/19 03:23 下午
 */
@Api(tags = "微信API")
@Slf4j
@RestController
@RequestMapping("/wx")
public class WxUserController {

  private final WxUserService wxUserService;
  private final WechatManager wechatManager;

  @Autowired
  public WxUserController(WxUserService wxUserService, WechatManager wechatManager) {
    this.wxUserService = wxUserService;
    this.wechatManager = wechatManager;
  }

  @OperationLog(type = OperationType.ADD, description = "/wx/login")
  @ApiOperation(value = "登录，可调用wx.getUserInfo，并在header中返回token，无需权限认证")
  @AnonymousAccess
  @PostMapping("/login")
  public ResultVO<String> login(@RequestParam(value = "resultCode") String code,
                                @RequestParam(value = "rawData", required = false) String rawData,
                                @RequestParam(value = "signature", required = false) String signature,
                                @RequestParam(value = "encryptedData", required = false) String encryptedData,
                                @RequestParam(value = "iv", required = false) String iv,
                                HttpServletResponse response) {
    log.info(code);
    log.info(rawData);
    log.info(signature);
    log.info(encryptedData);
    log.info(iv);

    WxUserDTO wxUserDTO;
    try {
      wxUserDTO = JSON.parseObject(rawData, WxUserDTO.class);
    } catch (Exception e) {
      throw new ApiException(ResultCode.INVALID_ARGUMENT);
    }

    WxSession wxSession = wechatManager.getWxSession(response, code, wxUserDTO);

    if (rawData != null && signature != null && encryptedData != null) {
      // 校验签名
      String trueSignature = DigestUtil.sha1Hex(rawData + wxSession.getSessionKey());
      if (!trueSignature.equals(signature)) {
        throw new ApiException(ResultCode.WECHAT_ERROR, "无效的签名！");
      }
    }

    return new ResultVO<>("success");
  }

  @ApiOperation(value = "获取用户信息,需在请求header中带上token")
//  @PreAuthorize("hasRole('wx_user')")
  @GetMapping("/info")
  public ResultVO<WxUser> fetch() {
    return new ResultVO<>(wxUserService.getWxUserByToken().orElseThrow(() -> new ApiException(ResultCode.WECHAT_ERROR, "openid 不存在")));
  }
}










