package com.anju.residence.dao;

import com.anju.residence.entity.Receptacle;
import com.anju.residence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author cygao
 * @date 2020/11/23 10:44
 **/
@Repository
public interface ReceptacleRepository extends JpaRepository<Receptacle, Integer> {

  Receptacle findFirstByUserAndName(User user, String name);

  List<Receptacle> findAllByUser(User user);

}
