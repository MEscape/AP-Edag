import { useTranslations } from 'next-intl';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Textarea } from '@/components/ui/textarea';
import {
    Select,
    SelectContent,
    SelectItem,
    SelectTrigger,
    SelectValue,
} from '@/components/ui/select';
import { AutocompleteInput } from '@/components/ui/autocomplete-input';
import { ProjectMemberList } from './project-member-list';
import { Badge } from '@/components/ui/badge';
import { X } from 'lucide-react';
import { useState } from 'react';
import type { Project, TechnologyFilterOption } from '@/store/api/project-api';
import type { Position } from '@/store/api/option-api';
import type { ProjectMember } from '../../hooks/use-project-member-selection';

interface FormData {
    name: string;
    description: string;
    status: Project['status'];
    startDate: string;
    endDate: string;
    client: string;
    technologies: string[];
}

interface ProjectDialogFormProps {
    formData: FormData;
    onFormChange: (data: FormData) => void;
    skillOptions: TechnologyFilterOption[];
    positionOptions: Position[];
    selectedMembers: ProjectMember[];
    onSelectMembers: () => void;
    onRemoveMember: (employeeId: string) => void;
    onUpdateMemberPosition: (employeeId: string, positionId: string) => void;
}export const ProjectDialogForm = ({
                                       formData,
                                       onFormChange,
                                       skillOptions,
                                       positionOptions,
                                       selectedMembers,
                                       onSelectMembers,
                                       onRemoveMember,
                                       onUpdateMemberPosition,
                                   }: ProjectDialogFormProps) => {
    const t = useTranslations('discoverProjects.dialog');
    const [skillSearch, setSkillSearch] = useState('');const updateField = <K extends keyof FormData>(field: K, value: FormData[K]) => {
        onFormChange({ ...formData, [field]: value });
    };const handleSkillSelect = (skillName: string) => {
        const skill = skillOptions.find((s) => s.name === skillName);
        if (!skill || formData.technologies.includes(skill.name)) {
            setSkillSearch('');
            return;
        }
        updateField('technologies', [...formData.technologies, skill.name]);
        setSkillSearch('');
    };const handleSkillRemove = (skillName: string) => {
        updateField(
            'technologies',
            formData.technologies.filter((t) => t !== skillName)
        );
    };return (
        <div className="grid gap-6">

            <div className="space-y-2">
                <Label htmlFor="name" className="text-sm font-semibold">
                    {t('name')} <span className="text-destructive">*</span>
                </Label>
                <Input
                    id="name"
                    value={formData.name}
                    onChange={(e) => updateField('name', e.target.value)}
                    placeholder={t('namePlaceholder')}
                    maxLength={255}
                    required
                    className="h-12"
                />
            </div>

            <div className="space-y-2">
                <div className="flex items-center justify-between">
                    <Label htmlFor="description" className="text-sm font-semibold">
                        {t('description')}
                    </Label>
                    <span className="text-xs text-muted-foreground">
        {formData.description.length}/2000
      </span>
                </div>
                <Textarea
                    id="description"
                    value={formData.description}
                    onChange={(e) => updateField('description', e.target.value)}
                    placeholder={t('descriptionPlaceholder')}
                    maxLength={2000}
                    rows={4}
                    className="resize-none"
                />
            </div>
            <div className="space-y-2">
                <Label htmlFor="status" className="text-sm font-semibold">
                    {t('status')} <span className="text-destructive">*</span>
                </Label>
                <Select
                    value={formData.status}
                    onValueChange={(value: Project['status']) => updateField('status', value)}
                >
                    <SelectTrigger className="h-12">
                        <SelectValue />
                    </SelectTrigger>
                    <SelectContent>
                        <SelectItem value="PLANNED">{t('statusOptions.planned')}</SelectItem>
                        <SelectItem value="ACTIVE">{t('statusOptions.active')}</SelectItem>
                        <SelectItem value="COMPLETED">{t('statusOptions.completed')}</SelectItem>
                    </SelectContent>
                </Select>
            </div>
            <div className="grid gap-4 sm:grid-cols-2">
                <div className="space-y-2">
                    <Label htmlFor="startDate" className="text-sm font-semibold">
                        {t('startDate')} <span className="text-destructive">*</span>
                    </Label>
                    <Input
                        id="startDate"
                        type="date"
                        value={formData.startDate}
                        onChange={(e) => updateField('startDate', e.target.value)}
                        required
                        className="h-12"
                    />
                </div>
                <div className="space-y-2">
                    <Label htmlFor="endDate" className="text-sm font-semibold">
                        {t('endDate')}
                    </Label>
                    <Input
                        id="endDate"
                        type="date"
                        value={formData.endDate}
                        onChange={(e) => updateField('endDate', e.target.value)}
                        min={formData.startDate}
                        className="h-12"
                    />
                </div>
            </div>
            <div className="space-y-2">
                <Label htmlFor="client" className="text-sm font-semibold">
                    {t('client')}
                </Label>
                <Input
                    id="client"
                    value={formData.client}
                    onChange={(e) => updateField('client', e.target.value)}
                    placeholder={t('clientPlaceholder')}
                    className="h-12"
                />
            </div>
            <div className="space-y-2">
                <Label className="text-sm font-semibold">
                    {t('technologies')} <span className="text-destructive">*</span>
                </Label>
                <AutocompleteInput
                    id="technologies"
                    label=""
                    value={skillSearch}
                    onChange={setSkillSearch}
                    onSelect={handleSkillSelect}
                    suggestions={skillOptions.map((s) => s.name)}
                    placeholder={t('technologiesPlaceholder')}
                    noSuggestionsText={t('noTechnologies')}
                />
                {formData.technologies.length > 0 && (
                    <div className="flex flex-wrap gap-2 pt-2">
                        {formData.technologies.map((tech) => (
                            <Badge
                                key={tech}
                                variant="outline"
                                className="gap-1 border-primary/20 bg-primary/5 pr-1"
                            >
                                {tech}
                                <button
                                    type="button"
                                    onClick={() => handleSkillRemove(tech)}
                                    className="ml-1 rounded-full p-0.5 hover:bg-primary/10"
                                >
                                    <X className="size-3" />
                                </button>
                            </Badge>
                        ))}
                    </div>
                )}
            </div>
            <ProjectMemberList
                members={selectedMembers}
                positionOptions={positionOptions}
                onSelectMembers={onSelectMembers}
                onRemoveMember={onRemoveMember}
                onUpdateMemberPosition={onUpdateMemberPosition}
            />
        </div>
    );
};