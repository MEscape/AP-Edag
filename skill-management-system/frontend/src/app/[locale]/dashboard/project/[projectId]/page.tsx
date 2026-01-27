import { getTranslations } from 'next-intl/server';
import type { Metadata } from 'next';
import ProjectDetailPage from "@/screens/project-details/project-details-page";

interface ProjectDetailRouteProps {
  params: Promise<{
    locale: string;
    projectId: string;
  }>;
}

export async function generateMetadata({ params }: ProjectDetailRouteProps): Promise<Metadata> {
  const { locale } = await params;

  const t = await getTranslations({ locale, namespace: 'projectDetail.meta' });

  return {
    title: t('title'),
    description: t('description'),
  };
}

export default async function ProjectDetailRoute({ params }: ProjectDetailRouteProps) {
  const { projectId } = await params;
  return <ProjectDetailPage projectId={projectId} />;
}
