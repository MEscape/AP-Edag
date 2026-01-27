'use client';

import { useTranslations } from 'next-intl';
import { Award, Briefcase, Star, TrendingUp } from 'lucide-react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import type { UserStatistics } from '@/store/api/dashboard-api';

interface DashboardCardsProps {
  stats?: UserStatistics;
}

export const DashboardCards = ({ stats }: DashboardCardsProps) => {
  const t = useTranslations('dashboard.stats');

  const cards = [
    {
      title: t('totalSkills'),
      value: stats?.totalSkills ?? 0,
      icon: Award,
    },
    {
      title: t('totalProjects'),
      value: stats?.totalProjects ?? 0,
      icon: Briefcase,
    },
    {
      title: t('averageSkillScore'),
      value: stats?.averageSkillScore ?? 0,
      icon: Star,
    },
    {
      title: t('totalRecommendations'),
      value: stats?.totalRecommendations ?? 0,
      icon: TrendingUp,
    },
  ];

  return (
      <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-4">
        {cards.map((card, index) => (
            <Card
                key={card.title}
                className="group relative overflow-hidden border-border bg-white transition-all hover:border-primary/20 hover:shadow-lg"
                style={{ animationDelay: `${index * 50}ms` }}
            >
              <div className="absolute inset-0 bg-gradient-to-br from-primary/10 via-primary/5 to-transparent opacity-0 transition-opacity group-hover:opacity-100" />

              <CardHeader className="relative flex flex-row items-center justify-between pb-4">
                <CardTitle className="text-sm font-semibold uppercase tracking-wide text-muted-foreground">
                  {card.title}
                </CardTitle>
                <div className="flex size-14 items-center justify-center rounded-lg bg-primary/10 transition-all group-hover:scale-110">
                  <card.icon className="size-7 text-primary" strokeWidth={2} />
                </div>
              </CardHeader>
              <CardContent className="relative">
                <div className="text-4xl font-bold tracking-tight text-foreground">{card.value}</div>
              </CardContent>
            </Card>
        ))}
      </div>
  );
};