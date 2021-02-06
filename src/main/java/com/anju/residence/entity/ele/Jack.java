package com.anju.residence.entity.ele;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
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
 * @author cygao
 * @date 2020/11/22 9:43
 * 插座插孔实体类
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "jack")
public class Jack {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(targetEntity = Receptacle.class, fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
  @JoinColumn
  private Receptacle receptacle;

  private String name;

  private Integer type;

  private Integer status;

  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date lastUsedTime;
}
