package com.anju.residence.dao;

import com.anju.residence.entity.Role;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author cygao
 * @date 2020/11/23 10:45
 **/
@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {

  /**
   * 根据角色名查询角色
   *
   * @param roleName 角色名
   * @return 角色实体类对象
   */
  Role findByRoleName(String roleName);

  /**
   * 查询该角色名是否存在
   *
   * @param roleName 角色名
   * @return 是否存在
   */
  boolean existsByRoleName(String roleName);

  /**
   * 删除一个角色
   *
   * @param roleName 角色名
   */
  @Modifying
  void deleteByRoleName(String roleName);

  /**
   * 根据用户id查询该用户的角色
   *
   * @param userId 用户id
   * @return 角色列表
   */
  @Query(nativeQuery = true, value = "select * from role as r where r.role_id in (select ur.role_id from user_roles as ur where ur.user_id = ?1)")
  List<Role> findRolesByUserId(Integer userId);
}
