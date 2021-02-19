package com.anju.residence.security.filter;

import com.anju.residence.enums.ResultCode;
import com.anju.residence.exception.AuthException;
import com.anju.residence.security.handler.AuthFailureHandler;
import com.anju.residence.security.handler.AuthSuccessHandler;
import com.anju.residence.security.jwt.JwtTokenUtil;
import com.anju.residence.security.model.JwtAuthenticationToken;
import com.anju.residence.security.jwt.JwtProperty;
import com.anju.residence.security.model.UserDetailsImpl;
import com.anju.residence.security.model.WxSession;
import com.anju.residence.service.UserService;
import com.anju.residence.service.WxUserService;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
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

  private final UserService userService;
  private final WxUserService wxUserService;

  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;

  private final AuthenticationSuccessHandler successHandler = new AuthSuccessHandler();
  private final AuthenticationFailureHandler failureHandler = new AuthFailureHandler();

  public JwtAuthenticationFilter(UserService userService, WxUserService wxUserService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
    this.userService = userService;
    this.wxUserService = wxUserService;
    this.passwordEncoder = passwordEncoder;
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

      String jwtToken = JwtTokenUtil.getJwtTokenByRawToken(rawToken);
      Claims claims = JwtTokenUtil.validateAndParse(jwtToken);

      String username = (String) claims.get("username");
      UserDetailsImpl userDetails = userService.loadUserByUsername(username);

      JwtAuthenticationToken auth = new JwtAuthenticationToken(userDetails, rawToken);

      // 判断是否是微信用户
      if (claims.get("open_id") != null) {
        checkAndSetWxSession(auth, claims);
      }

      authToken = auth;

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


  private void checkAndSetWxSession(JwtAuthenticationToken auth, Claims claims) {
    // TODO 验证skey(3rd_session)的一致性，对openid和sessionKey进行BCrypt加密，与数据库的进行比对
    String openId = (String) claims.get("open_id");
    String skey = (String) claims.get("skey");
    String sessionKey = wxUserService.getSessionKeyByOpenId(openId).orElseThrow(() -> new AuthException(ResultCode.NO_SESSION_KEY_EXISTS));

    if (!passwordEncoder.matches(openId + sessionKey, skey)) {
      throw new AuthException(ResultCode.INVALID_SKEY);
    }

    WxSession wxSession = WxSession.builder().openId(openId).sessionKey(sessionKey).skey(skey).build();
    auth.setWxSession(wxSession);
  }

}