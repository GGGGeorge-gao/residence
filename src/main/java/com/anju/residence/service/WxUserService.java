package com.anju.residence.service;

import com.anju.residence.dto.wx.WxUserDTO;
import com.anju.residence.entity.WxUser;
import com.anju.residence.security.model.WxSession;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author cygao
 * @date 2021/2/19 02:27 下午
 */
@Service
public interface WxUserService {

  /**
   * 添加一个微信用户
   * @param wxUserDTO 微信用户实体传输类
   */
  void addWxUser(WxUserDTO wxUserDTO);

  /**
   * 更新微信用户
   * @param wxUserDTO 微信用户实体传输类
   */
  void updateWxUser(WxUserDTO wxUserDTO);

  /**
   * 登录
   * @param wxUserDTO 微信用户实体传输类
   */
  void login(WxUserDTO wxUserDTO);

  /**
   * 根据skey查询用户
   * @param skey skey
   * @return 微信用户的Optional对象
   */
  Optional<WxUser> getWxUserBySkey(String skey);

  /**
   * 根据openid查询用户
   * @param openId openid
   * @return 微信用户的Optional对象
   */
  Optional<WxUser> getWxUserByOpenId(String openId);

  /**
   * 根据当前token查询用户
   * @return 微信用户的Optional对象
   */
  Optional<WxUser> getWxUserByToken();

  /**
   * 根据openid查询sessionKey
   * @param openId openid
   * @return sessionKey
   */
  Optional<String> getSessionKeyByOpenId(String openId);

  /**
   * 根据{@link WxSession}更新或注册微信用户
   * @param wxSession wxSession
   */
  void updateByWxSession(WxSession wxSession);

  /**
   * 查询该用户是否存在
   * @param openId openid
   * @return 是否存在
   */
  boolean existsByOpenid(String openId);

  /**
   * 保存用户
   * @param wxUser 微信用户
   * @return
   */
  WxUser save(WxUser wxUser);
}
