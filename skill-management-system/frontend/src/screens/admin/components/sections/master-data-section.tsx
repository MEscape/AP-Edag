'use client';

import { useState } from 'react';
import { useTranslations } from 'next-intl';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import { FolderTree, MapPin, Briefcase, Award, FolderOpen } from 'lucide-react';
import { LocationsManager } from '@/screens/admin/components/master-data/managers/locations-manager';
import { PositionsManager } from '@/screens/admin/components/master-data/managers/positions-manager';
import { SkillCategoriesManager } from '@/screens/admin/components/master-data/managers/skill-categories-manager';
import { SkillsManager } from '@/screens/admin/components/master-data/managers/skills-manager';

type MasterDataTab = 'locations' | 'positions' | 'skillCategories' | 'skills';

export const MasterDataSection = () => {
    const t = useTranslations('admin.masterData');
    const [activeTab, setActiveTab] = useState<MasterDataTab>('locations');

    return (
        <Card className="border-border bg-white shadow-edag">
            <CardHeader>
                <CardTitle className="flex items-center gap-2 text-xl font-bold text-foreground">
                    <FolderTree className="size-5 text-primary" />
                    {t('title')}
                </CardTitle>
                <CardDescription className="text-base text-muted-foreground">
                    {t('description')}
                </CardDescription>
            </CardHeader>
            <CardContent>
                <Tabs value={activeTab} onValueChange={(value) => setActiveTab(value as MasterDataTab)} className="space-y-6">
                    <TabsList className="grid w-full grid-cols-2 lg:grid-cols-4">
                        <TabsTrigger value="locations" className="gap-2">
                            <MapPin className="size-4" />
                            <span className="hidden sm:inline">{t('tabs.locations')}</span>
                        </TabsTrigger>
                        <TabsTrigger value="positions" className="gap-2">
                            <Briefcase className="size-4" />
                            <span className="hidden sm:inline">{t('tabs.positions')}</span>
                        </TabsTrigger>
                        <TabsTrigger value="skillCategories" className="gap-2">
                            <FolderOpen className="size-4" />
                            <span className="hidden sm:inline">{t('tabs.skillCategories')}</span>
                        </TabsTrigger>
                        <TabsTrigger value="skills" className="gap-2">
                            <Award className="size-4" />
                            <span className="hidden sm:inline">{t('tabs.skills')}</span>
                        </TabsTrigger>
                    </TabsList>

                    <TabsContent value="locations">
                        <LocationsManager />
                    </TabsContent>

                    <TabsContent value="positions">
                        <PositionsManager />
                    </TabsContent>

                    <TabsContent value="skillCategories">
                        <SkillCategoriesManager />
                    </TabsContent>

                    <TabsContent value="skills">
                        <SkillsManager />
                    </TabsContent>
                </Tabs>
            </CardContent>
        </Card>
    );
};