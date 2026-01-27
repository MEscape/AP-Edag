'use client';

import { useTranslations } from 'next-intl';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import { RefreshCw, Briefcase, Edit, CheckCircle2 } from 'lucide-react';

import type { UserProfile } from '@/store/api/profile-api';
import { AVAILABILITY_STYLES } from '../../utils/profile-variants';

import { OnboardingBanner } from './onboarding-banner';
import { UserAvatar } from './user-avatar';
import { ProfileCompletion } from './profile-completion';
import { ContactInfo } from './contact-info';

interface ProfileHeaderProps {
    profile?: UserProfile;
    isOwnProfile: boolean;
    isOnboarding: boolean;
    isRefreshing: boolean;
    onRefresh: () => void;
    onEdit: () => void;
}

export const ProfileHeader = ({
                                  profile,
                                  isOwnProfile,
                                  isOnboarding,
                                  isRefreshing,
                                  onRefresh,
                                  onEdit,
                              }: ProfileHeaderProps) => {
    const t = useTranslations('profile');

    if (!profile) return null;

    const availabilityColor = AVAILABILITY_STYLES[profile.availability];
    const isProfileComplete = profile.completionRate === 100;

    return (
        <div className="space-y-6">
            {isOnboarding && isOwnProfile && <OnboardingBanner />}

            <div className="rounded-lg border border-border bg-white p-6 shadow-edag lg:p-8">
                <div className="flex flex-col gap-6 lg:flex-row lg:items-start lg:justify-between">

                    <div className="flex flex-col items-center gap-6 sm:flex-row sm:items-start">
                        <UserAvatar firstName={profile.firstName} lastName={profile.lastName} />

                        <div className="flex-1 space-y-4 text-center sm:text-left">
                            <div className="space-y-2">
                                <div className="flex flex-col items-center gap-2 sm:flex-row sm:items-start">
                                    <h1 className="text-2xl font-bold tracking-tight text-foreground lg:text-3xl">
                                        {profile.firstName} {profile.lastName}
                                    </h1>

                                    {isOwnProfile && isProfileComplete && (
                                        <div className="rounded-full bg-success/10 p-1">
                                            <CheckCircle2 className="size-5 text-success" />
                                        </div>
                                    )}
                                </div>

                                <div className="flex flex-wrap items-center justify-center gap-2 sm:justify-start">
                                    <Badge variant="secondary" className="gap-1.5 px-3 py-1">
                                        <Briefcase className="size-3.5" />
                                        {profile.position || t('noPosition')}
                                    </Badge>

                                    <Badge className={availabilityColor}>
                                        {t(`availability.${profile.availability}`)}
                                    </Badge>
                                </div>
                            </div>

                            <ContactInfo
                                email={profile.email}
                                location={profile.location}
                                joinDate={profile.joinDate}
                            />
                        </div>
                    </div>

                    <div className="flex flex-col items-center gap-4 lg:items-end">
                        {isOwnProfile && (
                            <div className="flex gap-2">
                                <Button
                                    variant="outline"
                                    size="sm"
                                    onClick={onRefresh}
                                    disabled={isRefreshing}
                                    className="gap-2 font-semibold"
                                >
                                    <RefreshCw className={`size-4 ${isRefreshing ? 'animate-spin' : ''}`} />
                                    {t('refresh')}
                                </Button>

                                <Button size="sm" onClick={onEdit} className="gap-2 font-semibold">
                                    <Edit className="size-4" />
                                    {t('editProfile')}
                                </Button>
                            </div>
                        )}

                        {isOwnProfile && (
                            <ProfileCompletion completionRate={profile.completionRate} />
                        )}
                    </div>
                </div>

                {profile.bio && (
                    <div className="mt-6 border-t border-border pt-6">
                        <p className="text-sm leading-relaxed text-muted-foreground">{profile.bio}</p>
                    </div>
                )}
            </div>
        </div>
    );
};
