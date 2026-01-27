import { Metadata } from 'next';
import { getTranslations } from 'next-intl/server';
import { getServerSession } from 'next-auth';
import ProfilePage from "@/screens/profile/profile-page";

interface MyProfileRouteProps {
  params: Promise<{
    locale: string;
  }>;
}

export async function generateMetadata({ params }: MyProfileRouteProps): Promise<Metadata> {
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

export default function MyProfilePage() {
    return <ProfilePage userId="me" />;
}
