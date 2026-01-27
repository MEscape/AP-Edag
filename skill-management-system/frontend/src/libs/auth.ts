import type {NextAuthOptions} from 'next-auth';
import KeycloakProvider from 'next-auth/providers/keycloak';
import {Env} from '@/libs/env';
import {logger} from "@/libs/logger";

/**
 * Takes a token, and returns a new token with updated
 * `accessToken` and `accessTokenExpires`. If an error occurs,
 * returns the old token and an error property
 */
async function refreshAccessToken(token: any) {
  try {
    const url = `${Env.KEYCLOAK_ISSUER}/protocol/openid-connect/token`;

    const response = await fetch(url, {
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
      },
      method: "POST",
      body: new URLSearchParams({
        client_id: Env.KEYCLOAK_CLIENT_ID,
        client_secret: Env.KEYCLOAK_CLIENT_SECRET,
        grant_type: "refresh_token",
        refresh_token: token.refreshToken,
      }),
    });

    const refreshedTokens = await response.json();

    if (!response.ok) {
      logger.error('Token refresh failed:', refreshedTokens);
      throw refreshedTokens;
    }

    // Fetch updated user info to get the latest roles
    let updatedRoles = token.roles;
    let updatedGroups = token.groups;

    try {
      const userInfoResponse = await fetch(`${Env.KEYCLOAK_ISSUER}/protocol/openid-connect/userinfo`, {
        headers: {
          "Authorization": `Bearer ${refreshedTokens.access_token}`,
        },
      });

      if (userInfoResponse.ok) {
        const userInfo = await userInfoResponse.json();
        updatedRoles = userInfo.realm_access?.roles || token.roles;
        updatedGroups = userInfo.groups || token.groups;
        logger.info('Updated roles from userInfo', { roles: updatedRoles });
      }
    } catch (error) {
      logger.warn('Failed to fetch userInfo, using token roles', { error: String(error) });
    }

    return {
      ...token,
      accessToken: refreshedTokens.access_token,
      accessTokenExpires: Date.now() + refreshedTokens.expires_in * 1000,
      refreshToken: refreshedTokens.refresh_token ?? token.refreshToken,
      idToken: refreshedTokens.id_token ?? token.idToken,
      roles: updatedRoles,
      groups: updatedGroups,
      error: undefined, // Clear any previous errors
    };
  } catch (error) {
    logger.error('Error refreshing access token:' + error);

    return {
      ...token,
      error: "RefreshAccessTokenError",
    };
  }
}

export const authOptions: NextAuthOptions = {
  providers: [
    KeycloakProvider({
      clientId: Env.KEYCLOAK_CLIENT_ID,
      clientSecret: Env.KEYCLOAK_CLIENT_SECRET,
      issuer: Env.KEYCLOAK_ISSUER
    }),
  ],
  callbacks: {
    async jwt({ token, account, profile, trigger }) {
      // Initial sign in
      if (account && profile) {
        return {
          accessToken: account.access_token,
          accessTokenExpires: Date.now() + (account.expires_in as number) * 1000,
          refreshToken: account.refresh_token,
          idToken: account.id_token,
          id: profile.sub,
          email: profile.email,
          emailVerified: profile.email_verified,
          username: profile.preferred_username,
          firstName: profile.given_name,
          lastName: profile.family_name,
          name: profile.name,
          roles: profile.realm_access?.roles || [],
          groups: profile.groups || [],
        };
      }

      // Manual session update triggered (e.g., when role changes)
      // Force a token refresh to get latest profile data from Keycloak
      if (trigger === 'update') {
        logger.info('Manual session update triggered, refreshing token with profile update');
        return await refreshAccessToken(token);
      }

      // Return previous token if the access token has not expired yet
      if (Date.now() < (token.accessTokenExpires as number)) {
        return token;
      }

      // Access token has expired, try to update it
      return await refreshAccessToken(token);
    },
    async session({ session, token }) {
      // If there's a refresh error, return null to force sign out
      if (token.error === "RefreshAccessTokenError") {
        return {
          ...session,
          error: "RefreshAccessTokenError",
        } as any;
      }

      session.user = {
        id: token.id as string,
        email: token.email as string,
        emailVerified: token.emailVerified as boolean,
        username: token.username as string,
        firstName: token.firstName as string,
        lastName: token.lastName as string,
        name: token.name as string,
        roles: token.roles as string[],
        groups: token.groups as string[],
      };

      session.accessToken = token.accessToken as string;
      session.refreshToken = token.refreshToken as string;
      session.expiresAt = token.accessTokenExpires as number;
      session.error = token.error;

      return session;
    },
  },
  events: {
    async signOut({ token }) {
      if (token?.refreshToken) {
        try {
          const url = `${Env.KEYCLOAK_ISSUER}/protocol/openid-connect/logout`;
          await fetch(url, {
            method: 'POST',
            headers: {
              'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: new URLSearchParams({
              client_id: Env.KEYCLOAK_CLIENT_ID,
              client_secret: Env.KEYCLOAK_CLIENT_SECRET,
              refresh_token: token.refreshToken,
            }),
          });
        } catch (error) {
          logger.error('Error revoking token on signout: ' + error);
        }
      }
    },
  },
  pages: {
    signIn: '/',
    signOut: '/',
    error: '/',
  },
  session: {
    strategy: 'jwt',
    maxAge: 30 * 24 * 60 * 60, // 30 days
  },
  secret: Env.NEXTAUTH_SECRET,
};
