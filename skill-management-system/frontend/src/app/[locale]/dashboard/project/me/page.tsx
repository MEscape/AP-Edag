import { getTranslations } from 'next-intl/server';
import type { Metadata } from 'next';
import MyProjectsPage from "@/screens/my-projects/my-projects-page";

interface MyProjectsRouteProps {
  params: Promise<{
    locale: string;
    projectId: string;
  }>;
}

export async function generateMetadata({ params }: MyProjectsRouteProps): Promise<Metadata> {
  const { locale } = await params;

  const t = await getTranslations({ locale, namespace: 'myProjects.meta' });

  return {
    title: t('title'),
    description: t('description'),
  };
}

export default function MyProjectsRoute() {
  return <MyProjectsPage />;
}
