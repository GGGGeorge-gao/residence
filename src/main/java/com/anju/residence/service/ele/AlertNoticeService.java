package com.anju.residence.service.ele;

import com.anju.residence.entity.User;

/**
 * @author cygao
 * @date 2020/11/25 20:45
 **/
public interface AlertNoticeService {

  /**
   * 设置用户的钉钉token
   */
  boolean setDingToken(User user, String dingToken);

  /**
   * 设置用户的邮箱授权码
   */
  boolean setEmailAuth(User user, String emailAuth);

  /**
   * 设置用户的邮箱授权码和钉钉token
   */
  boolean setEmailAuthAndDingToken(User user, String emailAuth, String dingToken);


}
