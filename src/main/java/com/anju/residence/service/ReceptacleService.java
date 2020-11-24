package com.anju.residence.service;

import com.anju.residence.dao.ReceptacleRepository;
import com.anju.residence.entity.Receptacle;
import com.anju.residence.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cygao
 * @date 2020/11/24 15:48
 **/
@Service
public class ReceptacleService {

  private final ReceptacleRepository receptacleRepo;
  private final UserService userService;

  @Autowired
  public ReceptacleService(ReceptacleRepository receptacleRepo, UserService userService) {
    this.receptacleRepo = receptacleRepo;
    this.userService = userService;
  }

  public List<Receptacle> findAll(User user) {
    return receptacleRepo.findAllByUser(user);
  }

  public List<Receptacle> findAll(int userId) {
    return findAll(userService.build(userId));
  }
}
