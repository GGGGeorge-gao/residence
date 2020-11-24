package com.anju.residence.dao;

import com.anju.residence.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author cygao
 * @date 2020/11/23 10:44
 **/
@Repository
public interface DeviceRepository extends JpaRepository<Device, Integer> {
}
