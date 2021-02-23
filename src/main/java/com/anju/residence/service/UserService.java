package com.anju.residence.service;

import com.anju.residence.dto.UserDTO;
import com.anju.residence.dto.wx.WxUserDTO;
import com.anju.residence.entity.User;
import com.anju.residence.security.model.UserDetailsImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

/**
 * @author cygao
 * @date 2020/11/25 20:55
 **/
public interface UserService extends UserDetailsService {

  /**
   * 根据用户名查询用户实体类对象
   *
   * @param username 用户名
   * @return 用户实体类的Optional对象
   */
  Optional<User> getUserByName(String username);

  /**
   * 查询是否存在该用户
   *
   * @param username 用户名
   * @return 是否存在
   */
  boolean existsUserByUsername(String username);

  /**
   * 查询用户
   *
   * @param userId 用户id
   * @return 用户
   */
  Optional<User> getUserById(Integer userId);

  /**
   * 查询是否存在该用户
   *
   * @param userId 用户id
   * @return 是否存在
   */
  boolean existsById(Integer userId);

  /**
   * 添加用户
   *
   * @param user 用户实体类对象
   * @return 是否添加成功
   */
  boolean addUser(User user);

  /**
   * 保存用户
   *
   * @param user 用户实体类对象
   * @return 用户实体类
   */
  User save(User user);

  /**
   * 根据微信用户添加一个新用户
   * @param wxUserDTO 微信用户实体传输类
   * @return 用户实体类
   */
  User addByWxUser(WxUserDTO wxUserDTO);

  /**
   * 添加用户
   *
   * @param userDTO 用户dto对象
   */
  void addUser(UserDTO userDTO);

  /**
   * 修改用户信息
   *
   * @param userDTO 用户dto对象
   * @param userId  用户id
   */
  void putUser(UserDTO userDTO, Integer userId);

  /**
   * Spring Security加载用户
   * @param username 用户名
   * @return {@link UserDetailsImpl}
   * @throws UsernameNotFoundException 用户不存在
   */
  @Override
  UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException;
}
