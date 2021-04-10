package com.anju.residence.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author cygao
 * @date 2021/4/10
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceVO {

    private Integer id;

    private Integer userId;

    private Integer jackId;

    private String name;

    private String type;

    private Integer status;

    private Date createTime;

    private Date updateTime;


}
