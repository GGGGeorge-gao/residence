package com.anju.residence.service;

import com.anju.residence.entity.Role;

import java.util.List;
import java.util.Optional;

/**
 * @author cygao
 * @date 2020/11/25 21:06
 **/
public interface RoleService {

  /**
   * 查询用户的权限
   *
   * @param userId 用户ID
   * @return 权限列表
   */
  List<Role> listRoleByUserId(int userId);

  /**
   * 查找权限
   *
   * @param roleName 权限名
   * @return 权限对象
   */
  Role getRoleByName(String roleName);


}
