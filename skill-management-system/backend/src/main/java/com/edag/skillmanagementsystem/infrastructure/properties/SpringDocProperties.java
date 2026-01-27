package com.edag.skillmanagementsystem.infrastructure.properties;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * Configuration properties for SpringDoc OpenAPI and Swagger UI.
 *
 * <p>Binds properties prefixed with {@code skill-management.spring-doc} from application
 * configuration files. Configures API documentation metadata, Swagger UI settings, and OAuth2
 * authentication.
 */
@ConfigurationProperties(prefix = "skill-management.spring-doc")
@Validated
@Component
@Data
public class SpringDocProperties {
  /** API documentation metadata configuration. */
  @Valid @NotNull private ApiDocsProperties apiDocs = new ApiDocsProperties();

  /** Swagger UI configuration. */
  @Valid @NotNull private SwaggerUiProperties swaggerUi = new SwaggerUiProperties();

  /** API documentation metadata properties. */
  @Data
  public static class ApiDocsProperties {

    /** The title of the API documentation. */
    @NotBlank(message = "API title cannot be blank")
    private String title;

    /** The description of the API. */
    @NotBlank(message = "API description cannot be blank")
    private String description;

    /** The version of the API. */
    @NotBlank(message = "API version cannot be blank")
    private String appVersion;
  }

  /** Swagger UI configuration properties. */
  @Data
  public static class SwaggerUiProperties {

    /** OAuth configuration for Swagger UI. */
    @Valid @NotNull private OauthProperties oauth = new OauthProperties();
  }

  /** OAuth configuration properties for Swagger UI. */
  @Data
  public static class OauthProperties {

    /** OAuth2 authorization endpoint URL. */
    @NotBlank(message = "Authorization URL cannot be blank")
    private String authorizationUrl;

    /** OAuth2 token endpoint URL. */
    @NotBlank(message = "Token URL cannot be blank")
    private String tokenUrl;

    /** List of OAuth2 scopes to request. */
    @NotEmpty(message = "At least one OAuth scope must be specified")
    private List<@NotBlank String> scopes;
  }
}
