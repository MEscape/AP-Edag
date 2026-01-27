'use client';

import React, { useState, useEffect, useCallback } from 'react';
import { useTranslations } from 'next-intl';
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle,
} from '@/components/ui/dialog';
import { Button } from '@/components/ui/button';
import { Label } from '@/components/ui/label';
import { Textarea } from '@/components/ui/textarea';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import { AutocompleteInput } from '@/components/ui/autocomplete-input';
import { Input } from '@/components/ui/input';
import type {UpdateProfileRequest, UserProfile} from '@/store/api/profile-api';
import type { Position, Location } from '@/store/api/option-api';

interface EditProfileDialogProps {
    open: boolean;
    onOpenChange: (open: boolean) => void;
    currentData: {
        email?: string;
        firstName?: string;
        lastName?: string;
        positionId?: string;
        position?: string;
        locationId?: string;
        location?: string;
        availability?: UserProfile["availability"];
        yearsOfExperience?: number;
        bio?: string;
    };
    onSave: (data: UpdateProfileRequest) => Promise<boolean>;
    positionSuggestions: Position[];
    locationSuggestions: Location[];
}

const MAX_BIO_LENGTH = 500;

const AVAILABILITY_OPTIONS: UserProfile["availability"][] = [
    'available',
    'partially_available',
    'unavailable',
];

