import { Shield } from 'lucide-react';
import { useTranslations } from 'next-intl';

export const Footer = () => {
  const t = useTranslations('welcome.footer');

  return (
      <footer className="border-t border-border bg-white py-8">
        <div className="mx-auto max-w-[1440px] px-6 lg:px-16">
          <div className="flex flex-col items-center justify-between gap-4 sm:flex-row">
            <div className="flex items-center gap-3">
              <div className="flex size-9 items-center justify-center rounded bg-primary">
                <Shield className="size-5 text-white" strokeWidth={2} />
              </div>
              <div className="flex flex-col leading-tight">
                <span className="text-sm font-bold text-foreground">{t('companyName')}</span>
                <span className="text-xs text-muted-foreground">{t('systemName')}</span>
              </div>
            </div>

            <div className="text-sm text-muted-foreground">
              {t('copyright')}
            </div>
          </div>
        </div>
      </footer>
  );
};