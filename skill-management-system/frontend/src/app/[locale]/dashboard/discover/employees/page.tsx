import { Metadata } from 'next';
import { getTranslations } from 'next-intl/server';
import EmployeeDiscoverPage from "@/screens/discover-employees/employee-discover-page";
import {Suspense} from "react";

interface DiscoverRouteProps {
  params: Promise<{
    locale: string;
  }>;
}

export async function generateMetadata({ params }: DiscoverRouteProps): Promise<Metadata> {
  const { locale } = await params;
  const t = await getTranslations({ locale, namespace: 'discoverEmployees.meta' });

  return {
    title: t('title'),
    description: t('description'),
  };
}

export default function DiscoverRoute() {
  return (
      <Suspense fallback={null}>
        <EmployeeDiscoverPage />
      </Suspense>
  );
}
