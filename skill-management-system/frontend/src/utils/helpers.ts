import type { ClassValue } from 'clsx';
import { clsx } from 'clsx';
import { twMerge } from 'tailwind-merge';
import { Env } from '@/libs/env';
import { routing } from '@/libs/i18nRouting';

export const getBaseUrl = () => {
  if (Env.NEXT_PUBLIC_API_URL) {
    return Env.NEXT_PUBLIC_API_URL;
  }

  return 'http://localhost:3000';
};

export const getI18nPath = (url: string, locale: string) => {
  if (locale === routing.defaultLocale) {
    return url;
  }

  return `/${locale}${url}`;
};

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}
