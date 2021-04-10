package com.anju.residence.entity.water;

import com.alibaba.fastjson.annotation.JSONField;
import com.anju.residence.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author cygao
 * @date 2021/1/25 11:45
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "water_meter", indexes = {@Index(columnList = "lastRecordTime")})
public class WaterMeter implements Serializable {

  private static final long serialVersionUID = 5109071627623593032L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;

  @ManyToOne(targetEntity = User.class)
  private User user;

  private Integer status;

  @Column(length = 12, scale = 5)
  private BigDecimal currentCount;

  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date lastRecordTime;

  /**
   * 设置的采集间隔时间
   */
  private Integer collectIntervalMin;

  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  @LastModifiedDate
  @Column(columnDefinition = "TIMESTAMP ON UPDATE CURRENT_TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  private Date updateTime;

  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  @CreatedDate
  @Column(insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  private Date createTime;

  private String description;
}
