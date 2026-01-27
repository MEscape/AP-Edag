package com.edag.skillmanagementsystem;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class SkillManagementSystemApplicationTests {

  @Test
  void contextLoads() {
    // This test ensures that the Spring application context loads successfully.
  }
}
