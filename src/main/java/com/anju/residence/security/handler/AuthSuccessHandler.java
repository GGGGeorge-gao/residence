package com.anju.residence.security.handler;

import com.anju.residence.entity.Role;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 认证成功处理类
 * 将Authentication保存到SecurityContextHolder中

 * @author cygao
 * @date 2021/2/5 10:52 下午
 */
@Slf4j
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

  public AuthSuccessHandler() {}

  @SneakyThrows
  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    log.info("进入{}了", this.getClass().getName());
    System.out.println(authentication);

    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  List<Role> getRole(){
    Role wx_user = new Role(3, "wx_user");
    ArrayList<Role> roles = new ArrayList<>();
    roles.add(wx_user);

    return roles;
  }
}
