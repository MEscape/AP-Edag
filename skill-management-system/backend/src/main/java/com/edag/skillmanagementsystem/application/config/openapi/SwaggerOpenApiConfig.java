package com.edag.skillmanagementsystem.application.config.openapi;

import com.edag.skillmanagementsystem.infrastructure.properties.SpringDocProperties;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Swagger / OpenAPI documentation.
 *
 * <p>Sets up OpenAPI with application info and security schemes for OAuth2 and Bearer JWT tokens.
 */
@Configuration
@RequiredArgsConstructor
public class SwaggerOpenApiConfig {

  private final SpringDocProperties springDocProperties;

  /**
   * Creates the main {@link OpenAPI} bean for the application.
   *
   * <p>Configures API info, components (security schemes) and global security requirements
   *
   * @return a configured {@link OpenAPI} instance
   */
  @Bean
  public OpenAPI openApi() {
    SpringDocProperties.ApiDocsProperties apiDocs = springDocProperties.getApiDocs();

    return new OpenAPI()
        .info(
            new Info()
                .title(apiDocs.getTitle())
                .description(apiDocs.getDescription())
                .version(apiDocs.getAppVersion()))
        .components(
            new Components()
                .addSecuritySchemes("oauth2", createOauth2SecurityScheme())
                .addSecuritySchemes("bearerAuth", createBearerTokenSecurityScheme()))
        .addSecurityItem(new SecurityRequirement().addList("oauth2"))
        .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
  }

  /**
   * Creates the OAuth2 security scheme for Swagger/OpenAPI.
   *
   * <p>Uses authorization code flow with dynamic scopes from configuration.
   *
   * @return a configured {@link SecurityScheme} for OAuth2
   */
  private SecurityScheme createOauth2SecurityScheme() {
    SpringDocProperties.OauthProperties oauth = springDocProperties.getSwaggerUi().getOauth();

    // Build scopes dynamically
    Scopes scopes = new Scopes();
    if (oauth.getScopes() != null) {
      oauth
          .getScopes()
          .forEach(scope -> scopes.addString(scope, scope)); // scope name as description
    }

    return new SecurityScheme()
        .type(SecurityScheme.Type.OAUTH2)
        .description("OAuth2 Authorization Code Flow")
        .flows(
            new OAuthFlows()
                .authorizationCode(
                    new OAuthFlow()
                        .authorizationUrl(oauth.getAuthorizationUrl())
                        .tokenUrl(oauth.getTokenUrl())
                        .scopes(scopes)));
  }

  /**
   * Creates the Bearer token security scheme for Swagger/OpenAPI.
   *
   * <p>Configures HTTP Bearer authentication with JWT format.
   *
   * @return a configured {@link SecurityScheme} for JWT Bearer tokens
   */
  private SecurityScheme createBearerTokenSecurityScheme() {
    return new SecurityScheme()
        .type(SecurityScheme.Type.HTTP)
        .scheme("bearer")
        .bearerFormat("JWT")
        .description("JWT Bearer Token");
  }
}
