import { Metadata } from 'next';
import { getTranslations } from 'next-intl/server';
import AdminPage from '@/screens/admin/admin-page';

interface AdminRouteProps {
    params: Promise<{
        locale: string;
    }>;
}

export async function generateMetadata({ params }: AdminRouteProps): Promise<Metadata> {
    const { locale } = await params;
    const t = await getTranslations({ locale, namespace: 'admin.meta' });

    return {
        title: t('title'),
        description: t('description'),
    };
}

export default function AdminRoute() {
    return <AdminPage />;
}