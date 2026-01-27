import React from 'react';
import { Loader2Icon } from 'lucide-react';
import { cn } from '@/utils/helpers';
import { useTranslations } from 'next-intl';

const Spinner = ({ className, ...props }: Readonly<React.ComponentProps<'svg'>>) => {
  const t = useTranslations('welcome');

  return (
      <output aria-live="polite" className="flex flex-col items-center gap-4">
        <Loader2Icon
            aria-hidden="true"
            className={cn('size-4 animate-spin', className)}
            {...props}
        />
        <p className="text-sm text-muted-foreground">{t('loading.text')}</p>
      </output>
  );
};

export { Spinner };
