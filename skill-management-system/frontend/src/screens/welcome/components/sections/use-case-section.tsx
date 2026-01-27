import { BarChart3, Target, TrendingUp } from 'lucide-react';
import { useTranslations } from 'next-intl';

export const UseCaseSection = () => {
  const t = useTranslations('welcome.useCases');

  const useCases = [
    {
      icon: Target,
      title: t('skillBasedSearch'),
      description: t('skillBasedSearchDesc'),
    },
    {
      icon: BarChart3,
      title: t('evaluationLogic'),
      description: t('evaluationLogicDesc'),
    },
    {
      icon: TrendingUp,
      title: t('skillDevelopment'),
      description: t('skillDevelopmentDesc'),
    },
  ];

  return (
      <section className="py-20 lg:py-28">
        <div className="mx-auto max-w-[1440px] px-6 lg:px-16">
          <div className="rounded-xl border border-border bg-gradient-to-br from-primary/5 to-primary/10 p-8 lg:p-12">
            <div className="grid gap-12 lg:grid-cols-2 lg:gap-16">
              <div>
                <h2 className="mb-6 text-3xl font-bold tracking-tight text-foreground lg:text-4xl">
                  {t('title')}
                </h2>
                <p className="mb-6 text-lg leading-relaxed text-muted-foreground">
                  {t('description')}
                </p>
                <p className="text-base leading-relaxed text-muted-foreground">
                  {t('subDescription')}
                </p>
              </div>

              <div className="flex flex-col justify-center space-y-6">
                {useCases.map((useCase) => {
                  const IconComponent = useCase.icon;
                  return (
                      <div key={useCase.title} className="flex items-start gap-4">
                        <div className="flex size-12 shrink-0 items-center justify-center rounded-lg bg-white shadow-sm">
                          <IconComponent className="size-6 text-primary" strokeWidth={2} />
                        </div>
                        <div>
                          <h3 className="mb-1 font-semibold text-foreground">{useCase.title}</h3>
                          <p className="text-sm text-muted-foreground">{useCase.description}</p>
                        </div>
                      </div>
                  );
                })}
              </div>
            </div>
          </div>
        </div>
      </section>
  );
};