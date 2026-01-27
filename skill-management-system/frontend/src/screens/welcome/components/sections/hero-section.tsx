import { CheckCircle2 } from 'lucide-react';
import { useTranslations } from 'next-intl';
import type { Session } from 'next-auth';
import { Badge } from '@/components/ui/badge';
import { SkillVisualization } from '../skill-visualization';
import { HeroActions } from '../hero-actions';

type HeroSectionProps = {
  session: Session | null;
};

export const HeroSection = ({ session }: HeroSectionProps) => {
  const t = useTranslations('welcome.hero');

  const benefits: string[] = [
    t('benefits.skillOverview'),
    t('benefits.resourcePlanning'),
    t('benefits.dataDecisions'),
    t('benefits.reducedEffort'),
  ];

  return (
      <section className="relative overflow-hidden">
        <div className="absolute inset-0 bg-gradient-to-br from-primary/[0.02] via-transparent to-transparent" />
        <div className="absolute right-0 top-0 h-[500px] w-[600px] bg-gradient-to-bl from-primary/5 to-transparent blur-3xl" />

        <div className="relative mx-auto max-w-[1440px] px-6 pb-24 pt-20 lg:px-16 lg:py-32">
          <div className="grid gap-16 lg:grid-cols-2 lg:gap-20">
            <div className="flex flex-col justify-center">
              <Badge variant="outline" className="mb-6 w-fit border-0 bg-primary/10 px-3 py-1.5 text-xs font-semibold uppercase tracking-wide text-primary">
                {t('badge')}
              </Badge>

              <h1 className="mb-6 text-4xl font-bold leading-tight tracking-tight text-foreground lg:text-5xl xl:text-6xl">
                {t('title')}
              </h1>

              <p className="mb-8 text-lg leading-relaxed text-muted-foreground lg:text-xl">
                {t('description')}
              </p>

              <div className="mb-10 space-y-3">
                {benefits.map((benefit) => (
                    <div key={benefit} className="flex items-start gap-3">
                      <CheckCircle2 className="mt-0.5 size-5 shrink-0 text-primary" strokeWidth={2} />
                      <span className="text-base text-foreground">{benefit}</span>
                    </div>
                ))}
              </div>

              <HeroActions
                  isAuthenticated={!!session}
                  getStartedLabel={t('getStarted')}
                  signInLabel={t('signIn')}
                  keycloakNote={t('keycloakNote')}
              />
            </div>

            <SkillVisualization />
          </div>
        </div>
      </section>
  );
};