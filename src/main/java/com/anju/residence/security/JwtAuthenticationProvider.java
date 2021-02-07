package com.anju.residence.security;

import com.anju.residence.enums.ResultCode;
import com.anju.residence.exception.AuthException;
import com.anju.residence.security.jwt.JwtAuthenticationToken;
import com.anju.residence.security.jwt.JwtTokenUtil;
import com.anju.residence.security.model.UserDetailsImpl;
import com.anju.residence.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * 用于检验当前请求中的token是否有效，并返回JwtAuthenticationToken
 *
 * @author cygao
 * @date 2021/2/3 11:27 上午
 **/
@Slf4j
public class JwtAuthenticationProvider implements AuthenticationProvider {

  private final UserService userService;

  public JwtAuthenticationProvider(UserService userService) {
    this.userService = userService;
  }

  @Override
  public Authentication authenticate(Authentication auth) throws AuthenticationException {
    log.info("进入{}了", "JwtAuthenticationProvider");
    String rawToken = ((JwtAuthenticationToken) auth).getJwtToken();
    log.info(rawToken);
    // 判断是否是以Bearer开头的token
    String jwtToken = JwtTokenUtil.getJwtTokenByRawToken(rawToken);

    Claims claims = JwtTokenUtil.validateAndParse(jwtToken);

    String username = claims.getAudience();
    int userId = Integer.parseInt(claims.getSubject());
    UserDetailsImpl userDetails = userService.loadUserByUsername(username);

    if (userDetails == null || userDetails.getUserId() != userId) {
      throw new AuthException(ResultCode.USERNAME_NOT_EXISTS, "The username in token has been changed, please contact the administrator.");
    }

    ((JwtAuthenticationToken) auth).setPrincipal(userDetails);
    return auth;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.isAssignableFrom(JwtAuthenticationToken.class);
  }
}
