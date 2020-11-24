package com.anju.residence.dao;

import com.anju.residence.entity.Scene;
import com.anju.residence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author cygao
 * @date 2020/11/23 10:43
 **/
@Repository
public interface SceneRepository extends JpaRepository<Scene, Integer> {

  Scene findFirstByUserAndName(User user, String name);

  List<Scene> findAllByUser(User user);

  boolean deleteSceneByUserAndName(User user, String name);
}
