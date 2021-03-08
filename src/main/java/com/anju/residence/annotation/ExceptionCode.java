package com.anju.residence.annotation;

import com.anju.residence.enums.ResultCode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 异常响应码注解
 *
 * @author cygao
 * @date 2020/12/14 19:28
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ExceptionCode {

  // 响应状态码
  ResultCode resultCode() default ResultCode.UNKNOWN_ERROR;

}
