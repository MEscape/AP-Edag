'use client';

import { useTranslations } from 'next-intl';
import { useState } from 'react';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import { Label } from '@/components/ui/label';
import { Award, Plus } from 'lucide-react';
import {
    useGetAllSkillsQuery,
    useCreateSkillMutation,
    useGetSkillCategoriesQuery,
    Skill,
} from '@/store/api/option-api';
import { MasterDataDialog } from '../master-data-dialog';
import { MasterDataList } from '../master-data-list';
import { LoadingSkeletons } from '../../ui/loading-skeletons';
import { ErrorState } from '@/components/ui/error-state';
import { toast } from 'sonner';
import { useMasterDataManager } from '../../../hooks/use-master-data-manager';
import {
    Select,
    SelectContent,
    SelectItem,
    SelectTrigger,
    SelectValue,
} from '@/components/ui/select';

export const SkillsManager = () => {
    const t = useTranslations('admin.masterData.skills');
    const [name, setName] = useState('');
    const [categoryId, setCategoryId] = useState('');

    const { data: categories, isLoading: isLoadingCategories } = useGetSkillCategoriesQuery(undefined);

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
        useGetQuery: useGetAllSkillsQuery,
        useCreateMutation: useCreateSkillMutation,
        translationKey: 'skills',
    });

    const onCreate = async () => {
        if (!name.trim()) {
            toast.error(t('nameRequired'));
            return;
        }
        if (!categoryId) {
            toast.error(t('categoryRequired'));
            return;
        }

        const success = await handleCreate(
            { name: name.trim(), categoryId },
            name.trim()
        );

        if (success) {
            setName('');
            setCategoryId('');
        }
    };

    const handleClose = () => {
        closeDialog();
        setName('');
        setCategoryId('');
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

                <MasterDataList<Skill>
                    items={items.skills}
                    emptyMessage={t('empty')}
                    emptyIcon={Award}
                    renderExtra={(skill) => (
                        <Badge variant="secondary">{skill.categoryName}</Badge>
                    )}
                />
            </div>

            <MasterDataDialog
                isOpen={isDialogOpen}
                translationKey="skills"
                value={name}
                isCreating={isCreating}
                onClose={handleClose}
                onChange={setName}
                onCreate={onCreate}
                extraFields={
                    <div className="space-y-2">
                        <Label htmlFor="category">{t('categoryLabel')}</Label>
                        <Select
                            value={categoryId}
                            onValueChange={setCategoryId}
                            disabled={isLoadingCategories}
                        >
                            <SelectTrigger id="category">
                                <SelectValue placeholder={t('categoryPlaceholder')} />
                            </SelectTrigger>
                            <SelectContent className="max-h-60 overflow-y-auto">
                                {categories?.categories.map((category) => (
                                    <SelectItem key={category.id} value={category.id}>
                                        {category.name}
                                    </SelectItem>
                                ))}
                            </SelectContent>
                        </Select>
                    </div>
                }
            />
        </>
    );
};