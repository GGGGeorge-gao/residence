package com.anju.residence.dto;

import com.anju.residence.annotation.ExceptionCode;
import com.anju.residence.entity.User;
import com.anju.residence.enums.ResultCode;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

/**
 * @author cygao
 * @date 2020/12/15 15:16
 **/
@Data
public class UserDTO {

  @ExceptionCode(resultCode = ResultCode.USERNAME_NOT_VALID)
  @Size(min = 2, max = 40, message = "username length should be 2-40")
  private String username;

  private String password;

  private Integer gender;

  private String phone;

  @ExceptionCode(resultCode = ResultCode.USER_EMAIL_NOT_VALID)
  @Email(message = "incorrect email format")
  private String email;

  private String address;

  public void putUser(User user) {
    user.setUsername(username);
    user.setPassword(password);
    user.setGender(gender);
    user.setPhone(phone);
    user.setEmail(email);
    user.setAddress(address);
  }

  public User buildUser() {
    return User.builder()
            .username(username)
            .password(password)
            .gender(gender)
            .phone(phone)
            .email(email)
            .address(address)
            .build();
  }
}
