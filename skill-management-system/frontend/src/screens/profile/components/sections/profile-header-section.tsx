'use client';

import { useTranslations } from 'next-intl';
import { useProfile } from '../../hooks/use-profile';
import { useProfileDialogs } from '../../hooks/use-profile-dialogs';
import { useProfileOptions } from '../../hooks/use-profile-options';
import { ProfileHeader } from '../profile/profile-header';
import { EditProfileDialog } from '../profile/edit-profile-dialog';
import { ProfileHeaderSkeleton } from '../ui/loading-skeletons';
import { ErrorState } from '@/components/ui/error-state';

interface ProfileHeaderSectionProps {
    userId: string;
}

export const ProfileHeaderSection = ({ userId }: ProfileHeaderSectionProps) => {
    const t = useTranslations('profile');
    const { profile, isOwnProfile, isOnboarding, isLoading, isError, isFetching, refetch } =
        useProfile(userId);

    const { isEditDialogOpen, openEditDialog, closeEditDialog, handleUpdateProfile } =
        useProfileDialogs(userId);

    const { positionSuggestions, locationSuggestions } = useProfileOptions();

    if (isError) {
        return (
            <ErrorState
                title={t('error')}
                description={t('errorDescription')}
                onRetry={refetch}
                isRetrying={isFetching}
            />
        );
    }

    if (isLoading) {
        return <ProfileHeaderSkeleton isOwnProfile={isOwnProfile} />;
    }

    return (
        <>
            <ProfileHeader
                profile={profile}
                isOwnProfile={isOwnProfile}
                isOnboarding={isOnboarding}
                isRefreshing={isFetching}
                onRefresh={refetch}
                onEdit={openEditDialog}
            />

            {isOwnProfile && profile && (
                <EditProfileDialog
                    open={isEditDialogOpen}
                    onOpenChange={closeEditDialog}
                    currentData={{
                        email: profile.email,
                        firstName: profile.firstName,
                        lastName: profile.lastName,
                        positionId: profile.positionId,
                        position: profile.position,
                        locationId: profile.locationId,
                        location: profile.location,
                        availability: profile.availability,
                        yearsOfExperience: profile.yearsOfExperience,
                        bio: profile.bio,
                    }}
                    positionSuggestions={positionSuggestions}
                    locationSuggestions={locationSuggestions}
                    onSave={handleUpdateProfile}
                />
            )}
        </>
    );
};
