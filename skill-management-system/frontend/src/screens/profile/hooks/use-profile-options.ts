import { useMemo } from 'react';
import { useGetPositionsQuery, useGetLocationsQuery } from '@/store/api/option-api';

/**
 * Hook für Profile Options (Positions & Locations)
 */
export const useProfileOptions = () => {
    const { data: positionsData } = useGetPositionsQuery();
    const { data: locationsData } = useGetLocationsQuery();

    const positionSuggestions = useMemo(
        () => positionsData?.positions || [],
        [positionsData]
    );

    const locationSuggestions = useMemo(
        () => locationsData?.locations || [],
        [locationsData]
    );

    return {
        positionSuggestions,
        locationSuggestions,
    };
};