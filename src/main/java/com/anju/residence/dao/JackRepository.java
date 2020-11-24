package com.anju.residence.dao;

import com.anju.residence.entity.Jack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author cygao
 * @date 2020/11/23 10:46
 **/
@Repository
public interface JackRepository extends JpaRepository<Jack, Integer> {
}
