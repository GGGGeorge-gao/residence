package com.anju.residence.dto.ele;

import com.alibaba.fastjson.annotation.JSONField;
import com.anju.residence.annotation.ExceptionCode;
import com.anju.residence.entity.ele.ElectricLog;
import com.anju.residence.enums.ResultCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author cygao
 * @date 2020/12/15 21:01
 **/
@ApiModel(description = "插座耗电日志传输实体类")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EleLogDTO {

  @ApiModelProperty(value = "时间", name = "time", required = true)
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  @ExceptionCode(resultCode = ResultCode.INVALID_ARGUMENT)
  @NotNull(message = "时间不能为空")
  private Date time;

  @ApiModelProperty(value = "设备id", name = "deviceId", required = true)
  @ExceptionCode(resultCode = ResultCode.DEVICE_ERROR)
  @NotNull(message = "设备id不能为空")
  private Integer deviceId;

  @ApiModelProperty(value = "功率", name = "power", required = true)
  @ExceptionCode(resultCode = ResultCode.INVALID_ARGUMENT)
  @NotNull(message = "功率应为正整数")
  @PositiveOrZero(message = "power must be positive or zero")
  private Integer power;

  @ApiModelProperty(value = "耗电量", name = "consumption", required = true)
  @ExceptionCode(resultCode = ResultCode.INVALID_ARGUMENT)
  @NotNull(message = "耗电量应为正整数")
  @PositiveOrZero(message = "consumption must be positive or zero")
  private Integer consumption;

  @ApiModelProperty(value = "其他信息", name = "others")
  private String others;

  public ElectricLog createElectricLog() {
    return ElectricLog.builder()
            .time(time)
            .power(power)
            .consumption(consumption)
            .others(others)
            .build();
  }

  public static EleLogDTO build(ElectricLog log) {
    return EleLogDTO.builder()
            .time(log.getTime())
            .deviceId(log.getDevice().getId())
            .power(log.getPower())
            .consumption(log.getConsumption())
            .build();
  }
}
