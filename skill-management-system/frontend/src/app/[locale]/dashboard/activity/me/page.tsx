import { getTranslations } from 'next-intl/server';
import type { Metadata } from 'next';
import MyActivitiesPage from "@/screens/my-activities/my-activities-page";

interface MyActivitiesRouteProps {
  params: Promise<{
    locale: string;
  }>;
}

export async function generateMetadata({ params }: MyActivitiesRouteProps): Promise<Metadata> {
  const { locale } = await params;
  const t = await getTranslations({ locale, namespace: 'myActivities.meta' });

  return {
    title: t('title'),
    description: t('description'),
  };
}

export default function MyActivitiesRoute() {
  return <MyActivitiesPage />;
}
