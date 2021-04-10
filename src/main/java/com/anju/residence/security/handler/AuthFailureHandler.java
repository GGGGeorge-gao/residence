package com.anju.residence.security.handler;

import com.anju.residence.enums.ResultCode;
import com.anju.residence.exception.AuthException;
import com.anju.residence.util.ResponseUtil;
import com.anju.residence.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证失败处理类
 * 直接返回认证异常信息
 * @author cygao
 */
@Slf4j
public class AuthFailureHandler implements AuthenticationFailureHandler {

  public AuthFailureHandler() {
  }

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
    log.info("进入{}了", this.getClass().toString());
    if (e instanceof AuthException) {
      AuthException authException = (AuthException) e;

      ResponseUtil.response(response, new ResultVO<>(authException.getResultCode(), authException.getMsg()));
    } else {
      ResponseUtil.response(response, new ResultVO<>(ResultCode.AUTH_ERROR, e == null ? "没有权限" : e.getMessage()));
    }
  }
}
