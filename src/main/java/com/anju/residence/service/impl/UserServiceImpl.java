package com.anju.residence.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.anju.residence.config.RedisConfig;
import com.anju.residence.dao.UserRepository;
import com.anju.residence.dto.UserDTO;
import com.anju.residence.dto.wx.WxUserDTO;
import com.anju.residence.entity.User;
import com.anju.residence.enums.ResultCode;
import com.anju.residence.exception.ApiException;
import com.anju.residence.security.model.UserDetailsImpl;
import com.anju.residence.service.RoleService;
import com.anju.residence.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

/**
 * @author cygao
 * @date 2020/11/23 23:26
 **/
@Slf4j
@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepo;

  private final RoleService roleService;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserServiceImpl(UserRepository userRepo, RoleService roleService, PasswordEncoder passwordEncoder) {
    this.userRepo = userRepo;
    this.roleService = roleService;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  @Transactional(rollbackFor = Exception.class, propagation = Propagation.MANDATORY)
  public User save(User user) {
    return userRepo.save(user);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public User addByWxUser(WxUserDTO wxUserDTO) {
    String username = wxUserDTO.getNickName();

    // 防止用户名出现重复现象
    String duplicateStr = "";
    while (existsUserByUsername(username + duplicateStr)) {
      duplicateStr = RandomUtil.randomNumbers(3);
    }

    username = username + duplicateStr;
    User user = User.builder().username(username).roles(new ArrayList<>()).build();
    user.getRoles().add(roleService.getByName("ordinary"));
    user.getRoles().add(roleService.getByName("wx_user"));

    return save(user);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public void addUser(UserDTO userDTO) {
    log.info(userDTO.toString());
    if (existsUserByUsername(userDTO.getUsername())) {
      throw new ApiException(ResultCode.USER_ERROR, "用户名已存在");
    }

    User newUser = userDTO.buildUser();
    newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
    newUser.setRoles(Collections.singletonList(roleService.getByName("ordinary")));
    save(newUser);
  }

  @CacheEvict(value = RedisConfig.REDIS_KEY_DATABASE, key = "'user:' + #userId")
  @Transactional(rollbackFor = Exception.class)
  @Override
  public void putUser(UserDTO userDTO, Integer userId) {
    User old = getUserById(userId).orElseThrow(() -> new ApiException(ResultCode.USER_ERROR, "用户id不存在"));
    if (existsUserByUsername(userDTO.getUsername())) {
      throw new ApiException(ResultCode.USER_ERROR, "用户名已存在");
    }
    userDTO.putUser(old);
    save(old);
  }

  @Override
  public Optional<User> getUserByName(String username) {
    return userRepo.findUserByUsername(username);
  }

  @Override
  public boolean existsUserByUsername(String username) {
    return StringUtils.hasLength(username) && userRepo.existsByUsername(username);
  }

  @Cacheable(value = RedisConfig.REDIS_KEY_DATABASE, key = "'user:' + #userId")
  @Override
  public Optional<User> getUserById(Integer userId) {
    return userId == null ? Optional.empty() : userRepo.findById(userId);
  }

  @Override
  public boolean existsById(Integer userId) {
    return userId != null && userRepo.existsById(userId);
  }

  @Override
  public boolean addUser(User user) {
    if (userRepo.existsByUsername(user.getUsername())) {
      return false;
    }
    userRepo.save(user);
    return true;
  }

  @Override
  public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
    return new UserDetailsImpl(getUserByName(username).orElseThrow(() -> new UsernameNotFoundException("Username not found")));
  }
}
