'use client';

import { useTranslations } from 'next-intl';
import Link from 'next/link';
import { ArrowRight, Briefcase, Search, Users, Shield, Settings } from 'lucide-react';
import { RoleRequestDialog } from '../role-requests/role-request-dialog';
import { useDialog } from '@/hooks/use-dialog';
import type { RoleType } from '@/store/api/role-request-api';
import type { LucideIcon } from 'lucide-react';
import { useState } from 'react';

interface QuickAction {
  title: string;
  description: string;
  icon: LucideIcon;
  href?: string;
  onClick?: () => void;
  highlight?: boolean;
  requiredRoles?: string[];
  excludedRoles?: string[];
}

interface QuickActionsListProps {
  userRoles: string[];
  onCreateRoleRequest: (params: {
    requestedRole: RoleType;
    reason: string;
  }) => Promise<{ success: boolean; error?: string }>;
  isCreating: boolean;
}

export const QuickActionsList = ({
                                   userRoles,
                                   onCreateRoleRequest,
                                   isCreating,
                                 }: QuickActionsListProps) => {
  const t = useTranslations('dashboard.quickActions');

  const {
    isOpen: isDialogOpen,
    open: openDialog,
    close: closeDialog,
    setIsOpen: setDialogOpen,
  } = useDialog();

  const [error, setError] = useState<string | null>(null);

  const handleSubmit = async (params: { requestedRole: RoleType; reason: string }) => {
    setError(null);
    const result = await onCreateRoleRequest(params);

    if (result.success) {
      closeDialog();
    } else if (result.error) {
      setError(result.error);
    }

    return result;
  };

  const allActions: QuickAction[] = [
    {
      title: t('requestRole'),
      description: t('requestRoleDescription'),
      icon: Shield,
      onClick: openDialog,
      highlight: true,
      excludedRoles: ['admin'],
    },
    {
      title: t('goToProfile'),
      description: t('goToProfileDescription'),
      icon: Users,
      href: '/dashboard/profile/me',
    },
    {
      title: t('findEmployee'),
      description: t('findEmployeeDescription'),
      icon: Search,
      href: '/dashboard/discover/employees',
    },
    {
      title: t('viewProjects'),
      description: t('viewProjectsDescription'),
      icon: Briefcase,
      href: '/dashboard/discover/projects',
      requiredRoles: ['manager'],
    },
    {
      title: t('adminPanel'),
      description: t('adminPanelDescription'),
      icon: Settings,
      href: '/dashboard/admin',
      highlight: true,
      requiredRoles: ['admin'],
    },
  ];

  const actions = allActions.filter((action) => {
    if (action.excludedRoles?.some((role) => userRoles.includes(role))) {
      return false;
    }
    if (!action.requiredRoles) return true;
    return action.requiredRoles.some((role) => userRoles.includes(role));
  });

  return (
      <>
        <div className="space-y-3">
          {actions.map((action) => {
            const className = `group flex w-full items-center gap-4 rounded-lg border p-4 text-left transition-all hover:shadow-md ${
                action.highlight
                    ? 'border-primary/30 bg-primary/5 hover:border-primary/50 hover:bg-primary/10'
                    : 'border-border bg-white hover:border-primary/20'
            }`;

            const content = (
                <>
                  <div
                      className={`flex size-12 shrink-0 items-center justify-center rounded-lg transition-all group-hover:scale-110 ${
                          action.highlight ? 'bg-primary/20' : 'bg-primary/10'
                      }`}
                  >
                    <action.icon className="size-6 text-primary" strokeWidth={2} />
                  </div>
                  <div className="flex-1">
                    <p
                        className={`font-semibold group-hover:text-primary ${
                            action.highlight ? 'text-primary' : 'text-foreground'
                        }`}
                    >
                      {action.title}
                    </p>
                    <p className="text-sm text-muted-foreground">{action.description}</p>
                  </div>
                  <ArrowRight className="size-5 text-muted-foreground opacity-0 transition-all group-hover:translate-x-1 group-hover:opacity-100" />
                </>
            );

            if (action.href) {
              return (
                  <Link key={action.title} href={action.href} className={className}>
                    {content}
                  </Link>
              );
            }

            return (
                <button key={action.title} type="button" onClick={action.onClick} className={className}>
                  {content}
                </button>
            );
          })}
        </div>

        <RoleRequestDialog
            open={isDialogOpen}
            onOpenChange={setDialogOpen}
            onSubmit={handleSubmit}
            isLoading={isCreating}
            error={error}
            onClearError={() => setError(null)}
        />
      </>
  );
};
