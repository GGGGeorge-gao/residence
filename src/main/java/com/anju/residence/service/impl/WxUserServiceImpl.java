package com.anju.residence.service.impl;

import com.anju.residence.dao.WxUserRepository;
import com.anju.residence.entity.WxUser;
import com.anju.residence.service.ele.WxUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class WxUserServiceImpl implements WxUserService {
  private final WxUserRepository wxUserRepository;

  @Autowired
  public WxUserServiceImpl(WxUserRepository wxUserRepository) {
    this.wxUserRepository = wxUserRepository;
  }

  @Override
  public WxUser selectUserBySkey(String skey) {
    WxUser user = new WxUser();
    user.setSkey(skey);
    Example<WxUser> example = Example.of(user);
    return wxUserRepository.findOne(example).get();
  }

  @Override
  public WxUser selectUserBySkey(HttpServletRequest request) {
    String skey = request.getHeader("skey");
    return selectUserBySkey(skey);
  }

  @Override
  public WxUser selectUserByOpenId(String openId) {
    return wxUserRepository.getOne(openId);
  }

  @Override
  public void update(WxUser t) {
    wxUserRepository.saveAndFlush(t);
  }

  @Override
  public void insert(WxUser t) {
    wxUserRepository.saveAndFlush(t);
  }
}
