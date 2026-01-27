import type { MetadataRoute } from 'next';
import { getBaseUrl } from '@/utils/helpers';

export default function robots(): MetadataRoute.Robots {
  const locales = ['de', 'en'];
  const protectedRoutes = [
    'dashboard',
    'profile',
    'my-projects',
    'my-activities',
    'discover-employees',
    'discover-projects',
    'project-details',
    'admin',
  ];

  // Generate disallow patterns for all locales and protected routes
  const disallowPatterns = [
    '/api/', // all API routes
    ...protectedRoutes.flatMap(route => [
      `/${route}`,
      ...locales.map(locale => `/${locale}/${route}`),
    ]),
  ];

  return {
    rules: [
      {
        userAgent: '*',
        allow: '/',
        disallow: disallowPatterns,
      },
    ],
    sitemap: `${getBaseUrl()}/sitemap.xml`,
  };
}
