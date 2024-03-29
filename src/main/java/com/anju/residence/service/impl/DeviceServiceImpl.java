package com.anju.residence.service.impl;

import com.anju.residence.dao.ele.AlertInfoRepository;
import com.anju.residence.dao.ele.DeviceRepository;
import com.anju.residence.dao.ele.ElectricLogRepository;
import com.anju.residence.dto.ele.DeviceDTO;
import com.anju.residence.entity.ele.Device;
import com.anju.residence.entity.ele.ElectricLog;
import com.anju.residence.entity.ele.Jack;
import com.anju.residence.enums.ResultCode;
import com.anju.residence.exception.ApiException;
import com.anju.residence.service.ele.AlertInfoService;
import com.anju.residence.service.ele.BehaviorLogService;
import com.anju.residence.service.ele.DeviceLogService;
import com.anju.residence.service.ele.DeviceService;
import com.anju.residence.service.ele.ElectricLogService;
import com.anju.residence.service.ele.JackService;
import com.anju.residence.service.ele.TaskService;
import com.anju.residence.service.UserService;
import com.anju.residence.vo.DeviceListItemVO;
import com.anju.residence.vo.DeviceVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author cygao
 * @date 2020/12/4 9:02
 **/
@Slf4j
@Service
public class DeviceServiceImpl implements DeviceService {

  private final DeviceRepository deviceRepo;

  private final AlertInfoService alertInfoService;
  private final TaskService taskService;
  private final UserService userService;

  private JackService jackService;
  private DeviceLogService deviceLogService;
  private ElectricLogService eleLogService;
  private BehaviorLogService behaviorLogService;
  private final ElectricLogRepository electricLogRepo;
  private final AlertInfoRepository alertInfoRepo;

  @Autowired
  public DeviceServiceImpl(DeviceRepository deviceRepo, AlertInfoService alertInfoService,
                           TaskService taskService, UserService userService, ElectricLogRepository electricLogRepo, AlertInfoRepository alertInfoRepo) {
    this.deviceRepo = deviceRepo;
    this.alertInfoService = alertInfoService;
    this.taskService = taskService;
    this.userService = userService;
    this.electricLogRepo = electricLogRepo;
    this.alertInfoRepo = alertInfoRepo;
  }

  @Autowired
  public void setEleLogService(ElectricLogService eleLogService) {
    this.eleLogService = eleLogService;
  }
  @Autowired
  public void setDeviceLogService(DeviceLogService deviceLogService) {
    this.deviceLogService = deviceLogService;
  }
  @Autowired
  public void setJackService(JackService jackService) {
    this.jackService = jackService;
  }
  @Autowired
  public void setBehaviorLogService(BehaviorLogService behaviorLogService) {
    this.behaviorLogService = behaviorLogService;
  }

  @Override
  public List<Integer> listDeviceIdByUserId(Integer userId) {
    if (userId == null) {
      throw new ApiException(ResultCode.USER_ERROR, "用户id为空");
    }
    return deviceRepo.findAllDeviceIdByUserId(userId);
  }

  @Override
  public List<DeviceListItemVO> listVoByUserId(Integer userId) {
    if (userId == null) {
      throw new ApiException(ResultCode.USER_ERROR, "用户id为空");
    }
    List<DeviceListItemVO> deviceListItemVOS = new ArrayList<>();
    List<DeviceVO> allVoByUserId = deviceRepo.findAllVoByUserId(userId);
    allVoByUserId.forEach(e -> {
      Integer receptacleId = deviceRepo.getReceptacleIdByJackId(e.getJackId());
      int sceneId = deviceRepo.getSceneIdByReceptacleId(receptacleId);
      ElectricLog electricLog = electricLogRepo.findLatestLogByDeviceId(e.getId()).get();
      String sceneName = deviceRepo.getSceneNameBySceneId(sceneId);
      int alertCount = alertInfoRepo.getAllByDeviceId(e.getId()).size();
      DeviceListItemVO vo = new DeviceListItemVO();
      vo.setDeviceId(e.getId());
      vo.setDeviceName(e.getName());
      vo.setJackId(e.getJackId());
      vo.setSceneName(sceneName);
      vo.setAlertCount(alertCount);
      vo.setRealTimePower(electricLog.getPower());
      vo.setJackId(e.getJackId());
      vo.setStatus(e.getStatus());
      vo.setType(e.getType());
      vo.setCreateTime(e.getCreateTime());
      vo.setUpdateTime(e.getUpdateTime());
      deviceListItemVOS.add(vo);
    });
    return deviceListItemVOS;
  }

