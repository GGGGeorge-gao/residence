package com.anju.residence.service.ele;

import com.anju.residence.entity.WxUser;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public interface WxUserService {
  WxUser selectUserBySkey(String skey);

  WxUser selectUserBySkey(HttpServletRequest request);

  WxUser selectUserByOpenId(String openId);

  void update(WxUser t);

  void insert(WxUser t);
}
