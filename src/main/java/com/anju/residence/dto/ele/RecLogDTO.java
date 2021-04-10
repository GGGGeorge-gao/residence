package com.anju.residence.dto.ele;

import com.alibaba.fastjson.annotation.JSONField;
import com.anju.residence.annotation.ExceptionCode;
import com.anju.residence.entity.ele.ReceptacleLog;
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
 * @date 2020/12/22 12:59
 **/
@ApiModel(description = "插座用电日志传输实体类")
@Builder
@Data
public class RecLogDTO {

  @ApiModelProperty(value = "日志id", name = "id")
  private Integer id;

  @ApiModelProperty(value = "插座id", name = "receptacleId", required = true)
  @ExceptionCode(resultCode = ResultCode.RECEPTACLE_ERROR)
  @NotNull(message = "插座id不能为空")
  private Integer receptacleId;

  @ApiModelProperty(value = "耗电量", name = "consumption", required = true)
  @ExceptionCode(resultCode = ResultCode.INVALID_ARGUMENT)
  @NotNull(message = "耗电量不能为空")
  @PositiveOrZero(message = "耗电量应为正整数")
  private Integer consumption;

  @ApiModelProperty(value = "日期", name = "time", notes = "格式为 yyyy-MM-dd", required = true)
  @JSONField(format = "yyyy-MM-dd")
  @ExceptionCode(resultCode = ResultCode.INVALID_ARGUMENT)
  @NotNull(message = "日期不能为空")
  private Date time;

  @ApiModelProperty(value = "其他信息", name = "others")
  private String others;

  public static RecLogDTO build(ReceptacleLog receptacleLog, int receptacleId) {
    return RecLogDTO.builder()
            .id(receptacleLog.getId())
            .receptacleId(receptacleId)
            .consumption(receptacleLog.getConsumption())
            .time(receptacleLog.getTime())
            .others(receptacleLog.getOthers())
            .build();
  }

  public ReceptacleLog build() {
    return ReceptacleLog.builder()
            .id(id)
            .consumption(consumption)
            .time(time)
            .others(others)
            .build();
  }
}
