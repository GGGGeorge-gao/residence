package com.anju.residence.dao;

import com.anju.residence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author cygao
 * @date 2020/11/23 10:39
 **/
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  /**
   * 通过用户名获取该用户
   * @param username 用户名
   * @return 用户实体类对象
   */
  Optional<User> findUserByUsername(String username);

  /**
   * 查询该用户名是否存在
   * @param username 用户名
   * @return {@code true} if username exists
   */
  boolean existsByUsername(String username);

  /**
   * 删除用户
   * @param username 用户名
   */
  @Modifying
  void deleteUserByUsername(String username);

}