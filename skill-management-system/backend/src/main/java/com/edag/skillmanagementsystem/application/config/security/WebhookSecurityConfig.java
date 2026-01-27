package com.edag.skillmanagementsystem.application.config.security;

import com.edag.skillmanagementsystem.infrastructure.properties.SecurityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for webhook endpoints.
 *
 * <p>This configuration defines Basic Authentication specifically for {@code /v1/webhooks/**}
 * endpoints. It is given higher priority using {@link Order @Order(1)} to ensure these endpoints
 * are secured separately from OAuth2-protected API endpoints.
 *
 * @since 1.0.0
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebhookSecurityConfig {

  private final SecurityProperties securityProperties;

  /**
   * Defines a security filter chain dedicated to webhook endpoints.
   *
   * <p>This chain enforces Basic Authentication with stateless sessions and disables CSRF
   * protection (not needed for machine-to-machine communication).
   *
   * @param http the {@link HttpSecurity} builder used to configure security
   * @return a configured {@link SecurityFilterChain} for webhook endpoints
   * @throws Exception if configuration fails
   */
  @Bean
  @Order(1)
  public SecurityFilterChain webhookSecurityFilterChain(HttpSecurity http) throws Exception {
    http.securityMatcher("/v1/webhooks/**")
        .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
        .httpBasic(basic -> {})
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    return http.build();
  }

  /**
   * In-memory user details service for authenticating webhook requests.
   *
   * <p>Credentials are loaded from {@link SecurityProperties.WebhookProperties}, which are bound
   * from application configuration (e.g., {@code application.yml}).
   *
   * @return a {@link UserDetailsService} containing a single webhook user
   */
  @Bean
  public UserDetailsService webhookUserDetailsService() {
    var webhookAuth = securityProperties.getWebhook();

    UserDetails user =
        User.builder()
            .username(webhookAuth.getUsername())
            .password(passwordEncoder().encode(webhookAuth.getPassword()))
            .roles("WEBHOOK")
            .build();

    return new InMemoryUserDetailsManager(user);
  }

  /**
   * Provides a {@link PasswordEncoder} bean for encoding and verifying passwords.
   *
   * <p>Uses the {@link BCryptPasswordEncoder}, which applies an adaptive hashing algorithm
   * resistant to brute-force attacks.
   *
   * @return a {@link PasswordEncoder} instance based on {@link BCryptPasswordEncoder}
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
