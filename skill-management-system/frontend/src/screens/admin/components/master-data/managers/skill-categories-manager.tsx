'use client';

import { useState } from 'react';
import { useTranslations } from 'next-intl';
import { Button } from '@/components/ui/button';
import {FolderOpen, Plus} from 'lucide-react';
import { useMasterDataManager } from '../../../hooks/use-master-data-manager';
import { MasterDataList } from '../master-data-list';
import { MasterDataDialog } from '../master-data-dialog';
import { LoadingSkeletons } from '../../ui/loading-skeletons';
import { ErrorState } from '@/components/ui/error-state';
import { toast } from 'sonner';
import {useCreateSkillCategoryMutation, useGetSkillCategoriesQuery} from "@/store/api/option-api";

export const SkillCategoriesManager = () => {
    const t = useTranslations('admin.masterData.skillCategories');
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
        useGetQuery: useGetSkillCategoriesQuery,
        useCreateMutation: useCreateSkillCategoryMutation,
        translationKey: 'skillCategories',
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
                    items={items.categories}
                    emptyMessage={t('empty')}
                    emptyIcon={FolderOpen}
                />
            </div>

            <MasterDataDialog
                isOpen={isDialogOpen}
                translationKey="skillCategories"
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
