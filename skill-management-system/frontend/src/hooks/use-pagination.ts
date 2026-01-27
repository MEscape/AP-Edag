import { useState } from 'react';

export const usePagination = () => {
    const [page, setPage] = useState(0);

    const handlePageChange = (newPage: number) => {
        setPage(newPage);
    };

    const handleNextPage = () => {
        setPage((prev) => prev + 1);
    };

    const handlePreviousPage = () => {
        setPage((prev) => Math.max(0, prev - 1));
    };

    const resetPage = () => {
        setPage(0);
    };

    return {
        page,
        setPage: handlePageChange,
        onNextPage: handleNextPage,
        onPreviousPage: handlePreviousPage,
        resetPage,
    };
};