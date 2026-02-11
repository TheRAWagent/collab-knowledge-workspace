package com.dj.ckw.authservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableCaching
@EnableJpaAuditing
public class AuthServiceApplication {

  static void main(String[] args) {
    SpringApplication.run(AuthServiceApplication.class, args);
  }

}
