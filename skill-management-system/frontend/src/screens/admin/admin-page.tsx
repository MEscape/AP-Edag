'use client';

import { useState } from 'react';
import { useTranslations } from 'next-intl';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import { Users, Shield, FolderTree } from 'lucide-react';
import { UsersSection } from './components/sections/user-section';
import { RoleRequestsSection } from './components/sections/role-requests-section';
import { MasterDataSection } from './components/sections/master-data-section';
import {PageHeader} from "@/components/ui/page-header";

type AdminTab = 'users' | 'roleRequests' | 'masterData';

const AdminPage = () => {
  const t = useTranslations('admin');
  const [activeTab, setActiveTab] = useState<AdminTab>('users');

  return (
      <div className="min-h-screen bg-muted/30">
        <div className="pointer-events-none absolute inset-0 bg-gradient-to-br from-primary/[0.02] via-transparent to-transparent" />

        <div className="relative mx-auto max-w-[1440px] space-y-8 px-6 py-8 lg:px-16 lg:py-12">
          <PageHeader
              title={t('title')}
              description={t('description')}
          />

          <Tabs value={activeTab} onValueChange={(value) => setActiveTab(value as AdminTab)} className="space-y-6">
            <TabsList className="grid w-full grid-cols-3 lg:w-auto lg:inline-grid">
              <TabsTrigger value="users" className="gap-2">
                <Users className="size-4" />
                <span className="hidden sm:inline">{t('tabs.users')}</span>
              </TabsTrigger>
              <TabsTrigger value="roleRequests" className="gap-2">
                <Shield className="size-4" />
                <span className="hidden sm:inline">{t('tabs.roleRequests')}</span>
              </TabsTrigger>
              <TabsTrigger value="masterData" className="gap-2">
                <FolderTree className="size-4" />
                <span className="hidden sm:inline">{t('tabs.masterData')}</span>
              </TabsTrigger>
            </TabsList>

            <TabsContent value="users" className="space-y-6">
              <UsersSection />
            </TabsContent>

            <TabsContent value="roleRequests" className="space-y-6">
              <RoleRequestsSection />
            </TabsContent>

            <TabsContent value="masterData" className="space-y-6">
              <MasterDataSection />
            </TabsContent>
          </Tabs>
        </div>
      </div>
  );
};

export default AdminPage