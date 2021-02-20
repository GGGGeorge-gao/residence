package com.anju.residence.enums;

/**
 * 微信异常码
 * @author cygao
 * @date 2021/2/20 11:02 上午
 **/
public enum WechatErrCode {
  /**
   *
   */
  SUCCESS(0),

  BUSY_WECHAT_SERVER(-1),

  INVALID_JS_CODE(40029),

  REQUEST_TOO_FREQUENT(45011);

  private final int code;

  WechatErrCode(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }
}
