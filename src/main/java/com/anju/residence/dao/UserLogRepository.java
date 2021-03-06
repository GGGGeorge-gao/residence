package com.anju.residence.dao;

import com.anju.residence.entity.UserLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author cygao
 * @date 2020/11/23 10:45
 **/
@Repository
public interface UserLogRepository extends JpaRepository<UserLog, Integer> {
}
