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
import java.util.Date;

/**
 * 用户行为日志实体类，例如打开设备
 *
 * @author cygao
 * @date 2020/11/27 16:37
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "behavior_log")
public class BehaviorLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date time;

  @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
  private User user;

  @ManyToOne(targetEntity = Jack.class, fetch = FetchType.LAZY)
  private Jack jack;

  @ManyToOne(targetEntity = Device.class, fetch = FetchType.LAZY)
  private Device device;

  private Integer deviceStatus;

  private Integer jackStatus;
}
