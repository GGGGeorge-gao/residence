package com.anju.residence.dto.ele;

import com.anju.residence.annotation.ExceptionCode;
import com.anju.residence.entity.ele.Scene;
import com.anju.residence.enums.ResultCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author cygao
 * @date 2020/12/22 13:54
 **/
@ApiModel(description = "场景传输实体类")
@Builder
@Data
public class SceneDTO {

  @ApiModelProperty(value = "场景id", name = "id")
  private Integer id;

  @ApiModelProperty(value = "场景的父id", name = "parentId")
  private Integer parentId;

  @ApiModelProperty(value = "用户id", name = "userId", required = true)
  @ExceptionCode(resultCode = ResultCode.USER_ID_IS_NULL)
  @NotNull(message = "user ID must not be null")
  private Integer userId;

  @ApiModelProperty(value = "场景名称", name = "name", required = true)
  @ExceptionCode
  @NotEmpty(message = "name must not be null")
  private String name;

  @ApiModelProperty(value = "场景描述", name = "description")
  private String description;

  @ApiModelProperty(value = "子场景, 类型为：SceneDTO", name = "sons")
  private List<SceneDTO> sons;

  public static Scene buildScene(SceneDTO sceneDTO) {
    return Scene.builder()
            .id(sceneDTO.id)
            .parentId(sceneDTO.parentId)
            .name(sceneDTO.name)
            .description(sceneDTO.description)
            .build();
  }

  public static SceneDTO build(Scene scene) {
    return SceneDTO.builder()
            .id(scene.getId())
            .parentId(scene.getParentId())
            .userId(scene.getUser().getId())
            .name(scene.getName())
            .description(scene.getDescription())
            .build();
  }

  public void putScene(Scene scene) {
    scene.setParentId(parentId);
    scene.setName(name);
    scene.setDescription(description);
  }

}
