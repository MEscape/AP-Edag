import { Metadata } from 'next';
import { getTranslations } from 'next-intl/server';
import ProfilePage from '@/screens/profile/profile-page';
import {getServerSession} from "next-auth";

interface ProfilePageProps {
    params: Promise<{
        locale: string;
        userId: string;
    }>;
}

export async function generateMetadata({ params }: ProfilePageProps): Promise<Metadata> {
    const { locale } = await params;

    // Load user session
    const session = await getServerSession();
    const name = session?.user?.name ?? 'User';

    // Load translations
    const t = await getTranslations({ locale, namespace: 'profile.meta' });

    return {
        title: t('title', { name }),
        description: t('description', { name }),
    };
}

export default async function UserProfilePage({ params }: ProfilePageProps) {
    const { userId } = await params;
    return <ProfilePage userId={userId} />;
}
