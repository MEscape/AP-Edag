import { Metadata } from 'next';
import { getTranslations } from 'next-intl/server';
import ProjectsPage from "@/screens/discover-projects/discover-projects-page";
import {Suspense} from "react";

interface ProjectsRouteProps {
  params: Promise<{
    locale: string;
  }>;
}

export async function generateMetadata({ params }: ProjectsRouteProps): Promise<Metadata> {
  const { locale } = await params;
  const t = await getTranslations({ locale, namespace: 'discoverProjects.meta' });

  return {
    title: t('title'),
    description: t('description'),
  };
}

export default function ProjectsRoute() {
  return (
      <Suspense fallback={null}>
        <ProjectsPage />
      </Suspense>
  )
}
