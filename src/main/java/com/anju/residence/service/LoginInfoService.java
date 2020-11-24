package com.anju.residence.service;

import com.anju.residence.dao.LoginInfoRepository;
import com.anju.residence.entity.LoginInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author cygao
 * @date 2020/11/24 16:16
 **/
@Service
public class LoginInfoService {

  private final LoginInfoRepository loginInfoRepo;

  @Autowired
  public LoginInfoService(LoginInfoRepository loginInfoRepo) {
    this.loginInfoRepo = loginInfoRepo;
  }

  public void save(LoginInfo info) {
    loginInfoRepo.save(info);
  }
}
