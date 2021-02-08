package com.anju.residence.entity.water;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 水表抄表日志实体类
 *
 * @author cygao
 * @date 2021/1/25 11:57
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "water_record_log", indexes = {@Index(columnList = "time")})
public class WaterRecordLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(targetEntity = WaterMeter.class, fetch = FetchType.LAZY)
  private WaterMeter waterMeter;

  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date time;

  private BigDecimal count;

  private String others;
}