export const EditProfileDialog = ({
                                      open,
                                      onOpenChange,
                                      currentData,
                                      onSave,
                                      positionSuggestions,
                                      locationSuggestions,
                                  }: EditProfileDialogProps) => {
    const t = useTranslations('profile');
    const [formData, setFormData] = useState<UpdateProfileRequest>({
        positionId: currentData.positionId,
        locationId: currentData.locationId,
        availability: currentData.availability,
        yearsOfExperience: currentData.yearsOfExperience,
        bio: currentData.bio,
    });
    const [selectedPositionName, setSelectedPositionName] = useState(currentData.position || '');
    const [selectedLocationName, setSelectedLocationName] = useState(currentData.location || '');
    const [isSaving, setIsSaving] = useState(false);

    // Reset form when dialog opens
    useEffect(() => {
        if (open) {
            setFormData({
                positionId: currentData.positionId,
                locationId: currentData.locationId,
                availability: currentData.availability,
                yearsOfExperience: currentData.yearsOfExperience,
                bio: currentData.bio,
            });
            setSelectedPositionName(currentData.position || '');
            setSelectedLocationName(currentData.location || '');
        }
    }, [open, currentData]);

    // Check if form has changes
    const hasChanges =
        formData.positionId !== currentData.positionId ||
        formData.locationId !== currentData.locationId ||
        formData.availability !== currentData.availability ||
        formData.yearsOfExperience !== currentData.yearsOfExperience ||
        formData.bio !== currentData.bio;

    const updateField = useCallback(
        <K extends keyof UpdateProfileRequest>(field: K, value: UpdateProfileRequest[K]) => {
            setFormData((prev) => ({ ...prev, [field]: value }));
        },
        []
    );

    const handlePositionChange = useCallback(
        (value: string) => {
            setSelectedPositionName(value);
            const matchedPosition = positionSuggestions.find((p) => p.name === value);
            if (matchedPosition) {
                updateField('positionId', matchedPosition.id);
            } else {
                updateField('positionId', undefined);
            }
        },
        [positionSuggestions, updateField]
    );

    const handleLocationChange = useCallback(
        (value: string) => {
            setSelectedLocationName(value);
            const matchedLocation = locationSuggestions.find((l) => l.name === value);
            if (matchedLocation) {
                updateField('locationId', matchedLocation.id);
            } else {
                updateField('locationId', undefined);
            }
        },
        [locationSuggestions, updateField]
    );

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        if (!hasChanges) {
            onOpenChange(false);
            return;
        }

        setIsSaving(true);
        try {
            const success = await onSave(formData);
            if (success) {
                onOpenChange(false);
            }
        } finally {
            setIsSaving(false);
        }
    };

    const handleCancel = useCallback(() => {
        onOpenChange(false);
    }, [onOpenChange]);

    const bioLength = formData.bio?.length || 0;
    const bioRemaining = MAX_BIO_LENGTH - bioLength;
    const isBioWarning = bioRemaining < 50;

    return (
        <Dialog open={open} onOpenChange={onOpenChange}>
            <DialogContent className="max-w-2xl max-h-[90vh] overflow-y-auto">
                <form onSubmit={handleSubmit}>
                    <DialogHeader>
                        <DialogTitle>{t('editDialog.title')}</DialogTitle>
                        <DialogDescription>{t('editDialog.description')}</DialogDescription>
                    </DialogHeader>

                    <div className="grid gap-4 py-4">
                        <div className="grid gap-4 sm:grid-cols-2">
                            <AutocompleteInput
                                id="position"
                                label={t('editDialog.position')}
                                value={selectedPositionName}
                                suggestions={positionSuggestions.map((p) => p.name)}
                                onChange={handlePositionChange}
                                placeholder={t('editDialog.positionPlaceholder')}
                                maxLength={100}
                                noSuggestionsText={t('editDialog.noSuggestionsFound')}
                            />
                            <AutocompleteInput
                                id="location"
                                label={t('editDialog.location')}
                                value={selectedLocationName}
                                suggestions={locationSuggestions.map((l) => l.name)}
                                onChange={handleLocationChange}
                                placeholder={t('editDialog.locationPlaceholder')}
                                maxLength={100}
                                noSuggestionsText={t('editDialog.noSuggestionsFound')}
                            />
                        </div>

                        <div className="grid gap-4 sm:grid-cols-2">
                            <div className="space-y-2">
                                <Label htmlFor="yearsOfExperience">{t('editDialog.yearsOfExperience')}</Label>
                                <Input
                                    id="yearsOfExperience"
                                    type="number"
                                    min="0"
                                    max="99"
                                    step="0.5"
                                    value={formData.yearsOfExperience || 0}
                                    onChange={(e) => {
                                        updateField('yearsOfExperience', Number.parseFloat(e.target.value) || 0);
                                    }}
                                />
                            </div>
                            <div className="space-y-2">
                                <Label htmlFor="availability">{t('editDialog.availability')}</Label>
                                <Select
                                    value={formData.availability || ''}
                                    onValueChange={(value) => updateField('availability', value as UserProfile["availability"])}
                                >
                                    <SelectTrigger id="availability" className="w-full">
                                        <SelectValue placeholder={t('editDialog.selectAvailability')} />
                                    </SelectTrigger>
                                    <SelectContent>
                                        {AVAILABILITY_OPTIONS.map((status) => (
                                            <SelectItem key={status} value={status}>
                                                {t(`availability.${status}`)}
                                            </SelectItem>
                                        ))}
                                    </SelectContent>
                                </Select>
                            </div>
                        </div>

                        <div className="space-y-2">
                            <Label htmlFor="bio">{t('editDialog.bio')}</Label>
                            <Textarea
                                id="bio"
                                value={formData.bio || ''}
                                onChange={(e) => updateField('bio', e.target.value)}
                                placeholder={t('editDialog.bioPlaceholder')}
                                rows={4}
                                maxLength={MAX_BIO_LENGTH}
                                className="resize-none"
                            />
                            <p className={`text-xs ${isBioWarning ? 'text-warning' : 'text-muted-foreground'}`}>
                                {bioLength}/{MAX_BIO_LENGTH} {t('editDialog.characters')}
                                {isBioWarning && bioRemaining > 0 && (
                                    <span className="ml-2">
                    ({bioRemaining} {t('editDialog.remaining')})
                  </span>
                                )}
                            </p>
                        </div>
                    </div>

                    <DialogFooter className="gap-2">
                        <Button type="button" variant="outline" onClick={handleCancel} disabled={isSaving}>
                            {t('editDialog.cancel')}
                        </Button>
                        <Button type="submit" disabled={isSaving || !hasChanges}>
                            {isSaving ? t('editDialog.saving') : t('editDialog.save')}
                        </Button>
                    </DialogFooter>
                </form>
            </DialogContent>
        </Dialog>
    );
};