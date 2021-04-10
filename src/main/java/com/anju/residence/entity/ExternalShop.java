package com.anju.residence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author cygao
 * @date 2021/3/15
 **/
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "external_shop")
public class ExternalShop implements Serializable {

  private static final long serialVersionUID = 3768971136575600161L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String title;

  private Integer type;

  private BigDecimal price;

  private String summary;

  private String picUrl;

  private String linkUrl;

  private String others;
}
