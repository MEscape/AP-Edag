import { DashboardNavigation } from '@/components/navigation/dashboard-navigation';
import type { ReactNode } from 'react';

interface DashboardLayoutProps {
  children: ReactNode;
}

export default function DashboardLayout({ children }: DashboardLayoutProps) {
  return (
    <>
      <DashboardNavigation />
      <main>{children}</main>
    </>
  );
}

