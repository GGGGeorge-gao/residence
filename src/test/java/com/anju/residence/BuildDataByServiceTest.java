package com.anju.residence;

import com.anju.residence.entity.ele.Receptacle;
import com.anju.residence.entity.ele.Scene;
import com.anju.residence.service.SceneService;
import com.anju.residence.service.UserService;
import com.anju.residence.service.ele.DeviceLogService;
import com.anju.residence.service.ele.DeviceService;
import com.anju.residence.service.ele.ElectricLogService;
import com.anju.residence.service.ele.JackService;
import com.anju.residence.service.ele.ReceptacleLogService;
import com.anju.residence.service.ele.ReceptacleService;
import com.anju.residence.service.water.WaterMeterService;
import com.anju.residence.service.water.WaterRecordLogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author cygao
 * @date 2021/2/7 12:07 下午
 **/
@SpringBootTest
public class BuildDataByServiceTest {

  private final UserService userService;

  private final DeviceService deviceService;
  private final DeviceLogService deviceLogService;
  private final ElectricLogService electricLogService;
  private final SceneService sceneService;
  private final JackService jackService;
  private final ReceptacleService receptacleService;
  private final ReceptacleLogService receptacleLogService;

  private final WaterMeterService waterMeterService;
  private final WaterRecordLogService waterRecordLogService;

  @Autowired
  public BuildDataByServiceTest(UserService userService, DeviceService deviceService, DeviceLogService deviceLogService, ElectricLogService electricLogService, SceneService sceneService, JackService jackService, ReceptacleService receptacleService, ReceptacleLogService receptacleLogService, WaterMeterService waterMeterService, WaterRecordLogService waterRecordLogService) {
    this.userService = userService;
    this.deviceService = deviceService;
    this.deviceLogService = deviceLogService;
    this.electricLogService = electricLogService;
    this.sceneService = sceneService;
    this.jackService = jackService;
    this.receptacleService = receptacleService;
    this.receptacleLogService = receptacleLogService;
    this.waterMeterService = waterMeterService;
    this.waterRecordLogService = waterRecordLogService;
  }

  @Test
  void buildData() {

  }
}
