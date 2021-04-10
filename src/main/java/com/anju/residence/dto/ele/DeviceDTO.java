package com.anju.residence.dto.ele;

import com.anju.residence.annotation.ExceptionCode;
import com.anju.residence.entity.ele.Device;
import com.anju.residence.enums.ResultCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * @author cygao
 * @date 2020/12/17 18:42
 **/
@ApiModel(description = "设备传输实体类")
@Data
@AllArgsConstructor
public class DeviceDTO {

  @ApiModelProperty(value = "用户id", name = "userId", required = true)
  @ExceptionCode(resultCode = ResultCode.USER_ERROR)
  @NotNull(message = "user ID must not be null")
  private Integer userId;

  @ApiModelProperty(value = "插孔id", name = "jackId", required = true)
  @ExceptionCode(resultCode = ResultCode.JACK_ERROR)
  @NotNull(message = "插孔id不能为空")
  private Integer jackId;

  @ApiModelProperty(value = "设备名称", name = "name", required = true)
  @ExceptionCode(resultCode = ResultCode.JACK_ERROR)
  @NotEmpty(message = "插孔名称不能为空")
  private String name;

  @ApiModelProperty(value = "设备类型", name = "type", required = true)
  @ExceptionCode(resultCode = ResultCode.JACK_ERROR)
  @NotNull(message = "插孔状态不能为空")
  private String type;

  public Device buildDevice() {
    return Device.builder()
            .name(name)
            .type(type)
            .build();
  }

  public void putDevice(Device device) {
    device.setType(type);
    device.setName(name);
  }

}
