import { useState, useCallback } from 'react';

export const useFilterState = () => {
    const [showFilters, setShowFilters] = useState(true);

    const toggleFilters = useCallback(() => {
        setShowFilters((prev) => !prev);
    }, []);

    const openFilters = useCallback(() => {
        setShowFilters(true);
    }, []);

    const closeFilters = useCallback(() => {
        setShowFilters(false);
    }, []);

    return {
        showFilters,
        toggleFilters,
        openFilters,
        closeFilters,
    };
};