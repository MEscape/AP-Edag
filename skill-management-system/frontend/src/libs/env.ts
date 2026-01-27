import { createEnv } from '@t3-oss/env-nextjs';
import * as z from 'zod';

export const Env = createEnv({
  client: {
    NEXT_PUBLIC_APP_URL: z.url().optional(),
    NEXT_PUBLIC_API_URL: z.url(),
  },
  server: {
    // NextAuth.js
    NEXTAUTH_URL: z.url(),
    NEXTAUTH_SECRET: z.string().min(32),

    // Keycloak
    KEYCLOAK_ISSUER: z.url(),
    KEYCLOAK_CLIENT_ID: z.string().min(1),
    KEYCLOAK_CLIENT_SECRET: z.string().min(1),
  },
  shared: {
    NODE_ENV: z.enum(['test', 'development', 'production']).default('development'),
  },
  // You need to destructure all the keys manually
  runtimeEnv: {
    // Client
    NEXT_PUBLIC_APP_URL: process.env.NEXT_PUBLIC_APP_URL,
    NEXT_PUBLIC_API_URL: process.env.NEXT_PUBLIC_API_URL,

    // Server
    NEXTAUTH_URL: process.env.NEXTAUTH_URL,
    NEXTAUTH_SECRET: process.env.NEXTAUTH_SECRET,
    KEYCLOAK_ISSUER: process.env.KEYCLOAK_ISSUER,
    KEYCLOAK_CLIENT_ID: process.env.KEYCLOAK_CLIENT_ID,
    KEYCLOAK_CLIENT_SECRET: process.env.KEYCLOAK_CLIENT_SECRET,

    // Shared
    NODE_ENV: process.env.NODE_ENV,
  },
  skipValidation: !!process.env.SKIP_ENV_VALIDATION,
});
