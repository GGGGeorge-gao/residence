package com.anju.residence.vo;

import com.anju.residence.enums.ResultCode;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author cygao
 * @date 2020/12/9 9:54
 **/
@Getter
public class ResultVO<T> implements Serializable {

  /**
   * 状态码
   */
  private Integer code;

  /**
   * 提示信息
   */
  private String msg;

  /**
   * 返回内容
   */
  private T data;

  public ResultVO(T data) {
    this(ResultCode.SUCCESS, data);
  }

  public ResultVO(Integer code, String msg, T data) {
    this.code = code;
    this.msg = msg;
    this.data = data;
  }

  public ResultVO(ResultCode resultCode, T data) {
    this.code = resultCode.getCode();
    this.msg = resultCode.getMsg();
    this.data = data;
  }

  public ResultVO(ResultCode resultCode, String msg) {
    this.code = resultCode.getCode();
    this.msg = msg;
    this.data = null;
  }

  @Override
  public String toString() {
    return "ResultVO{" +
            "code=" + code +
            ", msg='" + msg + '\'' +
            ", data=" + data +
            '}';
  }
}
