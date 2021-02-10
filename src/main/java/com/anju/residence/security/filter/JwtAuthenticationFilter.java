package com.anju.residence.security.filter;

import com.anju.residence.security.handler.AuthFailureHandler;
import com.anju.residence.security.handler.AuthSuccessHandler;
import com.anju.residence.security.jwt.JwtAuthenticationToken;
import com.anju.residence.security.jwt.JwtProperty;
import com.anju.residence.service.LoginInfoService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cygao
 * @date 2021/2/3 3:25 下午
 **/
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private AuthenticationManager authenticationManager;
  private final List<RequestMatcher> permissiveRequestMatchers = new ArrayList<>();

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
    String rawToken = request.getHeader(JwtProperty.TOKEN_HEADER);

    if (rawToken == null) {
      filterChain.doFilter(request, response);
      return;
    }

    Authentication authToken = null;
    AuthenticationException failedException = null;

    try {
      logger.info("Header Authorization: " + rawToken);
      JwtAuthenticationToken auth = new JwtAuthenticationToken(rawToken);

      authToken = this.authenticationManager.authenticate(auth);

    } catch (AuthenticationException e) {
      logger.error(e.getMessage());

      failedException = e;
    }

    if (authToken != null) {
      successHandler.onAuthenticationSuccess(request, response, authToken);
    } else if (!isPermissiveRequest(request)) {
      failureHandler.onAuthenticationFailure(request, response, failedException);
      return;
    }
    logger.info("JwtAuthenticationFilter验证通过");
    filterChain.doFilter(request, response);
  }

  public void addPermissiveUrl(String... urls) {
    for (String url : urls) {
      permissiveRequestMatchers.add(new AntPathRequestMatcher(url));
    }
  }

  /**
   * 是否为不需要认证的请求
   */
  private boolean isPermissiveRequest(HttpServletRequest request) {
    for (RequestMatcher matcher : permissiveRequestMatchers) {
      if (matcher.matches(request)) {
        return true;
      }
    }
    return false;
  }

}