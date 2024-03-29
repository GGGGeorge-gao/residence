package com.anju.residence.dto.wx;

import com.anju.residence.annotation.ExceptionCode;
import com.anju.residence.entity.WxUser;
import com.anju.residence.enums.ResultCode;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author cygao
 * @date 2021/2/19 10:10 下午
 **/
@ApiModel(description = "微信用户传输实体类")
@Data
public class WxUserDTO {

  @ExceptionCode(resultCode = ResultCode.WECHAT_ERROR)
  @NotNull(message = "openId不能为空")
  private String openId;

  private String country;

  private String city;

  private String province;

  private String avatarUrl;

  private Integer gender;

  private String skey;

  private String sessionKey;

  @ExceptionCode(resultCode = ResultCode.WECHAT_ERROR)
  @NotNull(message = "用户名称不能为空")
  private String nickName;

  public WxUser buildWxUser() {
    return WxUser.builder()
            .openId(openId)
            .country(country)
            .city(city)
            .province(province)
            .avatarUrl(avatarUrl)
            .gender(gender)
            .nickName(nickName)
            .createTime(new Date())
            .lastVisitTime(new Date())
            .build();
  }

  public void updateWxUser(WxUser wxUser) {
    wxUser.setCountry(country);
    wxUser.setCity(city);
    wxUser.setProvince(province);
    wxUser.setGender(gender);
    wxUser.setAvatarUrl(avatarUrl);
    wxUser.setNickName(nickName);
    wxUser.setLastVisitTime(new Date());

  }
}
