import { useTranslations } from 'next-intl';
import { toast } from 'sonner';
import { useDialog } from '@/hooks/use-dialog';
import type {
    QueryDefinition,
    MutationDefinition,
    BaseQueryFn
} from '@reduxjs/toolkit/query';

// Define the hook types based on RTK Query's internal structure
type UseQuery<_D extends QueryDefinition<any, any, any, any>> = (
    arg: any,
    options?: any
) => any;

type UseMutation<_D extends MutationDefinition<any, any, any, any>> = (
    options?: any
) => readonly [any, any];

interface UseMasterDataManagerProps<TData, TCreateRequest> {
    useGetQuery: UseQuery<QueryDefinition<void, BaseQueryFn, string, TData[], string>>;
    useCreateMutation: UseMutation<MutationDefinition<TCreateRequest, BaseQueryFn, string, TData, string>>;
    translationKey: 'locations' | 'positions' | 'skillCategories' | 'skills';
}

export const useMasterDataManager = <TData, TCreateRequest>({
                                                                useGetQuery,
                                                                useCreateMutation,
                                                                translationKey,
                                                            }: UseMasterDataManagerProps<TData, TCreateRequest>) => {
    const t = useTranslations(`admin.masterData.${translationKey}`);

    // 🔥 replaced local dialog state with shared hook
    const { isOpen: isDialogOpen, open: openDialog, close: closeDialog } = useDialog();

    const { data, isLoading, isError } = useGetQuery(undefined);
    const [createMutation, { isLoading: isCreating }] = useCreateMutation();

    const handleCreate = async (request: TCreateRequest, name: string) => {
        try {
            await createMutation(request).unwrap();
            toast.success(t('createSuccess', { name }));
            closeDialog();
            return true;
        } catch {
            toast.error(t('createError'));
            return false;
        }
    };

    return {
        items: data || [],
        isLoading,
        isError,

        // dialog
        isDialogOpen,
        openDialog,
        closeDialog,

        // create
        isCreating,
        handleCreate,
    };
};
