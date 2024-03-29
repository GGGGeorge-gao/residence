package com.anju.residence.controller;

import com.anju.residence.annotation.ExceptionCode;
import com.anju.residence.enums.ResultCode;
import com.anju.residence.exception.ApiException;
import com.anju.residence.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.Field;

/**
 * @author cygao
 * @date 2020/12/12 21:28
 **/
@Slf4j
@RestControllerAdvice(basePackages = {"com.anju.residence.controller"})
public class ExceptionController {

  @ExceptionHandler(ApiException.class)
  public ResultVO<Object> apiExceptionHandler(ApiException e) {
    log.warn("ExceptionCode: {}, ExceptionMessage: {}.", e.getCode(), e.getMsg());
    return new ResultVO<>(e.getCode(), e.getMsg(), null);
  }

  @ExceptionHandler(UsernameNotFoundException.class)
  public ResultVO<String> usernameNotFoundExceptionHandler(UsernameNotFoundException e) {
    log.warn("UsernameNotFoundException: {}", e.getMessage());
    return new ResultVO<>(ResultCode.USER_ERROR, "用户名不存在");
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResultVO<String> validatorExceptionHandler(MethodArgumentNotValidException e) throws NoSuchFieldException {
    // 从异常中提取错误信息
    String defaultMessage = e.getAllErrors().get(0).getDefaultMessage();

    Class<?> parameterType = e.getParameter().getParameterType();

    // 获取发生异常的属性
    String fieldName = e.getBindingResult().getFieldError().getField();
    Field field = parameterType.getDeclaredField(fieldName);

    // 获取异常属性的自定义注解
    ExceptionCode exceptionCode = field.getAnnotation(ExceptionCode.class);

    // 如果有注解则返回注解的响应信息
    if (exceptionCode != null) {
      log.warn("ExceptionCode: {}, ExceptionMessage: {}.", exceptionCode.resultCode().getCode(), exceptionCode.resultCode().getMsg());
      return new ResultVO<>(exceptionCode.resultCode(), defaultMessage == null ? exceptionCode.resultCode().getMsg() : defaultMessage);
    }

    log.warn("No exceptionCode get: {} {}", fieldName, defaultMessage);
    log.info(ResultCode.INVALID_ARGUMENT.toString());
    return new ResultVO<>(ResultCode.INVALID_ARGUMENT, defaultMessage);
  }
}
