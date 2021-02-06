package com.anju.residence.service.impl;

import com.anju.residence.dao.ele.AlertNoticeRepository;
import com.anju.residence.entity.ele.AlertNotice;
import com.anju.residence.entity.User;
import com.anju.residence.service.ele.AlertNoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author cygao
 * @date 2020/11/23 23:10
 **/
@Slf4j
@Service
public class AlertNoticeServiceImpl implements AlertNoticeService {

  private final AlertNoticeRepository alertRepo;

  @Autowired
  public AlertNoticeServiceImpl(AlertNoticeRepository alertRepo) {
    this.alertRepo = alertRepo;
  }

  @Override
  public boolean setEmailAuth(User user, String emailAuth) {
    return setInfo(user, emailAuth, null);
  }

  @Override
  public boolean setDingToken(User user, String dingToken) {
    return setInfo(user, null, dingToken);
  }

  @Override
  public boolean setEmailAuthAndDingToken(User user, String emailAuth, String dingToken) {
    return setInfo(user, emailAuth, dingToken);
  }

  private boolean setInfo(User user, String email, String dingToken) {
    if (user == null) {
      return false;
    }
    AlertNotice info = alertRepo.existsByUser(user) ? alertRepo.findByUser(user) : AlertNotice.builder().user(user).build();

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
