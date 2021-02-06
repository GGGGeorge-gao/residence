package com.anju.residence.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 支持匿名访问注解
 *
 * @author cygao
 * @date 2021/2/5 10:52 下午
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface AnonymousAccess {

}
