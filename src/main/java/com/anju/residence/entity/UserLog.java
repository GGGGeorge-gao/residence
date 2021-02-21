package com.anju.residence.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * 用户登录日志实体类
 *
 * @author cygao
 * @date 2020/11/19 18:03
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_log")
public class UserLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
  @JoinColumn
  private User user;

  private Integer type;

  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date time;

  private String ip;

  private String description;
}