  @Override
  public Optional<Device> getById(Integer deviceId) {
    return deviceRepo.findById(deviceId);
  }

  @Override
  public void clearJack(int jackId) {
    deviceRepo.clearJack(jackId);
  }

  @Override
  public boolean existsById(Integer deviceId) {
    return deviceId != null && deviceRepo.existsById(deviceId);
  }

  @Override
  public boolean checkPermission(Integer deviceId, Integer userId) {
    if (deviceId == null) {
      throw new ApiException(ResultCode.DEVICE_ERROR, "设备id不能为空");
    }
    if (userId == null) {
      throw new ApiException(ResultCode.USER_ERROR, "用户id不能为空");
    }
    return deviceRepo.findIdByIdAndUser(deviceId, userId).isPresent();
  }

  @Override
  public void save(Device device) {
    deviceRepo.save(device);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public void putDevice(DeviceDTO deviceDTO, Integer deviceId) {
    if (!checkPermission(deviceDTO.getUserId(), deviceId)) {
      throw new ApiException(ResultCode.DEVICE_ERROR, "设备不属于该用户");
    }
    Device device = getById(deviceId).orElseThrow(() -> new ApiException(ResultCode.DEVICE_ERROR, "设备id不存在"));

    deviceDTO.putDevice(device);
    device.setJack(jackService.getById(deviceDTO.getJackId()).orElse(null));

    save(device);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public void addDevice(DeviceDTO deviceDTO) {
    if (!userService.existsById(deviceDTO.getUserId())) {
      throw new ApiException(ResultCode.USER_ERROR, "用户id不存在");
    }
    if (!jackService.existsByJackId(deviceDTO.getJackId())) {
      throw new ApiException(ResultCode.JACK_ERROR, "插孔id不存在");
    }
    if (!jackService.existsJackIdByUserId(deviceDTO.getJackId(), deviceDTO.getUserId())) {
      throw new ApiException(ResultCode.JACK_ERROR, "插孔不属于该用户");
    }

    Jack jack = jackService.getById(deviceDTO.getJackId()).orElseThrow(() -> new ApiException(ResultCode.JACK_ERROR, "插孔id不存在"));

    Device device = deviceDTO.buildDevice();
    device.setStatus(0);
    device.setJack(jack);

    device.setUser(jack.getReceptacle().getUser());

    save(device);
    log.info("Add device successfully by user: {}", device.getUser().getId());
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public void deleteDevice(Integer deviceId) {
    if (!existsById(deviceId)) {
      throw new ApiException(ResultCode.DEVICE_ERROR, "设备id不存在");
    }
    alertInfoService.deleteByDeviceId(deviceId);
    taskService.deleteByDeviceId(deviceId);
    deviceLogService.deleteByDeviceId(deviceId);
    eleLogService.deleteByDeviceId(deviceId);
    behaviorLogService.deleteByDeviceId(deviceId);

    deviceRepo.deleteById(deviceId);
  }

  @Override
  public void updateStatus(int deviceId, int status) {
    if (!deviceRepo.existsById(deviceId)) {
      throw new ApiException(ResultCode.DEVICE_ERROR, "设备id不存在");
    }
    deviceRepo.updateStatus(deviceId, status);
  }
}
