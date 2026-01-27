import { BarChart3, Target, TrendingUp, Users } from 'lucide-react';

export const SkillVisualization = () => {
  return (
      <div className="relative hidden lg:flex lg:items-center lg:justify-center">
        <div className="relative">
          <div className="relative h-[480px] w-[480px]">
            <div className="absolute left-1/2 top-1/2 z-10 -translate-x-1/2 -translate-y-1/2 rounded-lg border border-border bg-white p-8 shadow-lg">
              <div className="mb-4 flex size-16 items-center justify-center rounded-lg bg-primary/10">
                <BarChart3 className="size-8 text-primary" strokeWidth={2} />
              </div>
              <h3 className="mb-2 text-lg font-semibold text-foreground">Skill Matrix</h3>
              <p className="text-sm text-muted-foreground">Intelligente Ressourcenzuweisung</p>
            </div>

            <div className="absolute left-8 top-16 rounded-lg border border-border bg-white p-4 shadow-md">
              <div className="flex items-center gap-3">
                <div className="flex size-10 items-center justify-center rounded bg-primary/10">
                  <Target className="size-5 text-primary" />
                </div>
                <div>
                  <div className="text-xs font-medium text-muted-foreground">Skills</div>
                  <div className="text-lg font-bold text-foreground">1.000+</div>
                </div>
              </div>
            </div>

            <div className="absolute bottom-16 right-8 rounded-lg border border-border bg-white p-4 shadow-md">
              <div className="flex items-center gap-3">
                <div className="flex size-10 items-center justify-center rounded bg-success/10">
                  <Users className="size-5 text-success" />
                </div>
                <div>
                  <div className="text-xs font-medium text-muted-foreground">User</div>
                  <div className="text-lg font-bold text-foreground">500+</div>
                </div>
              </div>
            </div>

            <div className="absolute right-16 top-24 rounded-lg border border-border bg-white p-4 shadow-md">
              <div className="flex items-center gap-3">
                <div className="flex size-10 items-center justify-center rounded bg-warning/10">
                  <TrendingUp className="size-5 text-warning" />
                </div>
                <div>
                  <div className="text-xs font-medium text-muted-foreground">Teams</div>
                  <div className="text-lg font-bold text-foreground">50+</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
  );
};