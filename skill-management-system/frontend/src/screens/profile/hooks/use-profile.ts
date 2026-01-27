import { useGetProfileQuery } from '@/store/api/profile-api';

export const useProfile = (userId: string = 'me') => {
    const {
        data: profile,
        isLoading,
        isError,
        isFetching,
        refetch,
    } = useGetProfileQuery(userId);

    const isOwnProfile = userId === 'me';
    const isOnboarding = profile?.completionRate !== 100;

    return {
        profile,
        isOwnProfile,
        isOnboarding,
        isLoading,
        isError,
        isFetching,
        refetch,
    };
};