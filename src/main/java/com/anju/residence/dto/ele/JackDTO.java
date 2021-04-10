package com.anju.residence.dto.ele;

import com.anju.residence.annotation.ExceptionCode;
import com.anju.residence.entity.ele.Jack;
import com.anju.residence.entity.ele.Receptacle;
import com.anju.residence.enums.ResultCode;
import com.anju.residence.exception.ApiException;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author cygao
 * @date 2020/12/24 19:59
 **/
@Builder
@Data
public class JackDTO {

  private Integer id;

  @ExceptionCode(resultCode = ResultCode.JACK_ERROR)
  @NotEmpty(message = "插孔名称不能为空")
  private String name;

  @ExceptionCode(resultCode = ResultCode.JACK_ERROR)
  @NotNull(message = "插孔状态不能为空")
  private Integer status;

  @ExceptionCode(resultCode = ResultCode.JACK_ERROR)
  @NotNull(message = "插孔状态不能为空")
  private Integer type;

  private Integer receptacleId;

  public Jack build(Receptacle receptacle) {
    if (!receptacleId.equals(receptacle.getId())) {
      throw new ApiException(ResultCode.RECEPTACLE_ERROR, "无效的插座id");
    }
    return Jack.builder()
            .id(id)
            .name(name)
            .status(status)
            .type(type)
            .receptacle(receptacle)
            .build();
  }

}
