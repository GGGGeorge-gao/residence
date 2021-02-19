package com.anju.residence.util;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.anju.residence.dto.wx.WxSessionResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cygao
 * @date 2021/2/19 02:27 下午
 */
public class WechatUtil {

  private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
  public static final String CODE_2_SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session";

  public static WxSessionResponse getSessionKeyOrOpenId(String code) {
    Map<String, Object> requestUrlParam = new HashMap<>(4);
    //小程序appId
    requestUrlParam.put("appid", WechatParam.AppId);
    //小程序secret
    requestUrlParam.put("secret", WechatParam.AppSecret);
    //小程序端返回的code
    requestUrlParam.put("js_code", code);
    //默认参数
    requestUrlParam.put("grant_type", "authorization_code");
    //发送post请求读取调用微信接口获取openid用户唯一标识
    return JSON.parseObject(HttpUtil.post(CODE_2_SESSION_URL, requestUrlParam), WxSessionResponse.class);
//    return JSON.parseObject(HttpUtil.post(requestUrl, requestUrlParam));
  }


}

