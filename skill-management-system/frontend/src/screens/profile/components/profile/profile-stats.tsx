'use client';

import { useTranslations } from 'next-intl';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Trophy, Briefcase, Star, Target } from 'lucide-react';
import type { UserProfile } from '@/store/api/profile-api';
import type { LucideIcon } from 'lucide-react';

interface ProfileStatsProps {
    profile?: UserProfile;
}

interface StatConfig {
    label: string;
    value: string | number;
    icon: LucideIcon;
}

export const ProfileStats = ({ profile }: ProfileStatsProps) => {
    const t = useTranslations('profile.stats');

    if (!profile) {
        return null;
    }

    const stats: StatConfig[] = [
        {
            label: t('totalSkills'),
            value: profile.totalSkills,
            icon: Star,
        },
        {
            label: t('averageScore'),
            value: profile.averageSkillScore.toFixed(1),
            icon: Target,
        },
        {
            label: t('totalProjects'),
            value: profile.totalProjects,
            icon: Briefcase,
        },
        {
            label: t('activeProjects'),
            value: profile.activeProjects,
            icon: Trophy,
        },
    ];

    return (
        <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-4">
            {stats.map((stat, index) => (
                <Card
                    key={stat.label}
                    className="group relative overflow-hidden border-border bg-white transition-all hover:border-primary/20 hover:shadow-lg"
                    style={{ animationDelay: `${index * 50}ms` }}
                >
                    <div className="absolute inset-0 bg-gradient-to-br from-primary/10 via-primary/5 to-transparent opacity-0 transition-opacity group-hover:opacity-100" />

                    <CardHeader className="relative flex flex-row items-center justify-between pb-4">
                        <CardTitle className="text-sm font-semibold uppercase tracking-wide text-muted-foreground">
                            {stat.label}
                        </CardTitle>
                        <div className="flex size-10 items-center justify-center rounded-lg bg-primary/10 transition-all group-hover:scale-110">
                            <stat.icon className="size-5 text-primary" strokeWidth={2} />
                        </div>
                    </CardHeader>

                    <CardContent className="relative">
                        <div className="text-4xl font-bold tracking-tight text-foreground">
                            {stat.value}
                        </div>
                    </CardContent>
                </Card>
            ))}
        </div>
    );
};