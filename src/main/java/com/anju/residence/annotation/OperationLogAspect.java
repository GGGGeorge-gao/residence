package com.anju.residence.annotation;

import com.anju.residence.entity.User;
import com.anju.residence.entity.UserLog;
import com.anju.residence.manager.UserLogManager;
import com.anju.residence.security.model.JwtAuthenticationToken;
import com.anju.residence.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * OperationLog切面处理类
 *
 * @author cygao
 * @date 2021/2/20 8:16 下午
 **/
@Slf4j
@Aspect
@Component
public class OperationLogAspect {

  private UserLogManager userLogManager;

  @Autowired
  public OperationLogAspect(UserLogManager userLogManager) {
    this.userLogManager = userLogManager;
  }

  @Pointcut("@annotation(com.anju.residence.annotation.OperationLog)")
  public void execute() {

  }

  @Before("execute()")
  public void doAround(JoinPoint joinPoint) {
    OperationLog operationLog = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(OperationLog.class);

    UserLog userLog = UserLog.builder().type(operationLog.type().getType()).description(operationLog.description()).build();
    log.info(userLog.toString());
    if (RequestContextHolder.getRequestAttributes() != null) {
      HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

      userLog.setIp(HttpUtil.getRealIp(request));
      userLog.setTime(new Date());
    }
    log.info(userLog.toString());
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth instanceof JwtAuthenticationToken) {
      userLog.setUser(User.builder().id(((JwtAuthenticationToken) auth).getUserDetails().getUserId()).build());
    }
    log.info(userLog.toString());
    userLogManager.save(userLog);
  }
}












