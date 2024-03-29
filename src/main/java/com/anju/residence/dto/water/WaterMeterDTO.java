package com.anju.residence.dto.water;

import com.anju.residence.annotation.ExceptionCode;
import com.anju.residence.entity.User;
import com.anju.residence.entity.water.WaterMeter;
import com.anju.residence.enums.ResultCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author cygao
 * @date 2021/1/25 14:44
 **/
@ApiModel(description = "水表传输实体类")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WaterMeterDTO {

  @ApiModelProperty(value = "水表id", name = "id")
  private Integer id;

  @ApiModelProperty(value = "水表名", name = "name", required = true)
  @ExceptionCode(resultCode = ResultCode.WATER_METER_ERROR)
  @NotEmpty(message = "水表名不能为空")
  private String name;

  @ApiModelProperty(value = "用户id", name = "userId", required = true)
  @ExceptionCode(resultCode = ResultCode.USER_ERROR)
  @NotNull(message = "Water meter user id must not be null")
  private Integer userId;

  @ApiModelProperty(value = "水表状态", name = "status", required = true)
  @ExceptionCode(resultCode = ResultCode.WATER_METER_ERROR)
  @NotNull(message = "水表状态不能为空")
  private Integer status;

  @ApiModelProperty(value = "当前水表读数", name = "currentCount", required = true)
  @ExceptionCode(resultCode = ResultCode.WATER_METER_ERROR)
  @NotNull(message = "当前水表读数不能为空")
  private String currentCount;

  @ApiModelProperty(value = "采集间隔（单位：分钟）", name = "collectIntervalMin", required = true)
  @ExceptionCode(resultCode = ResultCode.WATER_METER_ERROR)
  @NotNull(message = "水表采集间隔（单位：分钟）不能为空")
  private Integer collectIntervalMin;

  @ApiModelProperty(value = "设备描述", name = "description")
  private String description;

  public WaterMeter build() {
    return WaterMeter.builder()
            .id(id)
            .user(User.builder().id(userId).build())
            .name(name)
            .status(status)
            .currentCount(new BigDecimal(currentCount))
            .description(description)
            .build();
  }

  public static WaterMeterDTO build(WaterMeter waterMeter) {
    return WaterMeterDTO.builder()
            .id(waterMeter.getId())
            .name(waterMeter.getName())
            .status(waterMeter.getStatus())
            .userId(waterMeter.getUser().getId())
            .currentCount(waterMeter.getCurrentCount().toString())
            .description(waterMeter.getDescription())
            .build();
  }
}
