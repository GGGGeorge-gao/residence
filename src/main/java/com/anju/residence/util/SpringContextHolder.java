package com.anju.residence.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @author cygao
 * @date 2020/12/30 16:32
 **/
@Slf4j
@Component
@Lazy(value = false)
public class SpringContextHolder implements ApplicationContextAware, DisposableBean {

  private static ApplicationContext context = null;

  public static ApplicationContext getApplicationContext() {
    return context;
  }

  @SuppressWarnings("unchecked")
  public static <T> T getBean(String name) {
    assertContextInjected();
    return (T) context.getBean(name);
  }

  public static <T> T getBean(Class<T> requiredType) {
    assertContextInjected();
    return context.getBean(requiredType);
  }

  public static void clearHolder() {
    log.debug("Clear ApplicationContext: " + context);
    context = null;
  }

  @Override
  public void destroy() throws Exception {
    clearHolder();
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    if (SpringContextHolder.context != null) {
      log.warn("SpringContextHolder ApplicationContext被覆盖");
    }
    context = applicationContext;
  }

  private static void assertContextInjected() {
    if (context == null) {
      throw new IllegalArgumentException("ApplicationContext未注入");
    }
  }
}
