package com.anju.residence.dao;

import com.anju.residence.entity.UserPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author cygao
 * @date 2021/2/8 10:18 上午
 **/
@Repository
public interface UserPriceRepository extends JpaRepository<UserPrice, Integer> {

  /**
   * 查询用户是否存在价格表
   * @param userId 用户id
   * @return 价格表条数
   */
  @Query(nativeQuery = true, value = "select count(*) from user_price as up where up.user_id = ?1")
  Integer countByUserId(Integer userId);

  /**
   * 查询用户价格表
   * @param userId 用户id
   * @return 用户价格实体类Optional对象
   */
  @Query(nativeQuery = true, value = "select  * from user_price where user_id = ?1 limit 1")
  Optional<UserPrice> findByUserId(Integer userId);
}
