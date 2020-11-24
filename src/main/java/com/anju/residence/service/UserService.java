package com.anju.residence.service;

import com.anju.residence.dao.UserRepository;
import com.anju.residence.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author cygao
 * @date 2020/11/23 23:26
 **/
@Service
public class UserService {

  private final UserRepository userRepo;

  @Autowired
  public UserService(UserRepository userRepo) {
    this.userRepo = userRepo;
  }

  @Transactional
  public void save(User user) {
    user.setUpdateTime(new Date());
    userRepo.save(user);
  }

  public User findUserByName(String username) {
    return userRepo.findUserByUsername(username);
  }

  public User build(int userId) {
    return User.builder().id(userId).build();
  }
}
