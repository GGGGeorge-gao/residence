package com.anju.residence.dto.wx;

import com.anju.residence.security.model.WxSession;
import lombok.Data;

/**
 * @author cygao
 * @date 2021/2/17 3:32 下午
 **/
@Data
public class WxSessionResponse {

  private String openid;

  private String session_key;

  private String unionid;

  private Integer errcode;

  private String errmsg;

  public WxSession buildSession() {
    return WxSession.builder()
            .openId(openid)
            .sessionKey(session_key)
            .unionId(unionid)
            .build();
  }
}
