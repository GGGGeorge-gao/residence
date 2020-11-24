package com.anju.residence.service;

import com.anju.residence.dao.SceneRepository;
import com.anju.residence.entity.Scene;
import com.anju.residence.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * @author cygao
 * @date 2020/11/24 14:58
 **/
@Service
public class SceneService {

  private final SceneRepository sceneRepo;
  private final UserService userService;

  @Autowired
  public SceneService(SceneRepository sceneRepo, UserService userService) {
    this.sceneRepo = sceneRepo;
    this.userService = userService;
  }

  @Transactional
  public void save(Scene scene) {
    scene.setUpdateTime(new Date());
    sceneRepo.save(scene);
  }

  @Transactional
  public List<Scene> findAllByUser(User user) {
    return sceneRepo.findAllByUser(user);
  }

  public List<Scene> findAllByUser(int userId) {
    return sceneRepo.findAllByUser(userService.build(userId));
  }

  @Transactional
  public boolean deleteScene(User user, String name) {
    if (user == null || StringUtils.hasLength(name)) {
      return  false;
    }
    return sceneRepo.deleteSceneByUserAndName(user, name);
  }

  public boolean deleteScene(int userId, String name) {
    return deleteScene(userService.build(userId), name);
  }




}
