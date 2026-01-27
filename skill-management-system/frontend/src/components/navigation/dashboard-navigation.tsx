'use client';

import { useTranslations } from 'next-intl';
import { useSession } from 'next-auth/react';
import { usePathname } from 'next/navigation';
import Link from 'next/link';
import { LayoutDashboard, Search, Briefcase, Settings, User } from 'lucide-react';
import type { ComponentType } from 'react';
import {cn} from "@/utils/helpers";

interface NavItem {
  href: string;
  label: string;
  icon: ComponentType<any>;
  requiredRoles?: string[];
}

export function DashboardNavigation() {
  const t = useTranslations('welcome.navigation');
  const { data: session } = useSession();
  const pathname = usePathname();

  const hasRole = (roles: string[]) => {
    return roles.some(role => session?.user?.roles?.includes(role));
  };

  const navItems: NavItem[] = [
    {
      href: '/dashboard/home',
      label: t('dashboard'),
      icon: LayoutDashboard,
    },
    {
      href: '/dashboard/profile/me',
      label: t('profile'),
      icon: User,
    },
    {
      href: '/dashboard/discover/employees',
      label: t('employees'),
      icon: Search,
    },
    {
      href: '/dashboard/discover/projects',
      label: t('projects'),
      icon: Briefcase,
      requiredRoles: ['manager'],
    },
    {
      href: '/dashboard/admin',
      label: t('admin'),
      icon: Settings,
      requiredRoles: ['admin'],
    },
  ];

  // Filter nav items based on user roles
  const visibleNavItems = navItems.filter(item => {
    if (!item.requiredRoles) return true;
    return hasRole(item.requiredRoles);
  });

  // Check if current path is active
  const isActive = (href: string) => {
    return pathname?.includes(href);
  };

  return (
    <nav className="border-b border-border bg-white">
      <div className="mx-auto max-w-[1440px] px-6 lg:px-16">
        <div className="flex h-16 items-center gap-8 overflow-x-auto">
          {visibleNavItems.map((item) => {
            const Icon = item.icon;
            const active = isActive(item.href);

            return (
              <Link
                key={item.href}
                href={item.href}
                className={cn(
                  'flex items-center gap-2 whitespace-nowrap border-b-2 px-1 py-4 text-sm font-medium transition-colors hover:text-primary',
                  active
                    ? 'border-primary text-primary'
                    : 'border-transparent text-muted-foreground'
                )}
              >
                <Icon className="size-4" />
                {item.label}
              </Link>
            );
          })}
        </div>
      </div>
    </nav>
  );
}
