package com.anju.residence.exception;

import com.anju.residence.enums.ResultCode;
import org.springframework.security.core.AuthenticationException;

/**
 * @author cygao
 * @date 2021/2/3 4:45 下午
 **/
public class AuthException extends AuthenticationException {

  private static final long serialVersionUID = -6524112676786082427L;

  private final ResultCode resultCode;

  private final String msg;

  public AuthException(ResultCode resultCode) {
    this(resultCode, null);
  }

  public AuthException(ResultCode resultCode, String msg) {
    super(msg);
    this.msg = msg;
    this.resultCode = resultCode;
  }

  public AuthException(ResultCode resultCode, String msg, Throwable cause) {
    super(msg, cause);
    this.msg = msg;
    this.resultCode = resultCode;
  }

  @Override
  public String toString() {
    return "AuthException{" +
            "resultCode=" + resultCode +
            ", msg='" + msg + '\'' +
            '}';
  }

  public ResultCode getResultCode() {
    return resultCode;
  }

  public String getMsg() {
    return msg;
  }
}
