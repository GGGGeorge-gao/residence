package com.anju.residence.manager;

import com.anju.residence.dto.wx.WxSessionResponse;
import com.anju.residence.dto.wx.WxUserDTO;
import com.anju.residence.entity.User;
import com.anju.residence.entity.WxUser;
import com.anju.residence.enums.ResultCode;
import com.anju.residence.enums.WechatErrCode;
import com.anju.residence.exception.ApiException;
import com.anju.residence.security.jwt.JwtProperty;
import com.anju.residence.security.jwt.JwtTokenUtil;
import com.anju.residence.security.model.JwtAuthenticationToken;
import com.anju.residence.security.model.WxSession;
import com.anju.residence.service.WxUserService;
import com.anju.residence.util.WechatUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author cygao
 * @date 2021/2/19 10:38 下午
 **/
@Slf4j
@Service
public class WechatManager {

  private final PasswordEncoder passwordEncoder;
  private final WxUserService wxUserService;

  @Autowired
  public WechatManager(PasswordEncoder passwordEncoder, UserLogManager userLogManager, WxUserService wxUserService) {
    this.passwordEncoder = passwordEncoder;
    this.wxUserService = wxUserService;
  }

  /**
   * 对前端对登录请求进行处理，获取WxSession并进行校验、保存、产生token并传输回前端
   * @param response response
   * @param code js_code
   */
  public WxSession getWxSession(HttpServletResponse response, String code, WxUserDTO wxUserDTO) {
    if (code == null || wxUserDTO == null) {
      throw new ApiException(ResultCode.INVALID_JS_CODE);
    }
    WxSessionResponse wxSessionResponse = WechatUtil.getSessionKeyOrOpenId(code);

    Integer errcode = wxSessionResponse.getErrcode();
    log.info("Wechat response: " + wxSessionResponse.toString());
    WxSession wxSession;

    if (errcode == null || errcode.equals(WechatErrCode.SUCCESS.getCode())) {
      wxSession = wxSessionResponse.buildSession();
    } else {
      if (WechatErrCode.INVALID_JS_CODE.getCode().equals(errcode)) {
        throw new ApiException(ResultCode.INVALID_JS_CODE);
      } else if (WechatErrCode.BUSY_WECHAT_SERVER.getCode().equals(errcode)) {
        throw new ApiException(ResultCode.BUSY_WECHAT_SERVER);
      } else if (WechatErrCode.REQUEST_TOO_FREQUENT.getCode().equals(errcode)) {
        throw new ApiException(ResultCode.REQUEST_TOO_FREQUENT);
      } else {
        throw new ApiException(ResultCode.CONNECTION_ERROR.getCode(), ResultCode.CONNECTION_ERROR.getMsg() + errcode);
      }
    }

    String skey = passwordEncoder.encode(wxSession.getOpenId() + wxSession.getSessionKey());
    wxSession.setSkey(skey);
    log.info(wxSession.toString());

    WxUser wxUser = wxUserService.updateByWxSession(wxUserDTO, wxSession);

    setToken(wxUser, wxSession, response);

    return wxSession;
  }

  public void setToken(WxUser wxUser, WxSession wxSession, HttpServletResponse response) {
    String token = JwtTokenUtil.generateToken(wxUser.getUser().getId(), wxUser.getUser().getUsername(), wxSession);
    log.info(token);
    response.setHeader(JwtProperty.TOKEN_HEADER, token);
  }

  /**
   * 通过SecurityContextHolder存储的Authentication获取WxSession
   * @return {@link WxSession}
   */
  public static WxSession getWxSessionByToken() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (!(auth instanceof JwtAuthenticationToken)) {
      log.error("Wrong Authentication Class get: {}", auth.getClass().getName());
      throw new ApiException(ResultCode.UNKNOWN_ERROR);
    }
    JwtAuthenticationToken authToken = (JwtAuthenticationToken) auth;

    WxSession wxSession = authToken.getWxSession();
    if (wxSession == null) {
      throw new ApiException(ResultCode.NO_WX_SESSION_EXISTS);
    }
    return wxSession;
  }

}








