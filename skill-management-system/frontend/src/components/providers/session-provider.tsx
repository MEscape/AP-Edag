'use client';

import {ReactNode} from 'react';
import {SessionProvider as NextAuthSessionProvider} from 'next-auth/react';

type SessionProviderProps = Readonly<{
  children: ReactNode;
}>;

export function SessionProvider({ children }: SessionProviderProps) {
  return (
    <NextAuthSessionProvider>
      {children}
    </NextAuthSessionProvider>
  );
}
