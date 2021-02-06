package com.anju.residence.controller.druid;

import com.alibaba.druid.stat.DruidStatManagerFacade;
import com.anju.residence.annotation.AnonymousAccess;
import io.swagger.annotations.Api;
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

//  @AnonymousAccess
  @GetMapping("/stat")
  public Object druidStat(){
    // DruidStatManagerFacade#getDataSourceStatDataList
    // 该方法可以获取所有数据源的监控数据，除此之外 DruidStatManagerFacade 还提供了一些其他方法，你可以按需选择使用。
    return DruidStatManagerFacade.getInstance().getDataSourceStatDataList();
  }

}
