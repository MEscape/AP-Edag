package com.edag.skillmanagementsystem.application.config.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * Converter that extracts Spring Security authorities from Keycloak JWT claims.
 *
 * <h3>Authority Mapping</h3>
 *
 * <ul>
 *   <li><strong>Realm Roles:</strong> Extracted from 'realm_access.roles' → ROLE_*
 *   <li><strong>Scopes:</strong> Extracted from 'scope' claim → SCOPE_*
 * </ul>
 */
@Component
public class KeycloakAuthoritiesConverter {

  /**
   * Extracts authorities from token claims map. Used in production environment with opaque token
   * introspection.
   *
   * @param claims the claims map from token introspection or JWT
   * @return list of granted authorities
   */
  public List<GrantedAuthority> extractAuthoritiesFromClaims(Map<String, Object> claims) {
    List<GrantedAuthority> authorities = new ArrayList<>();

    // Extract realm roles from realm_access.roles
    authorities.addAll(extractRealmRoles(claims));

    // Extract scopes
    authorities.addAll(extractScopes(claims));

    return authorities;
  }

  /**
   * Extracts realm roles from the realm_access claim.
   *
   * @param claims the token claims
   * @return list of authorities for realm roles
   */
  private List<GrantedAuthority> extractRealmRoles(Map<String, Object> claims) {
    List<GrantedAuthority> authorities = new ArrayList<>();

    Object realmAccess = claims.get("realm_access");
    if (realmAccess instanceof Map<?, ?> realmAccessMap) {
      Object rolesObj = realmAccessMap.get("roles");

      if (rolesObj instanceof Collection<?> rolesCollection) {
        authorities.addAll(
            rolesCollection.stream()
                .map(Object::toString)
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                .toList());
      }
    }

    return authorities;
  }

  /**
   * Extracts scopes from the scope claim. Handles both space-separated string format and collection
   * format.
   *
   * @param claims the token claims
   * @return list of authorities for scopes
   */
  private List<GrantedAuthority> extractScopes(Map<String, Object> claims) {
    List<GrantedAuthority> authorities = new ArrayList<>();

    Object scopeClaim = claims.get("scope");
    if (scopeClaim != null) {
      List<String> scopes = new ArrayList<>();

      if (scopeClaim instanceof String stringScope) {
        scopes.addAll(Arrays.asList(stringScope.split(" ")));
      } else if (scopeClaim instanceof Collection<?> collectionScope) {
        collectionScope.forEach(s -> scopes.add(s.toString()));
      }

      authorities.addAll(
          scopes.stream()
              .filter(s -> !s.isEmpty())
              .map(s -> new SimpleGrantedAuthority("SCOPE_" + s.toUpperCase()))
              .toList());
    }

    return authorities;
  }
}
