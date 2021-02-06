package com.anju.residence.service.impl;

import com.anju.residence.dao.ele.JackRepository;
import com.anju.residence.dto.ele.JackDTO;
import com.anju.residence.entity.ele.Jack;
import com.anju.residence.entity.ele.Receptacle;
import com.anju.residence.enums.ResultCode;
import com.anju.residence.exception.ApiException;
import com.anju.residence.service.ele.AlertInfoService;
import com.anju.residence.service.ele.BehaviorLogService;
import com.anju.residence.service.ele.DeviceService;
import com.anju.residence.service.ele.JackService;
import com.anju.residence.service.ele.ReceptacleService;
import com.anju.residence.service.ele.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author cygao
 * @date 2020/11/24 16:18
 **/
@Slf4j
@Service
public class JackServiceImpl implements JackService {

  private final JackRepository jackRepo;

  private final AlertInfoService alertInfoService;
  private final TaskService taskService;

  private BehaviorLogService behaviorLogService;
  private DeviceService deviceService;
  private ReceptacleService receptacleService;

  @Autowired
  public JackServiceImpl(JackRepository jackRepo, AlertInfoService alertInfoService, TaskService taskService) {
    this.jackRepo = jackRepo;
    this.alertInfoService = alertInfoService;
    this.taskService = taskService;
  }

  @Autowired
  public void setReceptacleService(ReceptacleService receptacleService) {
    this.receptacleService = receptacleService;
  }
  @Autowired
  public void setBehaviorLogService(BehaviorLogService behaviorLogService) {
    this.behaviorLogService = behaviorLogService;
  }
  @Autowired
  public void setDeviceService(DeviceService deviceService) {
    this.deviceService = deviceService;
  }

  @Override
  public void save(Jack jack) {
    jackRepo.save(jack);
  }

  @Transactional(rollbackFor = Exception.class, propagation = Propagation.MANDATORY)
  @Override
  public void addJacks(List<JackDTO> jacks, Integer receptacleId) {
    Receptacle receptacle = receptacleService.getById(receptacleId).orElseThrow(() -> new ApiException(ResultCode.RECEPTACLE_ID_NOT_EXISTS));
    jackRepo.saveAll(jacks.stream().map(j -> j.build(receptacle)).collect(Collectors.toList()));
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public void deleteJack(Integer jackId) {
    if (!jackRepo.existsById(jackId)) {
      throw new ApiException(ResultCode.JACK_ID_NOT_EXISTS);
    }
    deviceService.clearJack(jackId);
    behaviorLogService.deleteByJackId(jackId);
    alertInfoService.deleteByJackId(jackId);
    taskService.deleteByJackId(jackId);
    jackRepo.deleteById(jackId);
  }

  @Override
  public boolean existsByJackId(int jackId) {
    return jackRepo.existsById(jackId);
  }

  @Override
  public boolean existsJackIdByUserId(Integer jackId, Integer userId) {
    if (jackId == null) {
      return true;
    }
    if (userId == null) {
      return false;
    }
    int receptacleId = jackRepo.findReceptacleIdByJackId(jackId).orElse(-1);
    if (receptacleId == -1) {
      return false;
    }

    return receptacleService.matchReceptacleAndUser(receptacleId, userId);
  }

  @Override
  public Optional<Jack> getById(Integer jackId) {
    if (jackId == null || jackId < 0) {
      return Optional.empty();
    }
    return jackRepo.findById(jackId);
  }

  @Override
  public List<Jack> listJacksByUserId(int userId) {
    return jackRepo.findAllByUserId(userId);
  }

  @Override
  public List<Jack> listJacksByReceptacle(Integer receptacleId) {
    return jackRepo.findAllByReceptacleId(receptacleId);
  }

  @Override
  public List<Integer> listIdByReceptacle(Integer receptacleId) {
    return jackRepo.findAllIdByReceptacleId(receptacleId);
  }

  @Override
  public void updateStatus(int jackId, int status) {
    if (jackRepo.existsById(jackId)) {
      throw new ApiException(ResultCode.JACK_ID_NOT_EXISTS);
    }
    jackRepo.updateStatus(jackId, status);
  }
}
