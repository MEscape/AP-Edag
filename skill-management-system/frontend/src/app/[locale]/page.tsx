import { Metadata } from 'next';
import { getTranslations } from 'next-intl/server';
import WelcomePage from '@/screens/welcome/welcome-page';

interface WelcomeRouteProps {
    params: Promise<{
        locale: string;
    }>;
}

export async function generateMetadata({ params }: WelcomeRouteProps): Promise<Metadata> {
    const { locale } = await params;

    const t = await getTranslations({ locale, namespace: 'welcome.meta' });

    return {
        title: t('title'),
        description: t('description'),
    };
}

export default function WelcomeRoute() {
    return <WelcomePage />;
}
