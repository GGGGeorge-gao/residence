package com.anju.residence.security.jwt;

import com.anju.residence.security.model.UserDetailsImpl;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;

/**
 * @author cygao
 * @date 2021/2/3 12:30 上午
 **/
@Getter
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

  private UserDetailsImpl principal;

  private final String jwtToken;

  public JwtAuthenticationToken(String jwtToken) {
    super(Collections.emptyList());
    this.jwtToken = jwtToken;
  }

  public JwtAuthenticationToken(UserDetailsImpl userDetails, String jwtToken) {
    super(userDetails.getAuthorities());
    this.jwtToken = jwtToken;
    this.principal = userDetails;
  }

  @Override
  public Object getCredentials() {
    return principal.getPassword();
  }

  @Override
  public Object getPrincipal() {
    return principal;
  }

  public void setPrincipal(UserDetailsImpl principal) {
    this.principal = principal;
  }
}
