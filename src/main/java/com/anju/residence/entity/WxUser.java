package com.anju.residence.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "wx_user")
public class WxUser {

  @Id
  @Column(name = "open_id")
  private String openId;
  @Column(name = "skey")
  private String skey;
  @Column(name = "create_time")
  private Date createTime;
  @Column(name = "last_visit_time")
  private Date lastVisitTime;
  @Column(name = "session_key")
  private String sessionKey;
  @Column(name = "city")
  private String city;
  @Column(name = "province")
  private String province;
  @Column(name = "country")
  private String country;
  @Column(name = "avatar_url")
  private String avatarUrl;
  @Column(name = "gender")
  private long gender;
  @Column(name = "nick_name")
  private String nickName;

  @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id", updatable = false, unique = true)
  private User user;
}
