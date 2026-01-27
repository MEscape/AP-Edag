import { useTranslations } from 'next-intl';
import { Button } from '@/components/ui/button';
import { Label } from '@/components/ui/label';
import {
    Select,
    SelectContent,
    SelectItem,
    SelectTrigger,
    SelectValue,
} from '@/components/ui/select';
import { Users, X } from 'lucide-react';
import type { Position } from '@/store/api/option-api';
import type { ProjectMember } from '../../hooks/use-project-member-selection';

interface ProjectMemberListProps {
    members: ProjectMember[];
    positionOptions: Position[];
    onSelectMembers: () => void;
    onRemoveMember: (employeeId: string) => void;
    onUpdateMemberPosition: (employeeId: string, positionId: string) => void;
}

export const ProjectMemberList = ({
                                       members,
                                       positionOptions,
                                       onSelectMembers,
                                       onRemoveMember,
                                       onUpdateMemberPosition,
                                   }: ProjectMemberListProps) => {
    const t = useTranslations('discoverProjects.dialog');
    return (
        <div className="space-y-2">
            <Label className="text-sm font-semibold">
                {t('teamMembers')} <span className="text-destructive">*</span>
            </Label>
            <Button
                type="button"
                variant="outline"
                onClick={onSelectMembers}
                className="w-full justify-start gap-2 h-12 font-normal"
            >
                <Users className="size-4" />
                {members.length > 0
                    ? t('membersSelected', { count: members.length })
                    : t('selectMembers')}
            </Button>  {members.length > 0 && (
            <div className="space-y-2 rounded-lg border border-border bg-muted/20 p-3">
                {members.map((member) => (
                    <div
                        key={member.employeeId}
                        className="flex items-start gap-2 rounded bg-white p-3"
                    >
                        <div className="flex-1 space-y-2">
                            <p className="text-sm font-medium text-foreground">
                                {member.employeeName}
                            </p>            <div className="space-y-1">
                            <Label
                                htmlFor={`position-${member.employeeId}`}
                                className="text-xs text-muted-foreground"
                            >
                                {t('memberPosition')}
                            </Label>
                            <Select
                                value={member.positionId}
                                onValueChange={(positionId) =>
                                    onUpdateMemberPosition(member.employeeId, positionId)
                                }
                            >
                                <SelectTrigger
                                    id={`position-${member.employeeId}`}
                                    className="h-9 text-sm"
                                >
                                    <SelectValue placeholder={t('selectPosition')} />
                                </SelectTrigger>
                                <SelectContent className="max-h-72 overflow-y-auto">
                                    {positionOptions.map((position) => (
                                        <SelectItem key={position.id} value={position.id}>
                                            {position.name}
                                        </SelectItem>
                                    ))}
                                </SelectContent>
                            </Select>
                        </div>
                        </div>
                        <button
                        type="button"
                        onClick={() => onRemoveMember(member.employeeId)}
                        className="rounded-full p-1 hover:bg-destructive/10 mt-1"
                    >
                        <X className="size-4 text-destructive" />
                    </button>
                    </div>
                ))}
            </div>
        )}
        </div>
    );
};