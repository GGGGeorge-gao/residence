package com.anju.residence.service.impl;

import com.anju.residence.dao.ele.ReceptacleLogRepository;
import com.anju.residence.entity.ele.Device;
import com.anju.residence.entity.ele.ElectricLog;
import com.anju.residence.entity.ele.Receptacle;
import com.anju.residence.entity.ele.ReceptacleLog;
import com.anju.residence.enums.ResultCode;
import com.anju.residence.exception.ApiException;
import com.anju.residence.service.ele.ReceptacleLogService;
import com.anju.residence.service.UserService;
import com.anju.residence.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author cygao
 * @date 2020/12/1 20:12
 **/
@Slf4j
@Service
public class ReceptacleLogServiceImpl implements ReceptacleLogService {

  private final ReceptacleLogRepository receptacleLogRepo;

  private final UserService userService;

  @Autowired
  public ReceptacleLogServiceImpl(ReceptacleLogRepository receptacleLogRepo, UserService userService) {
    this.receptacleLogRepo = receptacleLogRepo;
    this.userService = userService;
  }

  @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
  @Override
  public void updateByElectricLog(ElectricLog eleLog) {

    Device device = eleLog.getDevice();

    Receptacle r = device.getJack().getReceptacle();
    ReceptacleLog preLog = getByReceptacleIdAndDate(r.getId(), eleLog.getTime()).orElse(ReceptacleLog.builder().consumption(0).receptacle(r).time(eleLog.getTime()).others(eleLog.getOthers()).build());

    preLog.addConsumption(eleLog.getConsumption());
    receptacleLogRepo.save(preLog);

  }

  @Override
  public List<ReceptacleLog> listByUserIdBetween(Integer userId, Date from, Date to) {
    if (!userService.existsById(userId)) {
      return new ArrayList<>();
    }

    List<ReceptacleLog> logs = receptacleLogRepo.findAllByUserAndTimeBetween(userId, from, to);

    return logs == null ? new ArrayList<>() : logs;
  }

  @Override
  public List<ReceptacleLog> listLogByUserIdAndDate(Integer userId, Date date) {
    return listByUserIdBetween(userId, DateUtil.getFirstSecondDate(date), DateUtil.getLastSecondDate(date));
  }

  @Override
  public List<ReceptacleLog> listTodayLogByUserId(Integer userId) {
    return listLogByUserIdAndDate(userId, new Date());
  }

  @Override
  public List<ReceptacleLog> listByReceptacleId(Integer receptacleId) {
    return receptacleLogRepo.findAllByReceptacleId(receptacleId);
  }

  @Override
  public List<ReceptacleLog> listByReceptacleIdBetween(Integer receptacleId, Date from, Date to) {
    return receptacleLogRepo.findAllByReceptacleIdBetween(receptacleId, DateUtil.getFirstSecondDate(from), DateUtil.getLastSecondDate(to));
  }

  @Override
  public Optional<ReceptacleLog> getByReceptacleIdAndDate(Integer receptacleId, Date date) {
    return receptacleLogRepo.findReceptacleLogByTime(receptacleId, DateUtil.getFirstSecondDate(date), DateUtil.getLastSecondDate(date));
  }

  @Override
  public int getTodayTotalConsumption(Integer userId) {
    int realTimePower = 0;
    if (!userService.existsById(userId)) {
      return realTimePower;
    }

    Date from = DateUtil.getTodayFirstSecondDate();
    Date to = DateUtil.now();

    List<ReceptacleLog> logs = receptacleLogRepo.findAllByUserAndTimeBetween(userId, from, to);

    for (ReceptacleLog rLog : logs) {
      realTimePower += rLog.getConsumption();
    }

    return realTimePower;
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public void deleteByReceptacleId(Integer receptacleId) {
    if (receptacleId == null) {
      throw new ApiException(ResultCode.RECEPTACLE_ERROR, "插座id不能为空");
    }
    receptacleLogRepo.deleteAllByReceptacleId(receptacleId);
  }

  @Override
  public boolean existsById(Integer logId) {
    return logId != null && receptacleLogRepo.existsById(logId);
  }
}
