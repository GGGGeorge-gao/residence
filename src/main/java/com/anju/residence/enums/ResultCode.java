package com.anju.residence.enums;

import com.anju.residence.params.JwtParams;
import io.swagger.annotations.ApiModel;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cygao
 * @date 2020/12/9 10:39
 **/
@ApiModel(description = "异常状态码* 1000-1099 通用状态码\n" +
        "   * 1100-1199 用户状态码\n" +
        "   * 1200-1299 设备状态码\n" +
        "   * 1300-1399 耗电日志状态码\n" +
        "   * 1400-1499 插孔状态码\n" +
        "   * 1500-1599 插座状态码\n" +
        "   * 1600-1699 场景状态码\n" +
        "   * 1700-1799 插座用电日志状态码\n" +
        "   *\n" +
        "   * 5000-5099 水表状态码")
@Getter
public enum ResultCode {
  /**
   * 1000-1099 通用状态码
   * 1100-1199 用户状态码
   * 1200-1299 设备状态码
   * 1300-1399 耗电日志状态码
   * 1400-1499 插孔状态码
   * 1500-1599 插座状态码
   * 1600-1699 场景状态码
   * 1700-1799 插座用电日志状态码
   *
   * 4000-4999 权限认证状态码
   *
   * 5000-5099 水表状态码
   *
   * 6000-6099 微信用户状态码
   *
   * 8000-8099 文件上传状态码
   * 8100-8199 模型状态码
   */
  SUCCESS(1000, "success"),

  INVALID_ARGUMENT(1001, "无效参数"),


  USER_ERROR(1100, "用户类异常"),


  DEVICE_ERROR(1200, "设备类异常"),


  ELECTRIC_LOG_ERROR(1300, "耗电日志类异常"),


  JACK_ERROR(1400, "插孔类异常"),


  RECEPTACLE_ERROR(1500, "插座类异常"),


  SCENE_ERROR(1600, "场景类异常"),


  AUTH_ERROR(4000, "授权异常"),

  UNAUTHORIZED_REQUEST(4001, "未授权的请求！"),

  INSUFFICIENT_AUTHORITY(4002, "权限不足"),

  TOKEN_ERROR(4003, "token异常"),

  WRONG_USERNAME_OR_PASSWORD(4004, "Wrong username or password"),


  WATER_METER_ERROR(5000, "水表类异常"),


  WATER_RECORD_LOG_ERROR(5100, "water record log does not exits"),


  WECHAT_ERROR(6000, "微信异常"),


  FILE_ERROR(8000, "文件上传异常"),


  OCR_ERROR(8100, "OCR模型异常"),


  UNKNOWN_ERROR(9999, "Unknown error");


  private final Integer code;

  private final String msg;

  /**
   * 状态码哈希表
   */
  public static final Map<Integer, ResultCode> RESULT_CODE_MAP;

  /*
    状态码哈希表初始化
   */
  static {
    Map<Integer, ResultCode> map = new HashMap<>();
    Arrays.stream(ResultCode.values()).forEach(resultCode -> map.put(resultCode.getCode(), resultCode));

    RESULT_CODE_MAP = Collections.unmodifiableMap(map);
  }

  public static ResultCode get(int code) {
    return RESULT_CODE_MAP.getOrDefault(code, null);
  }

  ResultCode(Integer code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  @Override
  public String toString() {
    return "ResultCode{" +
            "code=" + code +
            ", msg='" + msg + '\'' +
            '}';
  }
}
