'use client';

import { useState } from 'react';
import { useTranslations } from 'next-intl';
import { Button } from '@/components/ui/button';
import { Plus, MapPin } from 'lucide-react';
import { useGetLocationsQuery, useCreateLocationMutation } from '@/store/api/option-api';
import { useMasterDataManager } from '../../../hooks/use-master-data-manager';
import { MasterDataList } from '../master-data-list';
import { MasterDataDialog } from '../master-data-dialog';
import { LoadingSkeletons } from '../../ui/loading-skeletons';
import { ErrorState } from '@/components/ui/error-state';
import { toast } from 'sonner';

export const LocationsManager = () => {
    const t = useTranslations('admin.masterData.locations');
    const [name, setName] = useState('');

    const {
        items,
        isLoading,
        isError,
        isDialogOpen,
        isCreating,
        openDialog,
        closeDialog,
        handleCreate,
    } = useMasterDataManager({
        useGetQuery: useGetLocationsQuery,
        useCreateMutation: useCreateLocationMutation,
        translationKey: 'locations',
    });

    const onCreate = async () => {
        if (!name.trim()) {
            toast.error(t('nameRequired'));
            return;
        }

        const success = await handleCreate({ name: name.trim() }, name.trim());
        if (success) {
            setName('');
        }
    };

    if (isLoading) return <LoadingSkeletons />;
    if (isError) return <ErrorState variant="simple" title={t('error')} description={t('errorDescription')} />;

    return (
        <>
            <div className="space-y-4">
                <div className="flex items-center justify-between">
                    <p className="text-sm text-muted-foreground">
                        {t('total', { count: items.length })}
                    </p>
                    <Button onClick={openDialog} size="sm" className="gap-2">
                        <Plus className="size-4" />
                        {t('create')}
                    </Button>
                </div>

                <MasterDataList
                    items={items.locations}
                    emptyMessage={t('empty')}
                    emptyIcon={MapPin}
                />
            </div>

            <MasterDataDialog
                isOpen={isDialogOpen}
                translationKey="locations"
                value={name}
                isCreating={isCreating}
                onClose={() => {
                    closeDialog();
                    setName('');
                }}
                onChange={setName}
                onCreate={onCreate}
            />
        </>
    );
};