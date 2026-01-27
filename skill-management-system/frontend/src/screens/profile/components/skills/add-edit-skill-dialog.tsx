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
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import type { AddSkillRequest, ProfileSkill } from '@/store/api/profile-api';
import { AutocompleteInput } from '@/components/ui/autocomplete-input';
import { SkillLevelSlider } from '@/components/ui/skill-level-slider';
import { Skill, SkillCategory } from '@/store/api/option-api';

interface AddEditSkillDialogProps {
    open: boolean;
    onOpenChange: (open: boolean) => void;
    mode: 'add' | 'edit';
    currentSkill?: ProfileSkill;
    categories: SkillCategory[];
    suggestions: Skill[];
    selectedCategoryId: string;
    onSelectCategory: (categoryId: string) => void;
    onSave: (skill: AddSkillRequest, skillId?: string) => Promise<boolean>;
}

const INITIAL_FORM_DATA: AddSkillRequest = {
    categoryId: '',
    skillId: '',
    score: 50,
    yearsOfExperience: 0,
    lastUsed: new Date().toISOString().split('T')[0]!,
};

export const AddEditSkillDialog = ({
                                       open,
                                       onOpenChange,
                                       mode,
                                       currentSkill,
                                       categories,
                                       suggestions,
                                       selectedCategoryId,
                                       onSelectCategory,
                                       onSave,
                                   }: AddEditSkillDialogProps) => {
    const t = useTranslations('profile');
    const [formData, setFormData] = useState<AddSkillRequest>(INITIAL_FORM_DATA);
    const [isSaving, setIsSaving] = useState(false);
    const [filteredSuggestions, setFilteredSuggestions] = useState<Skill[]>([]);
    const [selectedSkillName, setSelectedSkillName] = useState<string>('');

    // Initialize form data
    useEffect(() => {
        if (!open) return;

        if (mode === 'edit' && currentSkill) {
            const category = categories.find((c) => c.name === currentSkill.category);
            setFormData({
                categoryId: category?.id || '',
                skillId: '',
                score: currentSkill.proficiencyScore,
                yearsOfExperience: currentSkill.yearsOfExperience,
                lastUsed: currentSkill.lastUsed,
            });
            setSelectedSkillName(currentSkill.skillName);
            if (category?.id) {
                onSelectCategory(category.id);
            }
        } else {
            const initialCategoryId = selectedCategoryId || categories[0]?.id || '';
            setFormData({
                ...INITIAL_FORM_DATA,
                categoryId: initialCategoryId,
            });
            setSelectedSkillName('');
            if (initialCategoryId && !selectedCategoryId) {
                onSelectCategory(initialCategoryId);
            }
        }
    }, [open, mode, currentSkill, categories, onSelectCategory, selectedCategoryId]);

    // Filter suggestions based on search
    useEffect(() => {
        if (mode !== 'add') {
            setFilteredSuggestions([]);
            return;
        }

        if (selectedSkillName.trim()) {
            const filtered = suggestions.filter((skill) =>
                skill.name.toLowerCase().includes(selectedSkillName.toLowerCase())
            );
            setFilteredSuggestions(filtered);
        } else {
            setFilteredSuggestions(suggestions);
        }
    }, [selectedSkillName, suggestions, mode]);

    const hasChanges =
        mode === 'edit' && currentSkill
            ? formData.score !== currentSkill.proficiencyScore ||
            formData.yearsOfExperience !== currentSkill.yearsOfExperience ||
            formData.lastUsed !== currentSkill.lastUsed
            : selectedSkillName.trim() !== '' &&
            formData.categoryId !== '' &&
            formData.skillId !== '' &&
            formData.lastUsed !== '';

    const updateField = useCallback(
        <K extends keyof AddSkillRequest>(field: K, value: AddSkillRequest[K]) => {
            setFormData((prev) => ({ ...prev, [field]: value }));
        },
        []
    );

    const handleCategoryChange = useCallback(
        (categoryId: string) => {
            updateField('categoryId', categoryId);
            updateField('skillId', '');
            setSelectedSkillName('');
            onSelectCategory(categoryId);
        },
        [updateField, onSelectCategory]
    );

    const handleSkillNameChange = useCallback(
        (value: string) => {
            setSelectedSkillName(value);
            const matchedSkill = suggestions.find((s) => s.name === value);
            if (matchedSkill) {
                updateField('skillId', matchedSkill.id);
                if (matchedSkill.categoryId !== formData.categoryId) {
                    updateField('categoryId', matchedSkill.categoryId);
                    onSelectCategory(matchedSkill.categoryId);
                }
            } else {
                updateField('skillId', '');
            }
        },
        [suggestions, formData.categoryId, updateField, onSelectCategory]
    );

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        if (!hasChanges) {
            onOpenChange(false);
            return;
        }

        setIsSaving(true);
        try {
            let success;
            if (mode === 'edit' && currentSkill) {
                success = await onSave(formData, currentSkill.id);
            } else {
                success = await onSave(formData);
            }

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

    return (
        <Dialog open={open} onOpenChange={onOpenChange}>
            <DialogContent className="max-w-lg max-h-[90vh] overflow-y-auto">
                <form onSubmit={handleSubmit}>
                    <DialogHeader>
                        <DialogTitle>
                            {mode === 'add' ? t('skillDialog.addTitle') : t('skillDialog.editTitle')}
                        </DialogTitle>
                        <DialogDescription>
                            {mode === 'add'
                                ? t('skillDialog.addDescription')
                                : t('skillDialog.editDescription')}
                        </DialogDescription>
                    </DialogHeader>

                    <div className="grid gap-4 py-4">
                        <div className="space-y-2">
                            <Label htmlFor="category">
                                {t('skillDialog.category')}
                                <span className="text-destructive ml-1">*</span>
                            </Label>
                            <Select
                                value={formData.categoryId}
                                onValueChange={handleCategoryChange}
                                disabled={mode === 'edit'}
                            >
                                <SelectTrigger id="category">
                                    <SelectValue placeholder={t('skillDialog.selectCategory')} />
                                </SelectTrigger>
                                <SelectContent>
                                    {categories.map((cat) => (
                                        <SelectItem key={cat.id} value={cat.id}>
                                            {cat.name}
                                        </SelectItem>
                                    ))}
                                </SelectContent>
                            </Select>
                        </div>

                        <AutocompleteInput
                            id="skillName"
                            label={t('skillDialog.skillName')}
                            value={selectedSkillName}
                            suggestions={filteredSuggestions.map((s) => s.name)}
                            onChange={handleSkillNameChange}
                            placeholder={t('skillDialog.skillNamePlaceholder')}
                            required
                            disabled={mode === 'edit'}
                            noSuggestionsText={t('skillDialog.noSuggestionsFound')}
                        />

                        <SkillLevelSlider
                            score={formData.score}
                            onScoreChange={(score) => updateField('score', score)}
                            label={t('skillDialog.skillLevel')}
                        />

                        <div className="space-y-2">
                            <Label htmlFor="experience">
                                {t('skillDialog.yearsOfExperience')}
                                <span className="text-destructive ml-1">*</span>
                            </Label>
                            <Input
                                id="experience"
                                type="number"
                                min="0"
                                max="50"
                                step="0.5"
                                value={formData.yearsOfExperience}
                                onChange={(e) =>
                                    updateField('yearsOfExperience', Number.parseFloat(e.target.value) || 0)
                                }
                                required
                            />
                        </div>

                        <div className="space-y-2">
                            <Label htmlFor="lastUsed">
                                {t('skillDialog.lastUsed')}
                                <span className="text-destructive ml-1">*</span>
                            </Label>
                            <Input
                                id="lastUsed"
                                type="date"
                                value={formData.lastUsed || ''}
                                onChange={(e) => updateField('lastUsed', e.target.value)}
                                max={new Date().toISOString().split('T')[0]}
                                required
                            />
                        </div>
                    </div>

                    <DialogFooter className="gap-2">
                        <Button type="button" variant="outline" onClick={handleCancel} disabled={isSaving}>
                            {t('skillDialog.cancel')}
                        </Button>
                        <Button type="submit" disabled={isSaving || !hasChanges}>
                            {isSaving ? t('skillDialog.saving') : t('skillDialog.save')}
                        </Button>
                    </DialogFooter>
                </form>
            </DialogContent>
        </Dialog>
    );
};