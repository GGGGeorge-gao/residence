package com.anju.residence.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.anju.residence.annotation.ExceptionCode;
import com.anju.residence.enums.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author cygao
 * @date 2020/11/19 17:58
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotNull(message = "username is required")
  @Size(min = 2, max = 16, message = "username length should be 2-16")
  @Column(unique = true)
  @ExceptionCode(resultCode = ResultCode.USERNAME_NOT_VALID)
  private String username;

  @NotNull(message = "password is required")
  @ExceptionCode(resultCode = ResultCode.USER_PASSWORD_IS_NULL)
  private String password;

  /**
   * 用户性别 0-男 1-女
   */
  private Integer gender;

  private String phone;

  @Email(message = "incorrect email format")
  @ExceptionCode(resultCode = ResultCode.USER_EMAIL_NOT_VALID)
  private String email;

  private String address;

  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  @CreatedDate
  @Column(insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  private Date createTime;

  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  @LastModifiedDate
  @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
  private Date updateTime;

  @JoinTable(name = "user_roles",
          joinColumns = {@JoinColumn(name = "user_id")},
          inverseJoinColumns = @JoinColumn(name = "role_id"))
  @ManyToMany(targetEntity = Role.class, cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
  private List<Role> roles = new ArrayList<>();

}
