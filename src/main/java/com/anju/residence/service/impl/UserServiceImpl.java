package com.anju.residence.service.impl;

import com.anju.residence.dao.UserRepository;
import com.anju.residence.dto.UserDTO;
import com.anju.residence.entity.User;
import com.anju.residence.enums.ResultCode;
import com.anju.residence.exception.ApiException;
import com.anju.residence.security.model.UserDetailsImpl;
import com.anju.residence.service.RoleService;
import com.anju.residence.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
  public void save(User user) {
    userRepo.save(user);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public void addUser(UserDTO userDTO) {
    log.info(userDTO.toString());
    if (existsUserByUsername(userDTO.getUsername())) {
      throw new ApiException(ResultCode.USERNAME_ALREADY_EXISTS);
    }

    User newUser = userDTO.buildUser();
    newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
    newUser.setRoles(Collections.singletonList(roleService.getRoleByName("ORDINARY")));
    save(newUser);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public void putUser(UserDTO userDTO, Integer userId) {
    User old = getUserById(userId).orElseThrow(() -> new ApiException(ResultCode.USER_ID_NOT_EXISTS));
    if (existsUserByUsername(userDTO.getUsername())) {
      throw new ApiException(ResultCode.USERNAME_ALREADY_EXISTS);
    }
    userDTO.putUser(old);
    save(old);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public void patchUser(UserDTO userDTO, Integer userId) {
    User old = getUserById(userId).orElseThrow(() -> new ApiException(ResultCode.USER_ID_NOT_EXISTS));
    if (existsUserByUsername(userDTO.getUsername())) {
      throw new ApiException(ResultCode.USERNAME_ALREADY_EXISTS);
    }
    userDTO.patchUser(old);
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
