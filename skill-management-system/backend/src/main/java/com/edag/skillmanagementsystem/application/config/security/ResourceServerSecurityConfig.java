package com.edag.skillmanagementsystem.application.config.security;

import com.edag.skillmanagementsystem.application.security.CustomAccessDeniedHandler;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;

/**
 * Configuration class for the resource server security.
 *
 * <p>Sets up CORS, CSRF, security headers, session management, and OAuth2 resource server with
 * opaque token introspection.
 */
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
@EnableWebSecurity
public class ResourceServerSecurityConfig {

  private final CorsSecurityConfig corsSecurityConfig;
  private final KeycloakAuthoritiesConverter authoritiesConverter;
  private final CustomAccessDeniedHandler customAccessDeniedHandler;

  /**
   * Configures common security settings for all HTTP requests.
   *
   * <p>Includes CSRF, CORS, session management, security headers, and authorization rules for
   * public and protected endpoints.
   *
   * @param http the {@link HttpSecurity} instance to configure
   * @throws Exception if any error occurs while configuring HttpSecurity
   */
  private void configureCommonSecurity(HttpSecurity http) throws Exception {
    http
        // Disable CSRF for stateless API
        .csrf(AbstractHttpConfigurer::disable)

        // Configure CORS
        .cors(cors -> cors.configurationSource(corsSecurityConfig.corsConfigurationSource()))

        // Configure session management
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

        // Configure security headers
        .headers(
            headers ->
                headers
                    .frameOptions(HeadersConfigurer.FrameOptionsConfig::deny)
                    .contentTypeOptions(contentTypeOptions -> {})
                    .httpStrictTransportSecurity(
                        hsts -> hsts.maxAgeInSeconds(31536000).includeSubDomains(true))
                    .referrerPolicy(
                        referrer ->
                            referrer.policy(
                                ReferrerPolicyHeaderWriter.ReferrerPolicy
                                    .STRICT_ORIGIN_WHEN_CROSS_ORIGIN)))

        // Configure authorization rules
        .authorizeHttpRequests(
            auth ->
                auth
                    // Public endpoints
                    .requestMatchers("/actuator/health")
                    .permitAll()
                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**")
                    .permitAll()
                    .requestMatchers("/error")
                    .permitAll()

                    // All other endpoints require authentication
                    .anyRequest()
                    .authenticated())

        // Configure custom access denied handler
        .exceptionHandling(ex -> ex.accessDeniedHandler(customAccessDeniedHandler));
  }

  /**
   * Configures the security filter chain for OAuth2 resource server using opaque tokens.
   *
   * <p>Uses a custom {@link KeycloakAuthoritiesConverter} to extract roles/authorities from the
   * token claims.
   *
   * @param http the {@link HttpSecurity} instance to configure
   * @param delegate the {@link OpaqueTokenIntrospector} delegate to introspect tokens
   * @return the configured {@link SecurityFilterChain} instance
   * @throws Exception if any error occurs while building the security filter chain
   */
  @Bean
  public SecurityFilterChain opaqueTokenFilterChain(
      HttpSecurity http, OpaqueTokenIntrospector delegate) throws Exception {

    configureCommonSecurity(http);

    return http.oauth2ResourceServer(
            oauth2 ->
                oauth2.opaqueToken(
                    token ->
                        token.introspector(
                            t -> {
                              OAuth2AuthenticatedPrincipal principal = delegate.introspect(t);
                              Map<String, Object> claims = principal.getAttributes();

                              String principalName = (String) claims.get("sub");
                              List<GrantedAuthority> authorities =
                                  authoritiesConverter.extractAuthoritiesFromClaims(claims);

                              return new DefaultOAuth2AuthenticatedPrincipal(
                                  principalName, claims, authorities);
                            })))
        .build();
  }
}
