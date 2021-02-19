package com.anju.residence.security.model;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * @author cygao
 * @date 2021/2/3 12:30 上午
 **/
@Getter
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

  private UserDetailsImpl userDetails;

  private final String jwtToken;

  private WxSession wxSession;

  private boolean isWxUser = false;

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
