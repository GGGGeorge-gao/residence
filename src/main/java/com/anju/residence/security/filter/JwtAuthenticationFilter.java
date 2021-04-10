package com.anju.residence.security.filter;

import com.anju.residence.security.handler.AuthFailureHandler;
import com.anju.residence.security.handler.AuthSuccessHandler;
import com.anju.residence.params.JwtParams;
import com.anju.residence.security.model.JwtAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author cygao
 * @date 2021/2/3 3:25 下午
 **/
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final AuthenticationManager authenticationManager;

  private final AuthenticationSuccessHandler successHandler = new AuthSuccessHandler();
  private final AuthenticationFailureHandler failureHandler = new AuthFailureHandler();

  public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  @Override
  public void afterPropertiesSet() {
    Assert.notNull(authenticationManager, "authenticationManager must be specified");
    Assert.notNull(successHandler, "AuthenticationSuccessHandler must be specified");
    Assert.notNull(failureHandler, "AuthenticationFailureHandler must be specified");
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String rawToken = request.getHeader(JwtParams.TOKEN_HEADER);

    if (rawToken == null) {
      filterChain.doFilter(request, response);
      return;
    }

    Authentication authToken = null;
    AuthenticationException failedException = null;

    try {
      authToken = this.authenticationManager.authenticate(new JwtAuthenticationToken(rawToken));
    } catch (AuthenticationException e) {
      logger.error(e.getMessage());

      failedException = e;
    }

    if (authToken != null) {
      successHandler.onAuthenticationSuccess(request, response, authToken);
    } else {
      failureHandler.onAuthenticationFailure(request, response, failedException);
      return;
    }
    logger.info("JwtAuthenticationFilter验证通过");
    filterChain.doFilter(request, response);
  }




}