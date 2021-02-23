package com.anju.residence.controller.druid;

import com.alibaba.druid.stat.DruidStatManagerFacade;
import com.anju.residence.annotation.AnonymousAccess;
import com.anju.residence.annotation.OperationLog;
import com.anju.residence.enums.OperationType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cygao
 * @date 2020/12/29 11:16
 **/
@Api(tags = "druid数据库连接池监控API")
@RestController
@RequestMapping("/druid")
public class DruidStatController {

  @ApiOperation(value = "进入druid", tags = "无需权限认证，druid账号为anju 密码为root")
  @OperationLog(type = OperationType.OPERATION, description = "druid /stat")
  @GetMapping("/stat")
  public Object druidStat(){
    // DruidStatManagerFacade#getDataSourceStatDataList
    // 该方法可以获取所有数据源的监控数据，除此之外 DruidStatManagerFacade 还提供了一些其他方法，你可以按需选择使用。
    return DruidStatManagerFacade.getInstance().getDataSourceStatDataList();
  }

}
