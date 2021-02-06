package com.anju.residence.exception;

import com.anju.residence.enums.ResultCode;
import lombok.Getter;

/**
 * @author cygao
 * @date 2020/12/10 19:34
 **/
@Getter
public class ApiException extends RuntimeException {

  private final int code;

  private final String msg;

  public ApiException(int code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  public ApiException(ResultCode resultCode) {
    this(resultCode.getCode(), resultCode.getMsg());
  }

  public ApiException() {
    this(ResultCode.INVALID_ARGUMENT);
  }

  public ApiException(String msg) {
    this(1001, msg);
  }

  @Override
  public String toString() {
    return "ApiException{" +
            "code=" + code +
            ", msg='" + msg + '\'' +
            '}';
  }
}
