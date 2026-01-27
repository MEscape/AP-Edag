import { useTranslations } from 'next-intl';
import Image from 'next/image';
import type { Session } from 'next-auth';
import { NavigationActions } from './navigation-actions';

type NavigationProps = {
  session: Session | null;
};

export const Navigation = ({ session }: NavigationProps) => {
  const t = useTranslations('welcome.navigation');

  return (
      <nav className="sticky top-0 z-50 w-full border-b border-border bg-white/80 backdrop-blur-md">
        <div className="mx-auto flex h-20 max-w-[1440px] items-center justify-between px-6 lg:px-16">
          <div className="flex items-center">
            <Image
                src="/logo.png"
                alt={t('companyName')}
                width={120}
                height={40}
                priority
                className="h-auto w-auto"
            />
          </div>

          <NavigationActions
              isAuthenticated={!!session}
              toDashboardLabel={t('toDashboard')}
              signInLabel={t('signIn')}
          />
        </div>
      </nav>
  );
};