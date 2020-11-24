package com.anju.residence.dao;

import com.anju.residence.entity.AlertInfo;
import com.anju.residence.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author cygao
 * @date 2020/11/23 10:46
 **/
@Repository
public interface AlertInfoRepository extends CrudRepository<AlertInfo, Integer> {

  AlertInfo findByUser(User user);

  boolean existsByUser(User user);
}
