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
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * 耗电日志实体类
 *
 * @author cygao
 * @date 2020/11/27 16:42
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "electric_log", indexes = {@Index(columnList = "time")})
public class ElectricLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date time;

  @ManyToOne(targetEntity = Device.class, fetch = FetchType.LAZY)
  private Device device;

  private Integer power;

  private Integer consumption;

  private String others;

}
