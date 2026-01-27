'use client';

import { useTranslations } from 'next-intl';
import { AlertTriangle, RefreshCw, LucideIcon, ArrowLeft } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Card, CardContent } from '@/components/ui/card';
import { ReactNode } from 'react';

interface ErrorStateProps {
  title?: string;
  description?: string;
  icon?: LucideIcon;
  variant?: 'card' | 'simple';
  onRetry?: () => void;
  isRetrying?: boolean;
  retryLabel?: string;
  goBack?: () => void;
  goBackLabel?: string;
  actions?: ReactNode;
}

export const ErrorState = ({
                             title,
                             description,
                             icon: Icon = AlertTriangle,
                             variant = 'card',
                             onRetry,
                             isRetrying = false,
                             retryLabel,
                             goBack,
                             goBackLabel,
                             actions,
                           }: ErrorStateProps) => {
  const t = useTranslations('common.error');

  const titleText = title ?? t('title');
  const descriptionText = description ?? t('description');
  const retryText = retryLabel ?? t('retry');
  const goBackText = goBackLabel ?? t('goBack');

  const content = (
      <>
        <div
            className={`flex items-center justify-center rounded-lg bg-destructive/10 ${
                variant === 'card' ? 'size-16' : 'size-12'
            }`}
        >
          <Icon
              className={`text-destructive ${
                  variant === 'card' ? 'size-8' : 'size-6'
              }`}
              strokeWidth={2}
          />
        </div>

        <div className="space-y-2">
          <h2
              className={`font-semibold tracking-tight text-foreground ${
                  variant === 'card' ? 'text-xl' : 'text-base'
              }`}
          >
            {titleText}
          </h2>
          <p className="text-sm leading-relaxed text-muted-foreground">
            {descriptionText}
          </p>
        </div>

        {(onRetry || goBack || actions) && (
            <div className="flex flex-wrap justify-center gap-2">
              {onRetry && (
                  <Button
                      variant="outline"
                      size={variant === 'card' ? 'lg' : 'default'}
                      onClick={onRetry}
                      disabled={isRetrying}
                      className={`gap-2 border-border bg-white font-semibold transition-all hover:border-primary/20 hover:shadow-md ${
                          variant === 'card' ? 'h-12 px-6' : ''
                      }`}
                  >
                    <RefreshCw
                        className={`size-5 ${isRetrying ? 'animate-spin' : ''}`}
                    />
                    {retryText}
                  </Button>
              )}

              {goBack && (
                  <Button
                      variant="outline"
                      size={variant === 'card' ? 'lg' : 'default'}
                      onClick={goBack}
                      className={`gap-2 border-border bg-white font-semibold transition-all hover:border-primary/20 hover:shadow-md ${
                          variant === 'card' ? 'h-12 px-6' : ''
                      }`}
                  >
                    <ArrowLeft className="size-5" />
                    {goBackText}
                  </Button>
              )}

              {actions}
            </div>
        )}
      </>
  );

  // Card variant
  if (variant === 'card') {
    return (
        <div className="mx-auto max-w-5xl p-4 w-full">
            <Card className="border-destructive/20 bg-white shadow-edag">
                <CardContent className="flex flex-col items-center justify-center gap-6 p-12 text-center">
                    {content}
                </CardContent>
            </Card>
        </div>
    );
  }

  // Simple variant
  return (
      <div className="flex flex-col items-center justify-center gap-4 py-12 text-center">
          {content}
      </div>
  );
};
