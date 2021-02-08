package com.anju.residence.dto;

import com.anju.residence.annotation.ExceptionCode;
import com.anju.residence.entity.User;
import com.anju.residence.entity.UserPrice;
import com.anju.residence.enums.ResultCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author cygao
 * @date 2021/2/8 10:12 上午
 **/
@ApiModel(description = "用户价格传输实体类")
@Builder
@Data
public class UserPriceDTO {

  @ApiModelProperty(value = "用户id", name = "userId", required = true)
  @ExceptionCode(resultCode = ResultCode.USER_ID_IS_NULL)
  @NotNull(message = "user id must not be null")
  private Integer userId;

  @ApiModelProperty(value = "每度电价格", name = "elePrice")
  private BigDecimal elePrice;

  @ApiModelProperty(value = "每立方米水价格", name = "waterPrice")
  private BigDecimal waterPrice;

  public UserPrice build() {
    return UserPrice.builder()
            .user(User.builder().id(userId).build())
            .elePrice(elePrice)
            .waterPrice(waterPrice)
            .build();
  }

  public void putUserPrice(UserPrice price) {
    price.setElePrice(elePrice);
    price.setWaterPrice(waterPrice);
  }

  public static UserPriceDTO build(UserPrice userPrice) {
    return UserPriceDTO.builder()
            .userId(userPrice.getUser().getId())
            .elePrice(userPrice.getElePrice())
            .waterPrice(userPrice.getWaterPrice())
            .build();
  }
}
