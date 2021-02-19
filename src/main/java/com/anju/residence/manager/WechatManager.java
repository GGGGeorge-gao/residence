package com.anju.residence.manager;

import com.anju.residence.dto.wx.WxSessionResponse;
import com.anju.residence.entity.WxUser;
import com.anju.residence.enums.ResultCode;
import com.anju.residence.exception.ApiException;
import com.anju.residence.security.jwt.JwtProperty;
import com.anju.residence.security.jwt.JwtTokenUtil;
import com.anju.residence.security.model.WxSession;
import com.anju.residence.service.UserService;
import com.anju.residence.service.WxUserService;
import com.anju.residence.util.WechatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

/**
 * @author cygao
 * @date 2021/2/19 10:38 下午
 **/
@Service
public class WechatManager {

  private final PasswordEncoder passwordEncoder;

  private final UserService userService;
  private final WxUserService wxUserService;

  @Autowired
  public WechatManager(PasswordEncoder passwordEncoder, UserService userService, WxUserService wxUserService) {
    this.passwordEncoder = passwordEncoder;
    this.userService = userService;
    this.wxUserService = wxUserService;
  }

  public void setToken(HttpServletResponse response, String code) {
    if (code == null) {
      throw new ApiException(ResultCode.INVALID_JS_CODE);
    }
    WxSessionResponse wxSessionResponse = WechatUtil.getSessionKeyOrOpenId(code);

    Integer errcode = wxSessionResponse.getErrcode();

    WxSession wxSession;
    if (errcode == null) {
      throw new ApiException(ResultCode.CONNECTION_ERROR);
    } else if (errcode == 0) {
      wxSession = wxSessionResponse.buildSession();
    } else if (errcode == 40029) {
      throw new ApiException(ResultCode.INVALID_JS_CODE);
    } else if (errcode == -1) {
      throw new ApiException(ResultCode.WECHAT_SERVER_BUSY);
    } else if (errcode == 45011) {
      throw new ApiException(ResultCode.REQUEST_TOO_FREQUENT);
    } else {
      throw new ApiException(ResultCode.UNKNOWN_ERROR);
    }
    String skey = passwordEncoder.encode(wxSession.getOpenId() + wxSession.getSessionKey());
    wxSession.setSkey(skey);

    WxUser wxUser = wxUserService.getWxUserByOpenId(wxSession.getOpenId()).orElseThrow(() -> new ApiException(ResultCode.OPEN_ID_NOT_EXISTS));
    wxUserService.updateByWxSession(wxSession);

    String token = JwtTokenUtil.generateToken(wxUser.getUser().getId(), wxUser.getUser().getUsername(), wxSession);

    response.setHeader(JwtProperty.TOKEN_HEADER, token);
  }

}
