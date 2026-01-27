'use client';

import { useTranslations } from 'next-intl';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Users } from 'lucide-react';
import { useUsersTable } from '../../hooks/use-users-table';
import { Searchbar } from '@/components/ui/searchbar';
import { UsersTableContent } from '../users/users-table-content';
import { UsersTablePagination } from '../users/users-table-pagination';
import { ErrorState } from '@/components/ui/error-state';
import { UsersTableSkeleton } from '../ui/loading-skeletons';

export const UsersSection = () => {
    const t = useTranslations('admin.users');
    const {
        users,
        totalPages,
        totalElements,
        page,
        isLoading,
        isError,
        setPage,
        setSearch,
        onNextPage,
        onPreviousPage,
    } = useUsersTable();

    return (
        <Card className="border-border bg-white shadow-edag">
            <CardHeader>
                <CardTitle className="flex items-center gap-2 text-xl font-bold text-foreground">
                    <Users className="size-5 text-primary" />
                    {t('title')}
                </CardTitle>
                <CardDescription className="text-base text-muted-foreground">
                    {t('description')}
                </CardDescription>
            </CardHeader>
            <CardContent className="space-y-6">
                <Searchbar
                    onSearch={setSearch}
                    placeholder={t('searchPlaceholder')}
                    isLoading={isLoading}
                />

                {isLoading ? (
                    <UsersTableSkeleton />
                ) : isError ? (
                    <ErrorState
                        variant="simple"
                        title={t('error')}
                        description={t('errorDescription')}
                    />
                ) : (
                    <>
                        <UsersTableContent users={users} />
                        {totalPages > 1 && (
                            <UsersTablePagination
                                page={page}
                                totalPages={totalPages}
                                totalElements={totalElements}
                                onPageChange={setPage}
                                onNextPage={onNextPage}
                                onPreviousPage={onPreviousPage}
                                isLoading={isLoading}
                            />
                        )}
                    </>
                )}
            </CardContent>
        </Card>
    );
};