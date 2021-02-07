package com.anju.residence.enums;

import com.anju.residence.security.jwt.JwtProperty;
import io.swagger.annotations.ApiModel;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
   */
  SUCCESS(1000, "success"),

  INVALID_ARGUMENT(1001, "Invalid argument"),

  ARGUMENT_POSITIVE(1002, "Argument must not be positive"),

  ARGUMENT_POSITIVE_OR_ZERO(1003, "Argument must be negative"),

  ARGUMENT_NEGATIVE(1004, "Argument must not be negative"),

  ARGUMENT_NEGATIVE_OR_ZERO(1005, "Argument must be positive"),

  ARGUMENT_ZERO(1006, "Argument must not be zero"),

  ARGUMENT_NULL(1007, "Argument must not be null"),

  ARGUMENT_NEGATIVE_OR_NULL(1008, "Argument must not be zero and negative"),

  INVALID_DATE(1009, "Date is not valid"),

  DATE_IS_NULL(1010, "Date must not be null"),

  INVALID_CONTENT_TYPE(1016, "Invalid content-type"),


  USER_NOT_FOUND(1100, "User not found"),

  USERNAME_NOT_EXISTS(1110, "Username does not exist"),

  USERNAME_ALREADY_EXISTS(1111, "A user has already registered for this username, please try again with another one"),

  USERNAME_NOT_VALID(1112, "Username is not valid"),

  USER_ID_NOT_EXISTS(1120, "User id does not exist"),

  USER_ID_IS_NULL(1121, "User id must not be null user is not null"),

  USER_PASSWORD_IS_NULL(1130, "User password must not be null"),

  USER_EMAIL_NOT_VALID(1140, "User email is not valid"),


  DEVICE_ID_NOT_EXISTS(1200, "Device id does not exist"),

  DEVICE_ID_IS_NULL(1201, "Device id must not be null"),

  DEVICE_USER_MISMATCH(1202, "This device does not belong to the user"),

  DEVICE_STATUS_IS_NULL(1203, "Device status must not be null"),


  NOT_ELECTRIC_LOG_EXISTS(1300, "Not any electric log found"),

  NOT_REALTIME_ELECTRIC_LOG_EXISTS(1301, "Not realtime electric log exists"),


  JACK_ID_NOT_EXISTS(1400, "Jack id does not exist"),

  JACK_USER_MISMATCH(1401, "This jack does not belong to the user"),

  JACK_RECEPTACLE_MISMATCH(1402, "This jack does not belong to the receptacle"),

  JACK_ID_IS_NULL(1403, "Jack id must not be null"),

  JACK_NAME_NOT_VALID(1404, "Jack name is not valid"),

  JACK_TYPE_NOT_VALID(1405, "Jack type is not valid"),

  JACK_ID_NEGATIVE(1406, "Jack id must be positive"),

  JACK_STATUS_IS_NULL(1407, "Jack status must not be null"),


  RECEPTACLE_ID_NOT_EXISTS(1500, "Receptacle id does not exist"),

  RECEPTACLE_ID_IS_NULL(1501, "Receptacle id must not be null"),

  INVALID_RECEPTACLE_ID(1502, "Receptacle id is not valid"),


  SCENE_ID_NOT_EXISTS(1600, "Scene id does not exist"),

  SCENE_ID_IS_NULL(1601, "Scene id must not be null"),

  SCENE_NAME_ALREADY_EXISTS(1602, "This user already has a scene with the same name, please try again with a different name"),

  SCENE_USER_MISMATCH(1603, "This scene does not belong to the user"),

  INVALID_PARENT_SCENE_ID(1604, "Scene id is not valid"),


  RECEPTACLE_LOG_ID_NULL(1700, "Receptacle log id must not be null"),



  UNAUTHORIZED_REQUEST(4000, "Unauthorized request"),

  INSUFFICIENT_AUTHORITY(4001, "Insufficient authority"),

  INVALID_TOKEN_SIGNATURE(4002, "Invalid jwt signature"),

  EXPIRED_TOKEN(4003, "The token is expired"),

  INVALID_TOKEN_FORMAT(4005, "Wrong format of the token"),

  INVALID_TOKEN_START_WITH(4004, "The token should start with '" + JwtProperty.TOKEN_START_WITH + "'"),

  WRONG_USERNAME_OR_PASSWORD(4005, "Wrong username or password"),

  AUTHENTICATION_FAILURE(4007, "Authentication failure"),


  WATER_METER_ID_IS_NULL(5001, "Water meter id must not be null"),

  WATER_METER_COUNT_IS_NULL(5002, "Water meter count must not be null"),

  WATER_METER_NAME_IS_NULL(5003, "Water meter name must not be null"),

  WATER_METER_USER_ID_IS_NULL(5004, "Water meter user id must not be null"),

  WATER_METER_ID_NOT_EXISTS(5005, "Water meter does not exist"),

  PRICE_PER_CUBIC_METER_IS_NULL(5006, "The price per cubic meter of water cannot be empty"),


  WATER_RECORD_LOG_ID_NOT_EXISTS(5100, "water record log does not exits"),


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
