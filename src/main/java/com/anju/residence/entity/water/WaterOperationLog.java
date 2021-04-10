package com.anju.residence.entity.water;

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
import java.io.Serializable;
import java.util.Date;

/**
 * @author cygao
 * @date 2021/1/25 11:54
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "water_operation_log")
public class WaterOperationLog implements Serializable {

  private static final long serialVersionUID = -791906810194618738L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date time;

  @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
  private User user;

  @ManyToOne(targetEntity = WaterMeter.class, fetch = FetchType.LAZY)
  private WaterMeter waterMeter;

  private Integer waterMeterStatus;
}
