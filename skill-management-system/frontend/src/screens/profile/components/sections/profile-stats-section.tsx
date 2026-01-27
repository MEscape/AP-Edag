'use client';

import { useProfile } from '../../hooks/use-profile';
import { ProfileStats } from '../profile/profile-stats';
import { ProfileStatsSkeleton } from '../ui/loading-skeletons';

interface ProfileStatsSectionProps {
    userId: string;
}

export const ProfileStatsSection = ({ userId }: ProfileStatsSectionProps) => {
    const { profile, isLoading } = useProfile(userId);

    if (isLoading) {
        return <ProfileStatsSkeleton />;
    }

    return <ProfileStats profile={profile} />;
};