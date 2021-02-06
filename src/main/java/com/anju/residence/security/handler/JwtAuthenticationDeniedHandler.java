package com.anju.residence.security.handler;

import cn.hutool.json.JSONUtil;
import com.anju.residence.enums.ResultCode;
import com.anju.residence.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author cygao
 * @date 2021/1/3 15:18
 **/
@Slf4j
@Component
public class JwtAuthenticationDeniedHandler implements AccessDeniedHandler {

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException {
    log.warn("JwtAuthenticationDeniedHandler:" + (e == null ? "Insufficient authority" : e.getMessage()));
    response.setStatus(200);
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/json; charset=utf-8");

    ResultVO<String> body = new ResultVO<>(ResultCode.INSUFFICIENT_AUTHORITY, "权限不足");
    String jsonBody = JSONUtil.toJsonPrettyStr(body);

    PrintWriter writer = response.getWriter();
    writer.write(jsonBody);
    writer.flush();
  }
}
