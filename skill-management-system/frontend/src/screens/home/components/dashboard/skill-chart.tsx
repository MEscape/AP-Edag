'use client';

import { useTranslations } from 'next-intl';
import { Area, AreaChart, CartesianGrid, XAxis, YAxis } from 'recharts';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import {
  ChartContainer,
  ChartTooltip,
  type ChartConfig,
} from '@/components/ui/chart';
import type { SkillDevelopmentDataPoint } from '@/store/api/dashboard-api';

interface SkillChartProps {
  data?: SkillDevelopmentDataPoint[];
}

export const SkillChart = ({ data }: SkillChartProps) => {
  const t = useTranslations('dashboard.skillDevelopment');

  const chartConfig = {
    totalSkills: {
      label: t('totalSkills'),
      color: 'hsl(var(--chart-1))',
    },
    skillsRemoved: {
      label: t('skillsDeleted'),
      color: 'hsl(var(--chart-5))',
    },
    skillsAdded: {
      label: t('skillsAdded'),
      color: 'hsl(var(--chart-2))',
    },
    skillsUpdated: {
      label: t('skillsUpdated'),
      color: 'hsl(var(--chart-3))',
    },
  } satisfies ChartConfig;

  return (
      <Card className="group col-span-1 border-border bg-white shadow-edag transition-all duration-300 hover:border-primary/30 hover:shadow-edag-md lg:col-span-2">
        <CardHeader className="space-y-2 border-b border-border/50 pb-5">
          <div className="flex items-start justify-between">
            <div className="space-y-1.5">
              <CardTitle className="text-xl font-semibold tracking-tight text-foreground">
                {t('title')}
              </CardTitle>
              <CardDescription className="text-sm leading-relaxed text-muted-foreground">
                {t('description')}
              </CardDescription>
            </div>
            <div className="flex gap-4 text-xs font-medium">
              <div className="flex items-center gap-2">
                <div className="h-2.5 w-2.5 rounded-full bg-[hsl(var(--chart-1))]" />
                <span className="text-muted-foreground">{t('totalSkills')}</span>
              </div>
              <div className="flex items-center gap-2">
                <div className="h-2.5 w-2.5 rounded-full bg-[hsl(var(--chart-5))]" />
                <span className="text-muted-foreground">{t('skillsDeleted')}</span>
              </div>
              <div className="flex items-center gap-2">
                <div className="h-2.5 w-2.5 rounded-full bg-[hsl(var(--chart-2))]" />
                <span className="text-muted-foreground">{t('skillsAdded')}</span>
              </div>
              <div className="flex items-center gap-2">
                <div className="h-2.5 w-2.5 rounded-full bg-[hsl(var(--chart-3))]" />
                <span className="text-muted-foreground">{t('skillsUpdated')}</span>
              </div>
            </div>
          </div>
        </CardHeader>
        <CardContent className="pb-6 pt-6">
          <ChartContainer
              config={chartConfig}
              className="h-[340px] w-full [&_svg]:outline-none [&_svg]:focus:outline-none [&_*]:outline-none"
          >
            <AreaChart data={data} margin={{ top: 16, right: 16, left: -8, bottom: 0 }}>
              <defs>
                <linearGradient id="fillTotalSkills" x1="0" y1="0" x2="0" y2="1">
                  <stop offset="0%" stopColor="var(--color-totalSkills)" stopOpacity={0.25} />
                  <stop offset="100%" stopColor="var(--color-totalSkills)" stopOpacity={0.02} />
                </linearGradient>
                <linearGradient id="fillSkillsRemoved" x1="0" y1="0" x2="0" y2="1">
                  <stop offset="0%" stopColor="var(--color-skillsRemoved)" stopOpacity={0.25} />
                  <stop offset="100%" stopColor="var(--color-skillsRemoved)" stopOpacity={0.02} />
                </linearGradient>
                <linearGradient id="fillSkillsAdded" x1="0" y1="0" x2="0" y2="1">
                  <stop offset="0%" stopColor="var(--color-skillsAdded)" stopOpacity={0.25} />
                  <stop offset="100%" stopColor="var(--color-skillsAdded)" stopOpacity={0.02} />
                </linearGradient>
                <linearGradient id="fillSkillsUpdated" x1="0" y1="0" x2="0" y2="1">
                  <stop offset="0%" stopColor="var(--color-skillsUpdated)" stopOpacity={0.25} />
                  <stop offset="100%" stopColor="var(--color-skillsUpdated)" stopOpacity={0.02} />
                </linearGradient>
              </defs>
              <CartesianGrid strokeDasharray="3 3" stroke="hsl(var(--border))" opacity={0.4} />
              <XAxis
                  dataKey="month"
                  tickLine={false}
                  axisLine={false}
                  tickMargin={12}
                  tick={{ fontSize: 12, fill: 'hsl(var(--muted-foreground))' }}
              />
              <YAxis
                  tickLine={false}
                  axisLine={false}
                  tickMargin={12}
                  tick={{ fontSize: 12, fill: 'hsl(var(--muted-foreground))' }}
              />
              <ChartTooltip cursor={false} />
              <Area
                  type="monotone"
                  dataKey="totalSkills"
                  stroke="var(--color-totalSkills)"
                  strokeWidth={2.5}
                  fill="url(#fillTotalSkills)"
              />
              <Area
                  type="monotone"
                  dataKey="skillsRemoved"
                  stroke="var(--color-skillsRemoved)"
                  strokeWidth={2}
                  fill="url(#fillSkillsRemoved)"
              />
              <Area
                  type="monotone"
                  dataKey="skillsAdded"
                  stroke="var(--color-skillsAdded)"
                  strokeWidth={2}
                  fill="url(#fillSkillsAdded)"
              />
              <Area
                  type="monotone"
                  dataKey="skillsUpdated"
                  stroke="var(--color-skillsUpdated)"
                  strokeWidth={2}
                  fill="url(#fillSkillsUpdated)"
              />
            </AreaChart>
          </ChartContainer>
        </CardContent>
      </Card>
  );
};
