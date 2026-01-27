package com.edag.skillmanagementsystem.infrastructure.properties;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * Configuration properties for application security settings.
 *
 * <p>Binds properties prefixed with {@code skill-management.security} from application
 * configuration files. Manages CORS configuration and webhook authentication credentials for secure
 * system communication.
 *
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = "skill-management.security")
@Validated
@Component
@Data
public class SecurityProperties {

  /** CORS (Cross-Origin Resource Sharing) configuration. */
  @Valid @NotNull private CorsProperties cors = new CorsProperties();

  /** Webhook authentication credentials configuration. */
  @Valid @NotNull private WebhookProperties webhook = new WebhookProperties();

  /** CORS configuration properties. */
  @Data
  public static class CorsProperties {

    /** List of allowed origins for CORS requests. */
    @NotEmpty(message = "At least one allowed origin must be specified")
    private List<@NotBlank String> allowedOrigins;

    /** List of allowed HTTP methods for CORS requests. */
    @NotEmpty(message = "At least one allowed method must be specified")
    private List<@NotBlank String> allowedMethods;

    /** List of allowed headers for CORS requests. */
    @NotEmpty(message = "At least one allowed header must be specified")
    private List<@NotBlank String> allowedHeaders;

    /** Whether to allow credentials in CORS requests. */
    private boolean allowCredentials;

    /** Maximum age in seconds for preflight request caching. */
    @Positive(message = "Max age must be positive")
    private long maxAge;
  }

  /**
   * Webhook authentication configuration properties.
   *
   * <p>Defines credentials used to secure incoming webhook requests, such as those from Keycloak.
   * These values should be kept confidential and loaded securely from environment variables or
   * externalized configuration sources.
   */
  @Data
  public static class WebhookProperties {

    /** Username used for basic authentication of incoming webhook requests. */
    @NotBlank(message = "Webhook username must not be blank")
    private String username;

    /** Password used for basic authentication of incoming webhook requests. */
    @NotBlank(message = "Webhook password must not be blank")
    private String password;
  }
}
