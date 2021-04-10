package com.anju.residence.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author cygao
 * @date 2021/4/10
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WaterMeterVO {

    private Integer id;

    private String name;

    private Integer userId;

    private Integer status;

    private BigDecimal currentCount;

    private Date lastRecordTime;

    private Integer collectIntervalMin;

    private Date updateTime;

    private Date createTime;

    private String description;


}
