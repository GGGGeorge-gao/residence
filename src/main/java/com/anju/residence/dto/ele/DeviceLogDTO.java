package com.anju.residence.dto.ele;

import com.alibaba.fastjson.annotation.JSONField;
import com.anju.residence.annotation.ExceptionCode;
import com.anju.residence.entity.ele.DeviceLog;
import com.anju.residence.enums.ResultCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.Date;

/**
 * @author cygao
 * @date 2021/1/31 8:22 下午
 **/
@ApiModel(description = "插座用电日志传输实体类")
@Builder
@Data
public class DeviceLogDTO {

  @ApiModelProperty(value = "日志id", name = "id")
  private Integer id;

  @ApiModelProperty(value = "设备id", name = "deviceId", required = true)
  @ExceptionCode(resultCode = ResultCode.DEVICE_ERROR)
  @NotNull(message = "设备id不能为空")
  private Integer deviceId;

  @ApiModelProperty(value = "耗电量", name = "consumption", required = true)
  @ExceptionCode(resultCode = ResultCode.INVALID_ARGUMENT)
  @NotNull(message = "耗电量应为正整数")
  @PositiveOrZero(message = "consumption must be positive or zero")
  private Integer consumption;

  @ApiModelProperty(value = "日期", name = "time", notes = "格式为 yyyy-MM-dd", required = true)
  @JSONField(format = "yyyy-MM-dd")
  @ExceptionCode(resultCode = ResultCode.INVALID_ARGUMENT)
  @NotNull(message = "日期不能为空")
  private Date time;

  @ApiModelProperty(value = "其他信息", name = "others")
  private String others;

  public static DeviceLogDTO build(DeviceLog deviceLog) {
    return DeviceLogDTO.builder()
            .id(deviceLog.getId())
            .deviceId(deviceLog.getDevice().getId())
            .time(deviceLog.getTime())
            .consumption(deviceLog.getConsumption())
            .others(deviceLog.getOthers())
            .build();
  }
}
