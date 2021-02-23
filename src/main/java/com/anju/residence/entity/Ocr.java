package com.anju.residence.entity;

import com.anju.residence.entity.water.WaterMeter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author cygao
 * @date 2021/2/22 2:43 下午
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ocr")
public class Ocr {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String findCircle;

  private String saveFilePath;

  private String resultAlpha;

  private String resultNum;

  private String useDetect;

  private String scores;

  private String originalPath;

  @ManyToOne(targetEntity = WaterMeter.class, fetch = FetchType.LAZY)
  private WaterMeter waterMeter;

  @Column(columnDefinition = "TIMESTAMP")
  private String time;
}
