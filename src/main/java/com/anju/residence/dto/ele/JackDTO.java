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

  @ExceptionCode(resultCode = ResultCode.JACK_NAME_NOT_VALID)
  @NotEmpty(message = "Jack name must not be null")
  private String name;

  @ExceptionCode(resultCode = ResultCode.JACK_STATUS_IS_NULL)
  @NotNull(message = "Jack status must not be null")
  private Integer status;

  @ExceptionCode(resultCode = ResultCode.JACK_TYPE_NOT_VALID)
  @NotNull(message = "Jack type must not be null")
  private Integer type;

  private Integer receptacleId;

  public Jack build(Receptacle receptacle) {
    if (!receptacleId.equals(receptacle.getId())) {
      throw new ApiException(ResultCode.INVALID_RECEPTACLE_ID);
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
