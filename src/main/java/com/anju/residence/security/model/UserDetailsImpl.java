package com.anju.residence.security.model;

import com.anju.residence.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * @author cygao
 * @date 2021/1/1 15:04
 **/
@Data
@EqualsAndHashCode
public class UserDetailsImpl implements UserDetails {

  private final Integer userId;

  private final String username;

  private final String password;

  private final Collection<? extends GrantedAuthority> authorities;

  public UserDetailsImpl(Integer userId, String username, String password, Collection<? extends GrantedAuthority> authorities) {
    this.userId = userId;
    this.username = username;
    this.password = password;
    this.authorities = authorities;
  }

  public UserDetailsImpl(User user) {
    this(user.getId(), user.getUsername(), user.getPassword(), user.getRoles() == null ? null : Collections.unmodifiableList(user.getRoles()));
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }


}
