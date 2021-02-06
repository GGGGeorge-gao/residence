package com.anju.residence.dao.water;

import com.anju.residence.entity.water.WaterOperationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 水表操作日志DAO
 *
 * @author cygao
 * @date 2021/1/25 14:33
 **/
@Repository
public interface WaterOperationLogRepository extends JpaRepository<WaterOperationLog, Integer> {


}
