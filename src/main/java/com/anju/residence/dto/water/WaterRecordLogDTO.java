package com.anju.residence.dto.water;

import com.alibaba.fastjson.annotation.JSONField;
import com.anju.residence.annotation.ExceptionCode;
import com.anju.residence.entity.water.WaterMeter;
import com.anju.residence.entity.water.WaterRecordLog;
import com.anju.residence.enums.ResultCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author cygao
 * @date 2021/1/25 17:36
 **/
@ApiModel(description = "水表抄表日志传输实体类")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WaterRecordLogDTO {

  @ApiModelProperty(value = "日志id", name = "id")
  private Integer id;

  @ApiModelProperty(value = "水表id", name = "waterMeterId", required = true)
  @ExceptionCode(resultCode = ResultCode.WATER_METER_ID_IS_NULL)
  @NotNull(message = "waterMeter id must not be null")
  private Integer waterMeterId;

  @ApiModelProperty(value = "抄表时间", name = "time", required = true)
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  @ExceptionCode(resultCode = ResultCode.DATE_IS_NULL)
  private Date time;

  @ApiModelProperty(value = "抄表计数", name = "count", required = true)
  @ExceptionCode(resultCode = ResultCode.WATER_METER_COUNT_IS_NULL)
  @NotNull(message = "count must not be null")
  private String count;

  @ApiModelProperty(value = "其他信息", name = "others")
  private String others;

  public WaterRecordLog build() {
    return WaterRecordLog.builder()
            .waterMeter(WaterMeter.builder().id(waterMeterId).build())
            .id(id)
            .time(time)
            .count(new BigDecimal(count))
            .others(others)
            .build();
  }

  public static WaterRecordLogDTO build(WaterRecordLog recordLog) {
    return WaterRecordLogDTO.builder()
            .id(recordLog.getId())
            .waterMeterId(recordLog.getWaterMeter().getId())
            .count(recordLog.getCount().toString())
            .time(recordLog.getTime())
            .others(recordLog.getOthers())
            .build();
  }


}
