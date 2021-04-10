package com.anju.residence.entity.ele;

import com.anju.residence.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 预警通知信息实体类
 *
 * @author cygao
 * @date 2020/11/22 9:34
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "alert_notice")
public class AlertNotice implements Serializable {

  private static final long serialVersionUID = -7095459225407327321L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
  @JoinColumn(nullable = false, updatable = false)
  private User user;

  private String emailAuth;

  private String dingToken;
}
