import { Metadata } from 'next';
import { getTranslations } from 'next-intl/server';
import HomePage from '@/screens/home/home-page';

interface DashboardRouteProps {
  params: Promise<{
    locale: string;
  }>;
}

export async function generateMetadata({ params }: DashboardRouteProps): Promise<Metadata> {
  const { locale } = await params;
  const t = await getTranslations({ locale, namespace: 'dashboard.meta' });

  return {
    title: t('title'),
    description: t('description'),
  };
}

export default function DashboardRoute() {
  return <HomePage />;
}
