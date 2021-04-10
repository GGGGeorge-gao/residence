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
import java.io.Serializable;
import java.util.Date;

/**
 * 设备耗电日志实体类
 *
 * @author cygao
 * @date 2020/11/27 17:09
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "device_log", indexes = {@Index(columnList = "time")})
public class DeviceLog implements Serializable {

  private static final long serialVersionUID = -3363683193302541855L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(targetEntity = Device.class, fetch = FetchType.LAZY)
  private Device device;

  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date time;

  private Integer consumption;

  private String others;


  public void addConsumption(int newConsumption) {
    if (consumption == null) {
      consumption = 0;
    }
    consumption += newConsumption;
  }

}
