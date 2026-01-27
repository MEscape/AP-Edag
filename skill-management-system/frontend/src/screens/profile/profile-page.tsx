import { ProfileHeaderSection } from './components/sections/profile-header-section';
import { ProfileStatsSection } from './components/sections/profile-stats-section';
import { SkillsSection } from './components/sections/skills-section';
import { ProjectsSection } from './components/sections/projects-section';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';

interface ProfilePageProps {
  userId: string;
}

const ProfilePage = async ({ userId }: ProfilePageProps) => {
  return (
      <div className="min-h-screen bg-muted/30">
        <div className="pointer-events-none absolute inset-0 bg-gradient-to-br from-primary/[0.02] via-transparent to-transparent" />

        <div className="relative mx-auto max-w-[1440px] space-y-8 px-6 py-8 lg:px-16 lg:py-12">
          <ProfileHeaderSection userId={userId} />
          <ProfileStatsSection userId={userId} />

          <Tabs defaultValue="skills" className="space-y-6">
            <TabsList className="grid w-full grid-cols-2 lg:w-auto lg:grid-cols-2">
              <TabsTrigger value="skills">Skills</TabsTrigger>
              <TabsTrigger value="projects">Projects</TabsTrigger>
            </TabsList>

            <TabsContent value="skills" className="space-y-6">
              <SkillsSection userId={userId} />
            </TabsContent>

            <TabsContent value="projects" className="space-y-6">
              <ProjectsSection userId={userId} />
            </TabsContent>
          </Tabs>
        </div>
      </div>
  );
};

export default ProfilePage;