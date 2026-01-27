import { hasLocale, NextIntlClientProvider } from 'next-intl';
import { setRequestLocale } from 'next-intl/server';
import { notFound } from 'next/navigation';
import React from 'react';
import { ReduxProvider } from '@/components/providers/redux-provider';
import { SessionProvider } from '@/components/providers/session-provider';
import { Toaster } from '@/components/ui/toaster';
import { routing } from '@/libs/i18nRouting';
import '@/styles/globals.css';
import localFont from 'next/font/local';
import {SessionChecker} from "@/components/providers/session-checker";

export { generateStaticParams, metadata } from './layout.constants';

const hind = localFont({
  src: [
    {
      path: '../fonts/hind/hind-v18-latin-400.woff2',
      weight: '400',
      style: 'normal',
    },
    {
      path: '../fonts/hind/hind-v18-latin-600.woff2',
      weight: '600',
      style: 'normal',
    },
    {
      path: '../fonts/hind/hind-v18-latin-700.woff2',
      weight: '700',
      style: 'normal',
    },
  ],
  variable: '--font-hind',
  fallback: ['arial', 'sans-serif'],
});

export default async function RootLayout(
    props: Readonly<{
      children: React.ReactNode;
      params: Promise<{ readonly locale: string }>;
    }>
) {
  const { locale } = await props.params;

  if (!hasLocale(routing.locales, locale)) {
    notFound();
  }

  setRequestLocale(locale);

  return (
    <html lang={locale} className={hind.variable}>
      <body className={hind.className}>
        <SessionProvider>
            <ReduxProvider>
              <NextIntlClientProvider>
                <SessionChecker>
                  {props.children}
                </SessionChecker>
              </NextIntlClientProvider>
            </ReduxProvider>
            <Toaster richColors position="top-right" />
        </SessionProvider>
      </body>
    </html>
  );
}
