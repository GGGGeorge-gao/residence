package com.anju.residence.service.impl;

import com.anju.residence.dao.ele.TaskRepository;
import com.anju.residence.service.ele.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author cygao
 * @date 2020/12/8 13:04
 **/
@Service
public class TaskServiceImpl implements TaskService {

  private final TaskRepository taskRepo;

  @Autowired
  public TaskServiceImpl(TaskRepository taskRepository) {
    this.taskRepo = taskRepository;
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public boolean deleteByJackId(int jackId) {
    taskRepo.deleteAllByJackId(jackId);
    return true;
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public boolean deleteByDeviceId(int deviceId) {
    taskRepo.deleteAllByDeviceId(deviceId);
    return true;
  }
}
