package com.anju.residence.service.impl;

import com.anju.residence.dao.LoginInfoRepository;
import com.anju.residence.entity.LoginInfo;
import com.anju.residence.service.LoginInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author cygao
 * @date 2020/11/24 16:16
 **/
@Service
public class LoginInfoServiceImpl implements LoginInfoService {

  private final LoginInfoRepository loginInfoRepo;

  @Autowired
  public LoginInfoServiceImpl(LoginInfoRepository loginInfoRepo) {
    this.loginInfoRepo = loginInfoRepo;
  }

  @Override
  public void save(LoginInfo info) {
    loginInfoRepo.save(info);
  }
}
