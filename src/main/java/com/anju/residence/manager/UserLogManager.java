package com.anju.residence.manager;

import com.anju.residence.dao.UserLogRepository;
import com.anju.residence.entity.UserLog;
import com.anju.residence.entity.User;
import com.anju.residence.util.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author cygao
 * @date 2020/11/25 20:49
 **/
@Service
public class UserLogManager {

  private final UserLogRepository loginInfoRepo;

  @Autowired
  public UserLogManager(UserLogRepository loginInfoRepo) {
    this.loginInfoRepo = loginInfoRepo;
  }

  /**
   * 添加一条登陆信息
   * @param userId 用户id
   * @param request 请求
   */
  public void addUserLog(int userId, HttpServletRequest request) {
    UserLog info = UserLog.builder().user(User.builder().id(userId).build()).ip(HttpUtil.getRealIp(request)).time(new Date()).build();
    save(info);
  }

  /**
   * 保存登陆信息
   *
   * @param userLog 登陆信息实体类对象
   */
  public void save(UserLog userLog) {
    loginInfoRepo.save(userLog);
  }


}
