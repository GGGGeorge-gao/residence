package com.anju.residence.service.impl;

import com.anju.residence.dao.ele.AlertInfoRepository;
import com.anju.residence.service.ele.AlertInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author cygao
 * @date 2020/12/8 11:15
 **/
@Service
public class AlertInfoServiceImpl implements AlertInfoService {

  private final AlertInfoRepository alertInfoRepo;

  @Autowired
  public AlertInfoServiceImpl(AlertInfoRepository alertInfoRepo) {
    this.alertInfoRepo = alertInfoRepo;
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public boolean deleteBySceneId(int sceneId) {
    alertInfoRepo.deleteAllBySceneId(sceneId);
    return true;
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public boolean deleteByJackId(int jackId) {
    alertInfoRepo.deleteAllByJackId(jackId);
    return false;
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public boolean deleteByDeviceId(int deviceId) {
    alertInfoRepo.deleteAllByDeviceId(deviceId);
    return true;
  }
}
