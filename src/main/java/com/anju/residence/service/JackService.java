package com.anju.residence.service;

import com.anju.residence.dao.JackRepository;
import com.anju.residence.entity.Jack;
import org.springframework.stereotype.Service;

/**
 * @author cygao
 * @date 2020/11/24 16:18
 **/
@Service
public class JackService {

  private final JackRepository jackRepo;

  public JackService(JackRepository jackRepo) {
    this.jackRepo = jackRepo;
  }

  public void save(Jack jack) {
    jackRepo.save(jack);
  }
}
