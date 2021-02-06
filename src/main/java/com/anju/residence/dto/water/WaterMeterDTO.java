package com.anju.residence.dto.water;

import com.anju.residence.annotation.ExceptionCode;
import com.anju.residence.entity.water.WaterMeter;
import com.anju.residence.enums.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author cygao
 * @date 2021/1/25 14:44
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WaterMeterDTO {

  private Integer id;

  @ExceptionCode(resultCode = ResultCode.WATER_METER_NAME_IS_NULL)
  @NotEmpty(message = "Water meter name must not be null")
  private String name;

  @ExceptionCode(resultCode = ResultCode.USER_ID_IS_NULL)
  @NotNull(message = "Water meter user id must not be null")
  private Integer userId;

  private Integer status;

  @ExceptionCode(resultCode = ResultCode.WATER_METER_COUNT_IS_NULL)
  @NotNull(message = "Water meter currentCount must not be null")
  private Double currentCount;

  private String description;

  public WaterMeter build() {
    return WaterMeter.builder()
            .id(id)
            .name(name)
            .status(status)
            .currentCount(currentCount)
            .description(description)
            .build();
  }

  public static WaterMeterDTO build(WaterMeter waterMeter) {
    return WaterMeterDTO.builder()
            .id(waterMeter.getId())
            .name(waterMeter.getName())
            .status(waterMeter.getStatus())
            .userId(waterMeter.getUser().getId())
            .currentCount(waterMeter.getCurrentCount())
            .description(waterMeter.getDescription())
            .build();
  }
}
