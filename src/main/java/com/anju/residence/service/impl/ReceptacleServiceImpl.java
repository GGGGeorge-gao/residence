package com.anju.residence.service.impl;

import com.anju.residence.dao.ele.ReceptacleRepository;
import com.anju.residence.dto.ele.ReceptacleDTO;
import com.anju.residence.entity.ele.Receptacle;
import com.anju.residence.entity.ele.Scene;
import com.anju.residence.entity.User;
import com.anju.residence.enums.ResultCode;
import com.anju.residence.exception.ApiException;
import com.anju.residence.service.ele.JackService;
import com.anju.residence.service.ele.ReceptacleLogService;
import com.anju.residence.service.ele.ReceptacleService;
import com.anju.residence.service.SceneService;
import com.anju.residence.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

/**
 * @author cygao
 * @date 2020/11/24 15:48
 **/
@Slf4j
@Service
public class ReceptacleServiceImpl implements ReceptacleService {

  private final ReceptacleRepository receptacleRepo;

  private final UserService userService;
  private final JackService jackService;
  private final ReceptacleLogService receptacleLogService;

  private SceneService sceneService;

  @Autowired
  public ReceptacleServiceImpl(ReceptacleRepository receptacleRepo, UserService userService, JackService jackService, ReceptacleLogService receptacleLogService) {
    this.receptacleRepo = receptacleRepo;
    this.userService = userService;
    this.jackService = jackService;
    this.receptacleLogService = receptacleLogService;
  }

  @Autowired
  public void setSceneService(SceneService sceneService) {
    this.sceneService = sceneService;
  }

  @Override
  public List<Receptacle> getAllByUser(User user) {
    return receptacleRepo.findAllByUser(user);
  }

  @Override
  public List<Receptacle> getAllByScene(Scene scene) {
    return receptacleRepo.findAllByScene(scene);
  }

  @Override
  public List<Receptacle> getAllByUserAndStatus(User user, int status) {
    return receptacleRepo.findAllByUserAndStatus(user, status);
  }

  @Override
  public List<Receptacle> getAllBySceneAndStatus(Scene scene, int status) {
    return receptacleRepo.findAllBySceneAndStatus(scene, status);
  }

  @Override
  public Optional<Receptacle> getById(int receptacleId) {
    return receptacleRepo.findById(receptacleId);
  }

  @Override
  public Optional<Receptacle> getByJackId(int jackId) {
    return receptacleRepo.findByJackId(jackId);
  }

  @Override
  public boolean matchReceptacleAndUser(Integer receptacleId, Integer userId) {
    if (receptacleId == null) {
      return false;
    }
    return receptacleRepo.findUserIdByReceptacleId(receptacleId).orElse(-1).equals(userId);
  }

  @Override
  public boolean existsById(Integer receptacleId) {
    return receptacleId != null && receptacleRepo.existsById(receptacleId);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public void addReceptacle(ReceptacleDTO receptacleDTO) {
    if (CollectionUtils.isEmpty(receptacleDTO.getJacks())) {
      throw new ApiException(ResultCode.ARGUMENT_NULL);
    }
    if (!userService.existsById(receptacleDTO.getUserId())) {
      throw new ApiException(ResultCode.USER_ID_NOT_EXISTS);
    }
    if (!sceneService.matchSceneAndUser(receptacleDTO.getSceneId(), receptacleDTO.getUserId())) {
      throw new ApiException(ResultCode.SCENE_USER_MISMATCH);
    }
    Receptacle receptacle = ReceptacleDTO.buildReceptacle(receptacleDTO);
    receptacle.setUser(userService.getUserById(receptacleDTO.getUserId()).orElseThrow(() -> new ApiException(ResultCode.USER_ID_NOT_EXISTS)));
    receptacle.setScene(sceneService.getById(receptacleDTO.getSceneId()).orElse(null));

    receptacle = receptacleRepo.save(receptacle);

    jackService.addJacks(receptacleDTO.getJacks(), receptacle.getId());
  }

  @Override
  public void putReceptacle(ReceptacleDTO receptacleDTO, Integer receptacleId) {
    if (!userService.existsById(receptacleDTO.getUserId())) {
      throw new ApiException(ResultCode.USER_ID_NOT_EXISTS);
    }
    if (receptacleDTO.getSceneId() != null && !sceneService.matchSceneAndUser(receptacleDTO.getSceneId(), receptacleDTO.getUserId())) {
      throw new ApiException(ResultCode.SCENE_USER_MISMATCH);
    }
    Receptacle receptacle = getById(receptacleId).orElseThrow(() -> new ApiException(ResultCode.RECEPTACLE_ID_NOT_EXISTS));
    receptacleDTO.putReceptacle(receptacle);

    save(receptacle);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public void deleteReceptacle(Integer receptacleId) {
    if (!existsById(receptacleId)) {
      throw new ApiException(ResultCode.RECEPTACLE_ID_NOT_EXISTS);
    }
    receptacleLogService.deleteByReceptacleId(receptacleId);
    jackService.listIdByReceptacle(receptacleId).forEach(jackService::deleteJack);
    receptacleRepo.deleteById(receptacleId);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public void save(Receptacle receptacle) {
    receptacleRepo.save(receptacle);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public boolean clearScene(int sceneId) {
    receptacleRepo.clearScene(sceneId);
    return true;
  }


}
