package com.anju.residence.security.model;

import lombok.Builder;
import lombok.Data;

/**
 * 微信用户维持登录状态
 *
 * @author cygao
 * @date 2021/2/18 11:15 上午
 **/
@Builder
@Data
public class WxSession {

  private String openId;

  private String skey;

  private String sessionKey;

  private String unionId;
}
