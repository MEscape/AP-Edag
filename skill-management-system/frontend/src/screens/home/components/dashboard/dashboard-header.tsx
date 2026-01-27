'use client';

import { useTranslations } from 'next-intl';
import { useSession } from 'next-auth/react';
import { RefreshCw } from 'lucide-react';
import { Badge } from '@/components/ui/badge';
import { Button } from '@/components/ui/button';
import { RoleBadge } from '../role-requests/role-badge';
import { useDashboardRefresh } from '../../hooks/use-dashboard-refresh';

export const DashboardHeader = () => {
  const t = useTranslations('dashboard');
  const { data: session } = useSession();
  const { isRefreshing, refreshAll } = useDashboardRefresh();

  return (
      <section className="relative overflow-hidden rounded-xl border border-border bg-white shadow-sm">
        <div className="absolute inset-0 bg-gradient-to-br from-primary/[0.02] via-transparent to-transparent" />
        <div className="absolute right-0 top-0 h-[300px] w-[500px] bg-gradient-to-bl from-primary/5 to-transparent blur-3xl" />

        <div className="relative px-8 py-10 lg:px-12 lg:py-12">
          <div className="flex flex-col gap-6 lg:flex-row lg:items-center lg:justify-between">
            <div>
              <div className="mb-4 flex flex-wrap items-center gap-2">
                <Badge
                    variant="outline"
                    className="border-0 bg-primary/10 px-3 py-1.5 text-xs font-semibold uppercase tracking-wide text-primary"
                >
                  {t('title')}
                </Badge>
                <RoleBadge />
              </div>
              <h1 className="mb-3 text-4xl font-bold leading-tight tracking-tight text-foreground lg:text-5xl">
                {session?.user.name ? t('welcomeBack', { name: session.user.name }) : t('welcome')}
              </h1>
              <p className="text-lg text-muted-foreground">{t('overview')}</p>
            </div>

            <Button
                variant="outline"
                size="lg"
                onClick={refreshAll}
                disabled={isRefreshing}
                className="h-12 gap-2 border-border bg-white px-6 font-semibold transition-all hover:border-primary/20 hover:shadow-md"
            >
              <RefreshCw className={`size-5 ${isRefreshing ? 'animate-spin' : ''}`} />
              {t('refresh')}
            </Button>
          </div>
        </div>
      </section>
  );
};
