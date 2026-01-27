package com.edag.skillmanagementsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/** Main entry point for the Skill Management System application. */
@EnableJpaAuditing
@SpringBootApplication
public class SkillManagementSystemApplication {

  /**
   * Main method to start the Skill Management System application.
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {
    SpringApplication.run(SkillManagementSystemApplication.class, args);
  }
}
