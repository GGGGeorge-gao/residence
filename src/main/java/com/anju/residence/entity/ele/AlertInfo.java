package com.anju.residence.entity.ele;

import com.alibaba.fastjson.annotation.JSONField;
import com.anju.residence.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * 预警提示信息实体类
 *
 * @author cygao
 * @date 2020/11/27 16:17
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "alert_info")
public class AlertInfo implements Serializable {

  private static final long serialVersionUID = 915253072509587603L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  @Temporal(TemporalType.TIMESTAMP)
  private Date time;

  private Integer type;

  private Integer isFinished = 0;

  @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
  private User user;

  @ManyToOne(targetEntity = Device.class, fetch = FetchType.LAZY)
  private Device device;

  @ManyToOne(targetEntity = Jack.class, fetch = FetchType.LAZY)
  private Jack jack;

  @ManyToOne(targetEntity = Scene.class, fetch = FetchType.LAZY)
  private Scene scene;

  private String content;
}
