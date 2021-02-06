package com.anju.residence.config;

import com.alibaba.druid.filter.FilterChain;
import com.alibaba.druid.filter.FilterEventAdapter;
import com.alibaba.druid.proxy.jdbc.ConnectionProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @author cygao
 * @date 2020/12/29 14:32
 **/
@Slf4j
@Component
public class DruidConnFilter extends FilterEventAdapter {

  @Override
  public void connection_connectBefore(FilterChain chain, Properties info) {
    log.info("BEFORE CONNECTION!");
  }

  @Override
  public void connection_connectAfter(ConnectionProxy connection) {
    log.info("AFTER CONNECTION!");
  }
}
