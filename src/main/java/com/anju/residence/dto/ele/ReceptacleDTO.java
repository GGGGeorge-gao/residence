package com.anju.residence.dto.ele;

import com.anju.residence.annotation.ExceptionCode;
import com.anju.residence.entity.ele.Receptacle;
import com.anju.residence.enums.ResultCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author cygao
 * @date 2020/12/22 13:19
 **/
@ApiModel(description = "插座传输实体类")
@Builder
@Data
public class ReceptacleDTO {

  @ApiModelProperty(value = "插座id", name = "id")
  private Integer id;

  @ApiModelProperty(value = "插座名称", name = "name", required = true)
  @ExceptionCode(resultCode = ResultCode.INVALID_ARGUMENT)
  @NotNull(message = "插座名不能为空")
  private String name;

  @ApiModelProperty(value = "场景id", name = "sceneId", required = true)
  @ExceptionCode(resultCode = ResultCode.SCENE_ERROR)
  @NotNull(message = "场景id不能为空")
  private Integer sceneId;

  @ApiModelProperty(value = "用户id", name = "userId", required = true)
  @ExceptionCode(resultCode = ResultCode.USER_ERROR)
  @NotNull(message = "user id must not be null")
  private Integer userId;

  @ApiModelProperty(value = "插座状态", name = "status", required = true)
  @ExceptionCode(resultCode = ResultCode.INVALID_ARGUMENT)
  @NotNull(message = "插座状态不能为空")
  private Integer status;

  @ApiModelProperty(value = "插孔列表", name = "jacks")
  private List<JackDTO> jacks;

  public static Receptacle buildReceptacle(ReceptacleDTO receptacleDTO) {
    return Receptacle.builder()
            .id(receptacleDTO.id)
            .name(receptacleDTO.name)
            .status(receptacleDTO.status)
            .build();
  }

  public void putReceptacle(Receptacle receptacle) {
    receptacle.setName(name);
    receptacle.setStatus(status);
  }
}
