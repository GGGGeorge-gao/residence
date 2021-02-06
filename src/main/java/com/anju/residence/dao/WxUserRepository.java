package com.anju.residence.dao;

import com.anju.residence.entity.WxUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WxUserRepository extends JpaRepository<WxUser, String> {
}
