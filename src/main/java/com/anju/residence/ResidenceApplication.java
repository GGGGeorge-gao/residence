package com.anju.residence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class ResidenceApplication {

  public static void main(String[] args) {
    SpringApplication.run(ResidenceApplication.class, args);
  }

}
