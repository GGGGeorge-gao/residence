package com.anju.residence.service.impl;

import com.anju.residence.dao.RoleRepository;
import com.anju.residence.entity.Role;
import com.anju.residence.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author cygao
 * @date 2020/11/25 21:06
 **/
@Service
public class RoleServiceImpl implements RoleService {

  @Autowired
  private RoleRepository roleRepo;

  @Override
  public List<Role> listRoleByUserId(int userId) {
    return roleRepo.findRolesByUserId(userId);
  }

  @Override
  public Role getRoleByName(String roleName) {
    return roleRepo.findByRoleName(roleName);
  }
}
