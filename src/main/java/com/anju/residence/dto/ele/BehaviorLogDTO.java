package com.anju.residence.dto.ele;

import com.alibaba.fastjson.annotation.JSONField;
import com.anju.residence.annotation.ExceptionCode;
import com.anju.residence.entity.ele.BehaviorLog;
import com.anju.residence.entity.ele.Device;
import com.anju.residence.entity.ele.Jack;
import com.anju.residence.entity.User;
import com.anju.residence.enums.ResultCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author cygao
 * @date 2020/12/26 11:24
 **/
@ApiModel(description = "操作日志传输实体类")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BehaviorLogDTO {

  @ApiModelProperty(value = "日志id", name = "id")
  private Integer id;

  @ApiModelProperty(value = "用户id", name = "userId", required = true)
  @ExceptionCode(resultCode = ResultCode.USER_ID_IS_NULL)
  @NotNull(message = " must not be null")
  private Integer userId;

  @ApiModelProperty(value = "插孔id", name = "jackId", required = true)
  @ExceptionCode(resultCode = ResultCode.JACK_ID_IS_NULL)
  @NotNull(message = " must not be null")
  private Integer jackId;

  @ApiModelProperty(value = "插孔状态", name = "jackStatus", notes = "0为关，1为开", required = true)
  @ExceptionCode(resultCode = ResultCode.JACK_STATUS_IS_NULL)
  @NotNull(message = " must not be null")
  private Integer jackStatus;

  @ApiModelProperty(value = "设备id", name = "deviceId")
  private Integer deviceId;

  @ApiModelProperty(value = "设备状态", name = "deviceStatus")
  private Integer deviceStatus;

  @ApiModelProperty(value = "时间", name = "time", required = true)
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  @ExceptionCode(resultCode = ResultCode.DATE_IS_NULL)
  @NotNull(message = "time must not be null")
  private Date time;

  public BehaviorLog build() {
    BehaviorLog behaviorLog = BehaviorLog.builder()
            .time(time)
            .jackStatus(jackStatus)
            .jack(Jack.builder().id(jackId).build())
            .user(User.builder().id(userId).build())
            .build();
    if (deviceId != null) {
      behaviorLog.setDevice(Device.builder().id(deviceId).build());
      behaviorLog.setDeviceStatus(deviceStatus);
    }
    return behaviorLog;
  }

  public static BehaviorLogDTO buildDTO(BehaviorLog behaviorLog) {
    if (behaviorLog == null) {
      return new BehaviorLogDTO();
    }
    return BehaviorLogDTO.builder()
            .id(behaviorLog.getId())
            .deviceStatus(behaviorLog.getDeviceStatus())
            .jackStatus(behaviorLog.getJackStatus())
            .jackId(behaviorLog.getJack().getId())
            .deviceId(behaviorLog.getDevice().getId())
            .userId(behaviorLog.getUser().getId())
            .time(behaviorLog.getTime())
            .build();
  }
}
