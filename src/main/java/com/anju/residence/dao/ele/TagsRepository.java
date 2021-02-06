package com.anju.residence.dao.ele;

import com.anju.residence.entity.ele.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author cygao
 * @date 2020/11/28 14:43
 **/
@Repository
public interface TagsRepository extends JpaRepository<Tags, Integer> {

}
