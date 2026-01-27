import { DashboardHeader } from './components/dashboard/dashboard-header';
import { StatsSection } from './components/sections/stats-section';
import { OverviewSection } from './components/sections/overview-section';
import { RoleRequestsSection } from './components/sections/role-requests-section';
import { RecentActivitySection } from './components/sections/recent-activity-section';

const HomePage = () => {
  return (
      <div className="min-h-screen bg-muted/30">
        <div className="pointer-events-none absolute inset-0 bg-gradient-to-br from-primary/[0.02] via-transparent to-transparent" />

        <div className="relative mx-auto max-w-[1440px] space-y-8 px-6 py-8 lg:px-16 lg:py-12">
          <DashboardHeader />

          <StatsSection />
          <OverviewSection />
          <RoleRequestsSection />
          <RecentActivitySection />
        </div>
      </div>
  );
};

export default HomePage;