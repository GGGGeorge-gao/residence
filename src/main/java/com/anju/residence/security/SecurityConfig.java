package com.anju.residence.security;

import com.anju.residence.annotation.AnonymousAccess;
import com.anju.residence.security.filter.JwtAuthenticationProvider;
import com.anju.residence.security.handler.JwtAuthenticationDeniedHandler;
import com.anju.residence.security.handler.JwtAuthenticationEntryPoint;
import com.anju.residence.security.filter.JwtAuthenticationFilter;
import com.anju.residence.security.filter.PasswordLoginFilter;
import com.anju.residence.manager.UserLogManager;
import com.anju.residence.service.UserService;
import com.anju.residence.service.WxUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * <h2>核心组件：</h2>
 * {@link AuthenticationManager} 用户认证的管理类<br>
 * {@link AuthenticationProvider} 认证的具体实现类<br><br>
 * AuthenticationManager是一切认证的起点，一般使用其实现类 {@link ProviderManager}
 * <br>而 AuthenticationProvider 提供不同的认证方式
 * 使用账号密码认证方式{@link DaoAuthenticationProvider}实现类，其为默认认证方式，进行数据库库获取认证数据信息。
 * 游客身份登录认证方式AnonymousAuthenticationProvider实现类
 * 从cookies获取认证方式RememberMeAuthenticationProvider实现类
 *
 * @author cygao
 * @date 2021/1/2 17:09
 **/
@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final UserService userService;
  private final WxUserService wxUserService;

  private final UserLogManager userLogManager;
  private final PasswordEncoder passwordEncoder;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  private final JwtAuthenticationDeniedHandler jwtAuthenticationDeniedHandler;

  private final Set<String> anonymousUrls = new HashSet<>();

  public static final String[] SWAGGER_WHITELIST = {
          "/swagger-ui.html",
          "/swagger-ui/*",
          "/swagger-resources/**",
          "/v2/api-docs",
          "/v3/api-docs",
          "/webjars/**"
  };

  @Autowired
  public SecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, UserService userService, WxUserService wxUserService, UserLogManager userLogManager, PasswordEncoder passwordEncoder, JwtAuthenticationDeniedHandler jwtAuthenticationDeniedHandler) {
    this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    this.userService = userService;
    this.wxUserService = wxUserService;
    this.userLogManager = userLogManager;
    this.passwordEncoder = passwordEncoder;
    this.jwtAuthenticationDeniedHandler = jwtAuthenticationDeniedHandler;

  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userService).passwordEncoder(passwordEncoder);

    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setPasswordEncoder(passwordEncoder);
    authenticationProvider.setUserDetailsService(userService);

    auth.authenticationProvider(authenticationProvider);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    Map<RequestMappingInfo, HandlerMethod> handlerMethods = getApplicationContext().getBean(RequestMappingHandlerMapping.class).getHandlerMethods();
    for (Map.Entry<RequestMappingInfo, HandlerMethod> infoEntry : handlerMethods.entrySet()) {
      HandlerMethod handlerMethod = infoEntry.getValue();
      AnonymousAccess anonymousAccess = handlerMethod.getMethodAnnotation(AnonymousAccess.class);
      if (anonymousAccess != null && infoEntry.getKey().getPatternsCondition() != null) {
        anonymousUrls.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
      }
    }
    anonymousUrls.forEach(s -> log.info("可以匿名访问的url: {}", s));

    http
            // 禁用csrf
            .csrf().disable()
            .formLogin().disable()

            // 防止iframe 造成跨域请求
//            .headers()
//            .frameOptions()
//            .disable()
//            .and()

            // 授权异常处理类
            .exceptionHandling()
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .accessDeniedHandler(jwtAuthenticationDeniedHandler)

            // no session
            .and()
            .sessionManagement().disable()

            .authorizeRequests()

            // 放行druid
            .antMatchers("/druid/**").permitAll()

            // 允许匿名及登录用户访问
            .antMatchers("/api/auth/**", "/error/**").permitAll()
            .antMatchers(HttpMethod.OPTIONS, "/**").anonymous()

//            .antMatchers(HttpMethod.POST, "/**").permitAll()

            // 放行swagger
            .antMatchers(SWAGGER_WHITELIST).permitAll()

            .antMatchers(anonymousUrls.toArray(new String[0])).anonymous()

            // 所有请求都需要认证
            .anyRequest().authenticated()

            .and()
            .addFilterBefore(new PasswordLoginFilter(authenticationManager(), userService, userLogManager), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(new JwtAuthenticationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);

  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(daoAuthenticationProvider()).authenticationProvider(jwtAuthenticationProvider());
  }

//  @Override
//  public void configure(WebSecurity web) throws Exception {
//    // TODO: 测试用，放行所有请求
//    web.ignoring().antMatchers("/**");
//  }

  @Bean
  public DaoAuthenticationProvider daoAuthenticationProvider() {
    DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
    daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
    daoAuthenticationProvider.setUserDetailsService(userService);
    return daoAuthenticationProvider;
  }

  @Bean
  public JwtAuthenticationProvider jwtAuthenticationProvider() {
    return new JwtAuthenticationProvider(userService, wxUserService, passwordEncoder);
  }

}

