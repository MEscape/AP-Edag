import { useGetFilterOptionsQuery } from '@/store/api/project-api';
import { useGetPositionsQuery } from '@/store/api/option-api';

export const useProjectFilters = () => {
    const { data: filterOptions, isLoading: isLoadingFilters } =
        useGetFilterOptionsQuery();
    const { data: positionData, isLoading: isLoadingPositions } =
        useGetPositionsQuery();

    const skillOptions = filterOptions?.skills ?? [];
    const positionOptions = positionData?.positions ?? [];

    return {
        skillOptions,
        positionOptions,
        isLoading: isLoadingFilters || isLoadingPositions,
    };
};