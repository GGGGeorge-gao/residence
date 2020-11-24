package com.anju.residence.dao;

import com.anju.residence.entity.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author cygao
 * @date 2020/11/23 10:45
 **/
@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {

  Role findByName(String roleName);

  boolean existsByName(String roleName);

  void deleteByName(String roleName);
}
