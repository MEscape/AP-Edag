package com.edag.skillmanagementsystem;

import org.springframework.boot.SpringApplication;

public class TestSkillManagementSystemApplication {

  public static void main(String[] args) {
    SpringApplication.from(SkillManagementSystemApplication::main)
        .with(TestcontainersConfiguration.class)
        .run(args);
  }
}
