package com.edag.skillmanagementsystem.application.config.security;

import com.edag.skillmanagementsystem.infrastructure.properties.SecurityProperties;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Cross-Origin Resource Sharing (CORS) configuration for the Skill Management application.
 *
 * <h3>CORS Configuration Details</h3>
 *
 * <ul>
 *   <li><strong>Allowed Origins:</strong> Configured via application properties for
 *       environment-specific control
 *   <li><strong>Allowed Methods:</strong> GET, POST, PUT, DELETE, OPTIONS for full REST API support
 *   <li><strong>Allowed Headers:</strong> Authorization, Content-Type, Accept for JWT and JSON
 *       support
 *   <li><strong>Credentials:</strong> Enabled to support authenticated cross-origin requests
 *   <li><strong>Max Age:</strong> 1 hour caching to reduce preflight requests
 * </ul>
 */
@Configuration
@RequiredArgsConstructor
public class CorsSecurityConfig {

  private final SecurityProperties securityProperties;

  /**
   * Configures Cross-Origin Resource Sharing (CORS) for the SafeNet application.
   *
   * @return the configured CorsConfigurationSource
   */
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    SecurityProperties.CorsProperties corsProps = securityProperties.getCors();

    // Set allowed origins from configuration
    configuration.setAllowedOrigins(corsProps.getAllowedOrigins());

    // Set allowed methods from configuration
    configuration.setAllowedMethods(corsProps.getAllowedMethods());

    // Set allowed headers from configuration
    configuration.setAllowedHeaders(corsProps.getAllowedHeaders());

    // Allow credentials from configuration
    configuration.setAllowCredentials(corsProps.isAllowCredentials());

    // Set max age for preflight requests from configuration
    configuration.setMaxAge(Duration.ofSeconds(corsProps.getMaxAge()));

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);

    return source;
  }
}
