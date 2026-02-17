package com.dj.ckw.authservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class AuthServiceApplication {

  static void main(String[] args) {
    SpringApplication.run(AuthServiceApplication.class, args);
  }

}
