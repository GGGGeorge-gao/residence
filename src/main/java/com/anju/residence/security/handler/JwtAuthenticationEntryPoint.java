package com.anju.residence.security.handler;

import cn.hutool.json.JSONUtil;
import com.anju.residence.enums.ResultCode;
import com.anju.residence.exception.AuthException;
import com.anju.residence.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 处理没有token授权的请求
 *
 * @author cygao
 * @date 2021/1/2 16:56
 **/
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
    log.info(e.getClass().toString());
    log.warn("JwtAuthenticationEntryPoint:" + e.getMessage());
    response.setStatus(200);
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/json; charset=utf-8");

    ResultVO<String> body;

    if (e instanceof AuthException) {
      body = new ResultVO<>(((AuthException) e).getResultCode(), "failed");
    } else if (e instanceof BadCredentialsException) {
      body = new ResultVO<>(ResultCode.WRONG_USERNAME_OR_PASSWORD, "密码错误");
    } else {
      body = new ResultVO<>(ResultCode.UNAUTHORIZED_REQUEST, "未验证的请求");
    }

    String jsonBody = JSONUtil.toJsonPrettyStr(body);

    PrintWriter writer = response.getWriter();
    writer.write(jsonBody);
    writer.flush();
    writer.close();
//    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e == null ? "Unauthorized" : e.getMessage());
  }
}
