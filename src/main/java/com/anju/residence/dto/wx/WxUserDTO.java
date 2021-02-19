package com.anju.residence.dto.wx;

import com.anju.residence.annotation.ExceptionCode;
import com.anju.residence.entity.WxUser;
import com.anju.residence.enums.ResultCode;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author cygao
 * @date 2021/2/19 10:10 下午
 **/
@Data
public class WxUserDTO {

  @ExceptionCode(resultCode = ResultCode.OPEN_ID_IS_NULL)
  @NotNull(message = "openId不能为空")
  private String openId;

  private String city;

  private String province;

  private String avatarUrl;

  private Integer gender;

  private Date createTime;

  private Date lastVisitTime;

  @ExceptionCode(resultCode = ResultCode.NICKNAME_IS_NULL)
  @NotNull(message = "用户名称不能为空")
  private String nickName;

  public WxUser build() {
    return WxUser.builder()
            .openId(openId)
            .city(city)
            .province(province)
            .avatarUrl(avatarUrl)
            .gender(gender)
            .build();
  }
}
