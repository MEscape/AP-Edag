import type { MetadataRoute } from 'next';
import { getBaseUrl } from '@/utils/helpers';

export default function sitemap(): MetadataRoute.Sitemap {
  const base = getBaseUrl();
  const locales = ['de', 'en'];
  const currentDate = new Date();

  const entries: MetadataRoute.Sitemap = [];

  // Public pages only (no authentication required)
  const publicPages = [
    { path: '', priority: 0.9, changeFrequency: 'daily' as const },
  ];

  // Root homepage
  entries.push({
    url: `${base}/`,
    lastModified: currentDate,
    changeFrequency: 'daily',
    priority: 1.0,
  });

  // Generate localized public pages
  locales.forEach((locale) => {
    publicPages.forEach(({ path, priority, changeFrequency }) => {
      const url = path ? `${base}/${locale}/${path}` : `${base}/${locale}`;
      entries.push({
        url,
        lastModified: currentDate,
        changeFrequency,
        priority,
      });
    });
  });

  return entries;
}
