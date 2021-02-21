package com.anju.residence.security.filter;

import com.alibaba.fastjson.JSON;
import com.anju.residence.enums.ResultCode;
import com.anju.residence.security.model.AccountCredentials;
import com.anju.residence.security.model.UserDetailsImpl;
import com.anju.residence.security.jwt.JwtProperty;
import com.anju.residence.security.jwt.JwtTokenUtil;
import com.anju.residence.manager.UserLogManager;
import com.anju.residence.service.UserService;
import com.anju.residence.util.ResponseUtil;
import com.anju.residence.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author cygao
 * @date 2021/2/2 5:40 下午
 **/
@Slf4j
public class PasswordLoginFilter extends AbstractAuthenticationProcessingFilter {

  private final UserDetailsService userDetailsService;
  private final UserLogManager userLogManager;

  public PasswordLoginFilter(AuthenticationManager authManager, UserService userDetailsService, UserLogManager userLogManager) {
    // 对匹配的请求进行过滤
    super(new AntPathRequestMatcher(JwtProperty.PASSWORD_LOGIN_URL, "POST"));
    this.userDetailsService = userDetailsService;
    this.userLogManager = userLogManager;
    setAuthenticationManager(authManager);
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
    log.info("进入JwtLoginFilter了");
    AccountCredentials ac = JSON.parseObject(request.getInputStream(), AccountCredentials.class);

    log.info("PasswordLoginFilter attemptAuthentication get: " + ac.toString());
    return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(ac.getUsername(), ac.getPassword()));
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {

    UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(authResult.getName());
    String jwtToken = JwtTokenUtil.generateToken(userDetails);

    response.setHeader(JwtProperty.TOKEN_HEADER, jwtToken);

    // 以json格式返回jwt token
    ResponseUtil.response(response, new ResultVO<>(ResultCode.SUCCESS, "success"));
    userLogManager.addUserLog(userDetails.getUserId(), request);
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
    ResponseUtil.response(response, new ResultVO<>(ResultCode.WRONG_USERNAME_OR_PASSWORD, "failed"));
  }
}
