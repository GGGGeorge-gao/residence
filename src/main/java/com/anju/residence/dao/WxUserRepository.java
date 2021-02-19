package com.anju.residence.dao;

import com.anju.residence.entity.WxUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author cygao
 * @date 2021/2/19 10:27 上午
 */
@Repository
public interface WxUserRepository extends JpaRepository<WxUser, String> {

  /**
   * 根据openId查询微信用户的sessionKey
   * @param openId openid
   * @return sessionKey
   */
  @Query(nativeQuery = true, value = "select session_key from wx_user as wu where wu.open_id = ?1")
  Optional<String> findSessionKeyByOpenId(String openId);
}
