package com.anju.residence.service;

import com.anju.residence.dao.LoginInfoRepository;
import com.anju.residence.entity.LoginInfo;
import com.anju.residence.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author cygao
 * @date 2020/11/25 20:49
 **/
@Service
public class LoginInfoService {

  private static final String UNKNOWN_IP = "unknown";

  private final LoginInfoRepository loginInfoRepo;

  @Autowired
  public LoginInfoService(LoginInfoRepository loginInfoRepo) {
    this.loginInfoRepo = loginInfoRepo;
  }

  /**
   * 添加一条登陆信息
   * @param userId 用户id
   * @param request 请求
   */
  public void addLoginInfo(int userId, HttpServletRequest request) {
    LoginInfo info = LoginInfo.builder().user(User.builder().id(userId).build()).ip(getRealIp(request)).loginTime(new Date()).build();
    save(info);
  }

  /**
   * 保存登陆信息
   *
   * @param loginInfo 登陆信息实体类对象
   */
  void save(LoginInfo loginInfo) {
    loginInfoRepo.save(loginInfo);
  }

  /**
   * 获取请求真实的id
   * @param request 请求
   * @return IP地址
   */
  public static String getRealIp(HttpServletRequest request) {
    String ip = request.getHeader("x-forwarded-for");
    if (ip == null || ip.length() == 0 || UNKNOWN_IP.equalsIgnoreCase(ip)) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || UNKNOWN_IP.equalsIgnoreCase(ip)) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || UNKNOWN_IP.equalsIgnoreCase(ip)) {
      ip = request.getHeader("HTTP_CLIENT_IP");
    }
    if (ip == null || ip.length() == 0 || UNKNOWN_IP.equalsIgnoreCase(ip)) {
      ip = request.getHeader("HTTP_X_FORWARDED_FOR");
    }
    if (ip == null || ip.length() == 0 || UNKNOWN_IP.equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr();
    }
    return ip;
  }
}
