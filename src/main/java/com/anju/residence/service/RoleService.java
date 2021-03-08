package com.anju.residence.service;

import com.anju.residence.dao.RoleRepository;
import com.anju.residence.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cygao
 * @date 2021/2/20 12:42 上午
 **/
@Service
public class RoleService {

  private final RoleRepository roleRepo;
  private static Map<String, Role> roleMap;

  private static boolean isInit = false;

  @Autowired
  public RoleService(RoleRepository roleRepo) {
    this.roleRepo = roleRepo;
  }

  /**
   * 懒加载数据
   */
  private void initData() {
    Map<String, Role> map = new HashMap<String, Role>(3) {
      {
        put("ordinary", roleRepo.existsByRoleName("ordinary") ? roleRepo.findByRoleName("ordinary") : roleRepo.save(new Role(null, "ordinary")));
        put("vip", roleRepo.existsByRoleName("vip") ? roleRepo.findByRoleName("vip") : roleRepo.save(new Role(null, "vip")));
        put("wx_user", roleRepo.existsByRoleName("wx_user") ? roleRepo.findByRoleName("wx_user") : roleRepo.save(new Role(null, "wx_user")));
      }
    };
    roleMap = Collections.unmodifiableMap(map);
  }

  public Role getByName(String roleName) {
    if (!isInit) {
      synchronized (this) {
        isInit = true;
      }
      initData();
    }
    return roleMap.get(roleName);
  }


}
