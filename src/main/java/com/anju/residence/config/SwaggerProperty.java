package com.anju.residence.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author cygao
 * @date 2021/1/29 3:37 下午
 **/
@Data
@Component
@ConfigurationProperties("swagger")
public class SwaggerProperty {
  /**
   * 是否开启swagger，生产环境一般关闭，所以这里定义一个变量
   */
  public static Boolean enable;

  /**
   * 项目应用名
   */
  public static String applicationName;

  /**
   * 项目版本信息
   */
  public static String applicationVersion;

  /**
   * 项目描述信息
   */
  public static String applicationDescription;

  /**
   * 接口调试地址
   */
  public static String tryHost;

}