'use client';

import React, { useState, useEffect } from 'react';
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
import { ProjectDialogForm } from './project-dialog-form';
import type {
    Project,
    CreateProjectRequest,
    UpdateProjectRequest,
    TechnologyFilterOption,
} from '@/store/api/project-api';
import type { Position } from '@/store/api/option-api';
import type { ProjectMember } from '../../hooks/use-project-member-selection';
import {Loader2Icon} from "lucide-react";

interface ProjectDialogProps {
    isOpen: boolean;
    mode: 'create' | 'edit';
    currentProject: Project | null;
    skillOptions: TechnologyFilterOption[];
    positionOptions: Position[];
    selectedMembers: ProjectMember[];
    isSaving: boolean;
    onClose: () => void;
    onSave: (data: CreateProjectRequest | UpdateProjectRequest) => Promise<boolean>;
    onSelectMembers: (onBeforeNavigate?: () => void) => void;
    onRemoveMember: (employeeId: string) => void;
    onUpdateMemberPosition: (employeeId: string, positionId: string) => void;
}

const INITIAL_FORM_DATA = {
    name: '',
    description: '',
    status: 'PLANNED' as Project['status'],
    startDate: new Date().toISOString().split('T')[0]!,
    endDate: '',
    client: '',
    technologies: [] as string[],
};

export const ProjectDialog = ({
                                  isOpen,
                                  mode,
                                  currentProject,
                                  skillOptions,
                                  positionOptions,
                                  selectedMembers,
                                  isSaving,
                                  onClose,
                                  onSave,
                                  onSelectMembers,
                                  onRemoveMember,
                                  onUpdateMemberPosition,
                              }: ProjectDialogProps) => {
    const t = useTranslations('discoverProjects.dialog');
    const [formData, setFormData] = useState(INITIAL_FORM_DATA);

    // Initialize form
    useEffect(() => {
        if (!isOpen) return;

        const savedFormData = sessionStorage.getItem('projectFormData');
        if (savedFormData) {
            try {
                setFormData(JSON.parse(savedFormData));
                sessionStorage.removeItem('projectFormData');
                return;
            } catch (error) {
                console.error('Failed to restore form data:', error);
            }
        }

        if (mode === 'edit' && currentProject) {
            setFormData({
                name: currentProject.name,
                description: currentProject.description || '',
                status: currentProject.status,
                startDate: currentProject.startDate,
                endDate: currentProject.endDate || '',
                client: currentProject.client || '',
                technologies: currentProject.technologies,
            });
        } else {
            setFormData(INITIAL_FORM_DATA);
        }
    }, [isOpen, mode, currentProject]);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        if (selectedMembers.length === 0) {
            alert(t('validation.membersRequired'));
            return;
        }

        if (formData.technologies.length === 0) {
            alert(t('validation.technologiesRequired'));
            return;
        }

        const membersWithoutPosition = selectedMembers.filter((m) => !m.positionId);
        if (membersWithoutPosition.length > 0) {
            alert(t('validation.positionsRequired'));
            return;
        }

        const members = selectedMembers.map((m) => ({
            employeeId: m.employeeId,
            positionId: m.positionId,
        }));

        const technologyIds = formData.technologies
            .map((techName) => skillOptions.find((s) => s.name === techName)?.id)
            .filter((id): id is string => !!id);

        let requestData;
        if (mode === 'edit') {
            requestData = {
                name: formData.name,
                description: formData.description || undefined,
                status: formData.status,
                startDate: formData.startDate,
                endDate: formData.endDate || null,
                client: formData.client || undefined,
                technologyIds,
                members,
            } as UpdateProjectRequest;
        } else {
            requestData = {
                name: formData.name,
                description: formData.description || undefined,
                status: formData.status,
                startDate: formData.startDate,
                endDate: formData.endDate || null,
                client: formData.client || undefined,
                technologies: technologyIds,
                members,
            } as CreateProjectRequest;
        }

        const success = await onSave(requestData);
        if (success) {
            setFormData(INITIAL_FORM_DATA);
        }
    };

    const handleSelectMembers = () => {
        sessionStorage.setItem('projectFormData', JSON.stringify(formData));
        onSelectMembers();
    };

    return (
        <Dialog open={isOpen} onOpenChange={onClose}>
            <DialogContent className="max-h-[90vh] max-w-2xl overflow-y-auto">
                <form onSubmit={handleSubmit}>
                    <DialogHeader>
                        <DialogTitle>
                            {mode === 'create' ? t('addTitle') : t('editTitle')}
                        </DialogTitle>
                        <DialogDescription>
                            {mode === 'create' ? t('addDescription') : t('editDescription')}
                        </DialogDescription>
                    </DialogHeader>

                    <div className="py-6">
                        <ProjectDialogForm
                            formData={formData}
                            onFormChange={setFormData}
                            skillOptions={skillOptions}
                            positionOptions={positionOptions}
                            selectedMembers={selectedMembers}
                            onSelectMembers={handleSelectMembers}
                            onRemoveMember={onRemoveMember}
                            onUpdateMemberPosition={onUpdateMemberPosition}
                        />
                    </div>

                    <DialogFooter>
                        <Button type="button" variant="outline" onClick={onClose} disabled={isSaving}>
                            {t('cancel')}
                        </Button>
                        <Button type="submit" disabled={isSaving}>
                            {isSaving ? (
                                <>
                                    <Loader2Icon
                                        aria-hidden="true"
                                        className="mr-2 size-3 animate-spin"
                                    />
                                    {t('saving')}
                                </>
                            ) : mode === 'create' ? (
                                t('create')
                            ) : (
                                t('save')
                            )}
                        </Button>
                    </DialogFooter>
                </form>
            </DialogContent>
        </Dialog>
    );
};