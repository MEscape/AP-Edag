import { Target, TrendingUp, Users } from 'lucide-react';
import { useTranslations } from 'next-intl';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';

type Feature = {
  id: string;
  title: string;
  description: string;
};

const iconMap = {
  'skill-tracking': Target,
  'team-collaboration': Users,
  'career-growth': TrendingUp,
};

export const FeaturesSection = () => {
  const t = useTranslations('welcome.features');

  const features: Feature[] = [
    {
      id: 'skill-tracking',
      title: t('skillTracking'),
      description: t('skillTrackingDesc'),
    },
    {
      id: 'team-collaboration',
      title: t('teamCollaboration'),
      description: t('teamCollaborationDesc'),
    },
    {
      id: 'career-growth',
      title: t('careerGrowth'),
      description: t('careerGrowthDesc'),
    },
  ];

  return (
      <section className="border-t border-border bg-muted/30 py-20 lg:py-28">
        <div className="mx-auto max-w-[1440px] px-6 lg:px-16">
          <div className="mb-16 text-center">
            <h2 className="mb-4 text-3xl font-bold tracking-tight text-foreground lg:text-4xl">
              {t('title')}
            </h2>
            <p className="mx-auto max-w-2xl text-lg text-muted-foreground">
              {t('description')}
            </p>
          </div>

          <div className="grid gap-8 md:grid-cols-3">
            {features.map((feature) => {
              const IconComponent = iconMap[feature.id as keyof typeof iconMap];
              return (
                  <Card
                      key={feature.id}
                      className="group border-border bg-white transition-all hover:border-primary/20 hover:shadow-lg"
                  >
                    <CardHeader className="space-y-4 pb-4">
                      <div className="flex size-14 items-center justify-center rounded-lg bg-primary/10 transition-colors group-hover:bg-primary/20">
                        <IconComponent className="size-7 text-primary" strokeWidth={2} />
                      </div>
                      <CardTitle className="text-xl font-bold text-foreground">
                        {feature.title}
                      </CardTitle>
                    </CardHeader>
                    <CardContent>
                      <CardDescription className="text-base leading-relaxed text-muted-foreground">
                        {feature.description}
                      </CardDescription>
                    </CardContent>
                  </Card>
              );
            })}
          </div>
        </div>
      </section>
  );
};