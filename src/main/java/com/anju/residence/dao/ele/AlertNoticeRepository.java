package com.anju.residence.dao.ele;

import com.anju.residence.entity.ele.AlertNotice;
import com.anju.residence.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author cygao
 * @date 2020/11/23 10:46
 **/
@Repository
public interface AlertNoticeRepository extends CrudRepository<AlertNotice, Integer> {

  AlertNotice findByUser(User user);

  boolean existsByUser(User user);
}
