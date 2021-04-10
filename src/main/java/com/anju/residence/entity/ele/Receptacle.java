package com.anju.residence.entity.ele;

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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author cygao
 * @date 2020/11/19 18:12
 * 插座实体表
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "receptacle")
public class Receptacle implements Serializable {

  private static final long serialVersionUID = 1609374185714788828L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
  @JoinColumn
  private User user;

  @ManyToOne(targetEntity = Scene.class, fetch = FetchType.EAGER)
  @JoinColumn
  private Scene scene;

  @Column(nullable = false)
  private String name;

  private Integer status;

  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date lastUsedTime;

  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  @CreatedDate
  @Column(insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  private Date createTime;

  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  @LastModifiedDate
  @Column(columnDefinition = "TIMESTAMP ON UPDATE CURRENT_TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  private Date updateTime;
}
