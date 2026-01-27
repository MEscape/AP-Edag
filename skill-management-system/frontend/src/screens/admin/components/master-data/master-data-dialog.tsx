import { useTranslations } from 'next-intl';
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from '@/components/ui/dialog';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import {Loader2Icon, Plus} from 'lucide-react';
import React from "react";

interface MasterDataDialogProps {
    isOpen: boolean;
    translationKey: 'locations' | 'positions' | 'skillCategories' | 'skills';
    value: string;
    isCreating: boolean;
    onClose: () => void;
    onChange: (value: string) => void;
    onCreate: () => void;
    extraFields?: React.ReactNode;
}

export const MasterDataDialog = ({
                                     isOpen,
                                     translationKey,
                                     value,
                                     isCreating,
                                     onClose,
                                     onChange,
                                     onCreate,
                                     extraFields,
                                 }: MasterDataDialogProps) => {
    const t = useTranslations(`admin.masterData.${translationKey}`);

    return (
        <Dialog open={isOpen} onOpenChange={(open) => !open && onClose()}>
            <DialogContent>
                <DialogHeader>
                    <DialogTitle>{t('createTitle')}</DialogTitle>
                    <DialogDescription>{t('createDescription')}</DialogDescription>
                </DialogHeader>

                <div className="space-y-4 py-4">
                    <div className="space-y-2">
                        <Label htmlFor="name">{t('nameLabel')}</Label>
                        <Input
                            id="name"
                            placeholder={t('namePlaceholder')}
                            value={value}
                            onChange={(e) => onChange(e.target.value)}
                        />
                    </div>
                    {extraFields}
                </div>

                <DialogFooter>
                    <Button variant="outline" onClick={onClose} disabled={isCreating}>
                        {t('cancel')}
                    </Button>
                    <Button onClick={onCreate} disabled={isCreating || !value.trim()}>
                        {isCreating ? (
                            <Loader2Icon
                                aria-hidden="true"
                                className="mr-2 size-3 animate-spin"
                            />
                        ) : (
                            <Plus className="mr-2 size-4" />
                        )}
                        {t('create')}
                    </Button>
                </DialogFooter>
            </DialogContent>
        </Dialog>
    );
};