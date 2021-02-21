package com.anju.residence.security.model;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Authentication实现类，存储在SecurityContextHolder中，用于权限验证
 *
 * @author cygao
 * @date 2021/2/3 12:30 上午
 **/
@Getter
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

  private UserDetailsImpl userDetails;

  private final String jwtToken;

  private WxSession wxSession;

  private boolean isWxUser = false;

  public JwtAuthenticationToken(String jwtToken) {
    super(null);
    this.jwtToken = jwtToken;
  }

  public JwtAuthenticationToken(UserDetailsImpl userDetails, String jwtToken) {
    super(userDetails.getAuthorities());
    this.userDetails = userDetails;
    this.jwtToken = jwtToken;
  }

  public void setWxSession(WxSession wxSession) {
    this.wxSession = wxSession;
    this.isWxUser = wxSession != null;
  }

  public WxSession getWxSession() {
    return wxSession;
  }

  public boolean isWxUser() {
    return isWxUser;
  }

  public void setUserDetails(UserDetailsImpl userDetails) {
    this.userDetails = userDetails;
  }

  @Override
  public Object getCredentials() {
    return userDetails.getPassword();
  }

  @Override
  public Object getDetails() {
    return userDetails;
  }

  @Override
  public Object getPrincipal() {
    return userDetails;
  }


}
