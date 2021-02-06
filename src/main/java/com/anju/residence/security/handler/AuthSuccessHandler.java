package com.anju.residence.security.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证成功处理类
 * 将Authentication保存到SecurityContextHolder中

 * @author cygao
 * @date 2021/2/5 10:52 下午
 */
@Slf4j
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

  public AuthSuccessHandler() {
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    log.info("进入{}了", this.getClass().getName());
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }
}
