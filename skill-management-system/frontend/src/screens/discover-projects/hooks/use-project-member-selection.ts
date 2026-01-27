import { useState, useCallback, useEffect } from 'react';
import { useRouter } from '@/libs/i18nNavigation';
import { useSearchParams } from 'next/navigation';

export interface ProjectMember {
    employeeId: string;
    employeeName: string;
    positionId: string;
    positionName: string;
}

export const useProjectMemberSelection = () => {
    const router = useRouter();
    const searchParams = useSearchParams();
    const [selectedMembers, setSelectedMembers] = useState<ProjectMember[]>([]);

    // Restore members from sessionStorage when returning from employee selection
    useEffect(() => {
        const fromSelection = searchParams.get('fromSelection');
        if (fromSelection === 'true') {
            const savedState = sessionStorage.getItem('projectDialogState');
            console.log('Loading from sessionStorage on return:', savedState);
            if (savedState) {
                try {
                    const parsed = JSON.parse(savedState);
                    // extract members
                    if (parsed.members && Array.isArray(parsed.members)) {
                        console.log('Setting members from state:', parsed.members);
                        setSelectedMembers(parsed.members);
                    }
                } catch (error) {
                    console.error('Failed to restore members:', error);
                }
            }
        }
    }, [searchParams]);

    const selectMembers = useCallback(
        (onBeforeNavigate?: () => void) => {
            console.log('Navigating to employee selection with members:', selectedMembers);
            onBeforeNavigate?.();
            router.push('/dashboard/discover/employees?mode=select&returnTo=projects');
        },
        [selectedMembers, router]
    );

    const removeMember = useCallback((employeeId: string) => {
        setSelectedMembers((prev) => prev.filter((m) => m.employeeId !== employeeId));
    }, []);

    const updateMemberPosition = useCallback(
        (employeeId: string, positionId: string, positionName: string) => {
            setSelectedMembers((prev) =>
                prev.map((member) =>
                    member.employeeId === employeeId
                        ? { ...member, positionId, positionName }
                        : member
                )
            );
        },
        []
    );

    const clearMembers = useCallback(() => {
        setSelectedMembers([]);
    }, []);

    const setMembers = useCallback((members: ProjectMember[]) => {
        console.log('Setting members manually:', members);
        setSelectedMembers(members);
    }, []);

    return {
        selectedMembers,
        selectMembers,
        removeMember,
        updateMemberPosition,
        clearMembers,
        setMembers,
    };
};