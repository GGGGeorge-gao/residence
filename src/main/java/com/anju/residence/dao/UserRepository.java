package com.anju.residence.dao;

import com.anju.residence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author cygao
 * @date 2020/11/23 10:39
 **/
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  User findUserByUsername(String username);

  boolean existsUserByUsername(String username);


}
