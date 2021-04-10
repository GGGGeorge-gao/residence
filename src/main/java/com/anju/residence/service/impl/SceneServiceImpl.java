package com.anju.residence.service.impl;

import com.anju.residence.dao.ele.SceneRepository;
import com.anju.residence.dto.ele.SceneDTO;
import com.anju.residence.entity.ele.Scene;
import com.anju.residence.enums.ResultCode;
import com.anju.residence.exception.ApiException;
import com.anju.residence.service.ele.AlertInfoService;
import com.anju.residence.service.ele.ReceptacleService;
import com.anju.residence.service.SceneService;
import com.anju.residence.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author cygao
 * @date 2020/11/24 14:58
 **/
@Service
public class SceneServiceImpl implements SceneService {

  private final SceneRepository sceneRepo;

  private final UserService userService;
  private final AlertInfoService alertInfoService;
  private final ReceptacleService receptacleService;

  @Autowired
  public SceneServiceImpl(SceneRepository sceneRepo, UserService userService, AlertInfoService alertInfoService, ReceptacleService receptacleService) {
    this.sceneRepo = sceneRepo;
    this.userService = userService;
    this.alertInfoService = alertInfoService;
    this.receptacleService = receptacleService;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void save(Scene scene) {
    scene.setUpdateTime(new Date());
    sceneRepo.save(scene);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public void addScene(SceneDTO sceneDTO) {
    if (!userService.existsById(sceneDTO.getUserId())) {
      throw new ApiException(ResultCode.USER_ERROR, "用户id不存在");
    }
    if (sceneRepo.findIdByUserIdAndName(sceneDTO.getId(), sceneDTO.getName()).isPresent()) {
      throw new ApiException(ResultCode.SCENE_ERROR, "该用户已经拥有相同名称的场景");
    }
    Scene newScene = SceneDTO.buildScene(sceneDTO);
    newScene.setUser(userService.getUserById(sceneDTO.getUserId()).orElseThrow(() -> new ApiException(ResultCode.USER_ERROR, "用户id不存在")));

    sceneRepo.save(newScene);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public void putScene(SceneDTO sceneDTO, Integer sceneId) {
    if (!matchSceneAndUser(sceneDTO.getUserId(), sceneId)) {
      throw new ApiException(ResultCode.SCENE_ERROR, "场景不属于该用户");
    }
    Scene scene = getById(sceneId).orElseThrow(() -> new ApiException(ResultCode.SCENE_ERROR, "场景id不存在"));

    sceneDTO.putScene(scene);

    save(scene);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public void deleteScene(Integer sceneId) {
    if (!existsById(sceneId)) {
      throw new ApiException(ResultCode.SCENE_ERROR, "场景id不存在");
    }
    receptacleService.clearScene(sceneId);
    alertInfoService.deleteBySceneId(sceneId);

    sceneRepo.deleteById(sceneId);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public void updateParentId(Integer sceneId, Integer parentId) {
    if (parentId == null || parentId < 0) {
      throw new ApiException(ResultCode.SCENE_ERROR, "无效的父场景id");
    }
    if (!existsById(sceneId)) {
      throw new ApiException(ResultCode.SCENE_ERROR, "场景id不存在");
    }
    sceneRepo.updateParentId(sceneId, parentId);
  }

  @Override
  public boolean matchSceneAndUser(Integer sceneId, Integer userId) {
    if (sceneId == null) {
      throw new ApiException(ResultCode.SCENE_ERROR, "场景id不能为空");
    }
    if (userId == null) {
      throw new ApiException(ResultCode.USER_ERROR, "用户id为空");
    }
    return sceneRepo.findByIdAndUser(sceneId, userId).isPresent();
  }

  @Override
  public boolean existsById(Integer sceneId) {
    return sceneId != null && sceneRepo.existsById(sceneId);
  }

  @Override
  public Optional<Scene> getById(Integer sceneId) {
    return sceneId == null ? Optional.empty() : sceneRepo.findById(sceneId);
  }

  @Override
  public List<Scene> listByUserId(Integer userId) {
    if (userId == null || !userService.existsById(userId)) {
      throw new ApiException(ResultCode.USER_ERROR, "用户id不存在");
    }
    return sceneRepo.findAllByUserId(userId);
  }

  @Override
  public List<SceneDTO> listTreeByUserId(Integer userId) {
    List<Scene> scenes = listByUserId(userId);
    // 存储场景id到SceneDTO对象的映射关系
    Map<Integer, SceneDTO> dtoMap = new HashMap<>();
    // 存储父id到所有子id的映射
    Map<Integer, List<Integer>> son = new HashMap<>();

    for (Scene scene : scenes) {
      son.computeIfAbsent(scene.getParentId(), k -> new ArrayList<>()).add(scene.getId());
      dtoMap.put(scene.getId(), SceneDTO.build(scene));
    }

    return dfs(0, dtoMap, son);
  }

  private List<SceneDTO> dfs(int curId, Map<Integer, SceneDTO> map, Map<Integer, List<Integer>> son) {
    List<SceneDTO> res = new ArrayList<>();

    // 获取当前对象的所有子场景id
    for (int sonId : son.getOrDefault(curId, Collections.emptyList())) {
      // 获取子场景对象
      SceneDTO dto = map.get(sonId);
      // 对子场景进行深搜
      dto.setSons(dfs(sonId, map, son));
      // 将子场景保存到list中
      res.add(dto);
    }
    return res;
  }


}
