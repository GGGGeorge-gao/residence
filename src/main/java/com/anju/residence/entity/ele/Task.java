package com.anju.residence.entity.ele;

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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 定时任务实体类
 *
 * @author cygao
 * @date 2020/11/27 17:18
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "task")
public class Task implements Serializable {

  private static final long serialVersionUID = 1327308759627394175L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date createTime;

  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date targetTime;

  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date finishedTime;

  @ManyToOne(targetEntity = Device.class, fetch = FetchType.LAZY)
  private Device device;

  @ManyToOne(targetEntity = Jack.class, fetch = FetchType.LAZY)
  private Jack jack;

  private Integer taskType;

  private Integer isFinished;

  private String others;

}
