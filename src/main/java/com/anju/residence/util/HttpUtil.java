package com.anju.residence.util;

import javax.servlet.http.HttpServletRequest;

/**
 * @author cygao
 * @date 2021/2/21 12:19 下午
 **/
public class HttpUtil {

  private static final String UNKNOWN_IP = "unknown";

  /**
   * 获取请求真实的id
   * @param request 请求
   * @return IP地址
   */
  public static String getRealIp(HttpServletRequest request) {
    if (request == null) {
      return "";
    }
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
