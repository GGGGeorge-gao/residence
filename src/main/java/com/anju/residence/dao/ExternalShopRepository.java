package com.anju.residence.dao;

import com.anju.residence.entity.ExternalShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author cygao
 * @date 2021/3/15
 **/
@Repository
public interface ExternalShopRepository extends JpaRepository<ExternalShop, Integer> {

}
