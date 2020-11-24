package com.anju.residence.service;

import com.anju.residence.dao.AlertInfoRepository;
import com.anju.residence.entity.AlertInfo;
import com.anju.residence.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author cygao
 * @date 2020/11/23 23:10
 **/
@Slf4j
@Service
public class AlertService {

  private final AlertInfoRepository alertRepo;

  @Autowired
  public AlertService(AlertInfoRepository alertRepo) {
    this.alertRepo = alertRepo;
  }

  public boolean setEmailAuth(User user, String emailAuth) {
    return setInfo(user, emailAuth, null);
  }

  public boolean setDingToken(User user, String dingToken) {
    return setInfo(user, null, dingToken);
  }

  public boolean setEmailAuthAndDingToken(User user, String emailAuth, String dingToken) {
    return setInfo(user, emailAuth, dingToken);
  }

  private boolean setInfo(User user, String email, String dingToken) {
    if (user == null) {
      return false;
    }
    AlertInfo info = alertRepo.existsByUser(user) ? alertRepo.findByUser(user) : AlertInfo.builder().user(user).build();

    if (email != null) {
      log.info("User:{} emailAuth {} ====> {}", user.getUsername(), info.getEmailAuth(), email);
      info.setEmailAuth(email);
    }
    if (dingToken != null) {
      log.info("User:{} dingToken {} ====> {}", user.getUsername(), info.getDingToken(), dingToken);
      info.setDingToken(dingToken);
    }
    return true;
  }



}
