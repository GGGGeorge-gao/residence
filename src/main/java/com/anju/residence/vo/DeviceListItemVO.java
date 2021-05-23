package com.anju.residence.vo;

import lombok.Data;

import java.util.Date;

@Data
public class DeviceListItemVO {
    private int deviceId;
    private String deviceName;
    private String sceneName;
    private String jackName;
    private int alertCount;
    private int realTimePower;
    private int jackId;
    private int status;
    private String type;
    private Date createTime;
    private Date updateTime;
}
