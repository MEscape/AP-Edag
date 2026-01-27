/* eslint-disable ts/consistent-type-definitions */
import 'next-auth';
import 'next-auth/jwt';

declare module 'next-auth' {
  /**
   * Extended session with Keycloak user information
   */
  interface Session {
    user: {
      id: string;
      email: string;
      emailVerified: boolean;
      username: string;
      firstName: string;
      lastName: string;
      name: string;
      roles: string[];
      groups: string[];
    };
    accessToken: string;
    refreshToken: string;
    expiresAt: number;
    error?: string;
  }

  /**
   * Keycloak user profile from the ID token
   */
  interface Profile {
    sub: string;
    email: string;
    email_verified: boolean;
    preferred_username: string;
    given_name: string;
    family_name: string;
    name: string;
    realm_access?: {
      roles: string[];
    };
    groups?: string[];
  }
}

declare module 'next-auth/jwt' {
  /**
   * Extended JWT token with Keycloak information
   */
  interface JWT {
    id?: string;
    email?: string;
    emailVerified?: boolean;
    username?: string;
    firstName?: string;
    lastName?: string;
    name?: string;
    roles?: string[];
    groups?: string[];
    accessToken?: string;
    refreshToken?: string;
    idToken?: string;
    expiresAt?: number;
    error?: string;
  }
}
