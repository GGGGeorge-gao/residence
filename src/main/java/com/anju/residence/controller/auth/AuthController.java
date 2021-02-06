package com.anju.residence.controller.auth;

import com.anju.residence.annotation.AnonymousAccess;
import com.anju.residence.security.model.AccountCredentials;
import com.anju.residence.security.model.UserDetailsImpl;
import com.anju.residence.security.jwt.JwtTokenUtil;
import com.anju.residence.service.UserService;
import com.anju.residence.vo.ResultVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cygao
 * @date 2021/1/2 15:47
 **/
@Api(tags = "权限认证API（开发中）")
@Slf4j
@RestController
@RequestMapping(value = "/api/authentication")
public class AuthController {

  private final UserService userService;

  @Autowired
  public AuthController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping(value = "/login")
  public ResultVO<String> login(@RequestBody AccountCredentials ac) {
    final UserDetailsImpl userDetails = userService.loadUserByUsername(ac.getUsername());
    final String token = JwtTokenUtil.generateToken(userDetails);
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    return new ResultVO<>(token);
  }

  @PreAuthorize("hasAuthority('ORDINARY')")
  @GetMapping(value = "test")
  public ResultVO<String> testMethod() {
    Authentication authenticationToken = SecurityContextHolder.getContext().getAuthentication();

    return new ResultVO<>("GOGO");
  }

  @GetMapping(value = "/aaa")
  public ResultVO<String> aaa() {
    return new ResultVO<>("success");
  }
}
