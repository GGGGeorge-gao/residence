package com.anju.residence.controller.ele;

import com.anju.residence.dto.ele.DeviceDTO;
import com.anju.residence.entity.ele.Device;
import com.anju.residence.enums.ResultCode;
import com.anju.residence.exception.ApiException;
import com.anju.residence.service.ele.DeviceService;
import com.anju.residence.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author cygao
 * @date 2020/12/16 21:03
 **/
@Api(tags = "设备API（智能插座）")
@Slf4j
@RestController
@RequestMapping("/api/v1/device")
public class DeviceController {

  private final DeviceService deviceService;

  @Autowired
  public DeviceController(DeviceService deviceService) {
    this.deviceService = deviceService;
  }

  @ApiOperation(value = "获取设备详细信息")
  @GetMapping("/{deviceId}")
  public ResultVO<Device> getDevice(@PathVariable Integer deviceId) {

    return new ResultVO<>(deviceService.getById(deviceId).orElseThrow(() -> new ApiException(ResultCode.DEVICE_ID_NOT_EXISTS)));
  }

  @ApiOperation(value = "添加一个设备")
  @PostMapping("/add")
  public ResultVO<String> addDevice(@RequestBody @Valid DeviceDTO deviceDTO) {
    deviceService.addDevice(deviceDTO);

    return new ResultVO<>("success");
  }

  @ApiOperation(value = "删除一个设备")
  @DeleteMapping("/{deviceId}")
  public ResultVO<String> deleteDevice(@PathVariable Integer deviceId) {
    // 后期添加用户权限校验

    deviceService.deleteDevice(deviceId);

    return new ResultVO<>("success");
  }

  @ApiOperation(value = "修改设备信息")
  @PutMapping("/{deviceId}")
  public ResultVO<String> putDevice(@RequestBody @Valid DeviceDTO deviceDTO, @PathVariable Integer deviceId) {

    deviceService.putDevice(deviceDTO, deviceId);

    return new ResultVO<>("success");
  }

  @ApiOperation(value = "获取一个用户所有的设备的详细信息")
  @GetMapping("/all/{userId}")
  public ResultVO<List<Device>> listDevice(@PathVariable Integer userId) {

    List<Device> devices = deviceService.listDeviceByUserId(userId);

    return new ResultVO<>(devices);
  }
}