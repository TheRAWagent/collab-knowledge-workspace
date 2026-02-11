package com.dj.ckw.workspaceservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class WorkspaceServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(WorkspaceServiceApplication.class, args);
  }

}
