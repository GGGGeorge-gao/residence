package com.anju.residence.annotation;

import com.anju.residence.enums.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解记录日志
 *
 * @author cygao
 * @date 2021/2/20 6:30 下午
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OperationLog {

  OperationType type();

  String description();

}
