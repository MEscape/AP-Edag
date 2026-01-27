import { Card, CardContent, CardHeader } from '@/components/ui/card';

export const DashboardCardsSkeleton = () => {
    const skeletonIds = ['s1', 's2', 's3', 's4'];

    return (
        <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-4">
            {skeletonIds.map((id) => (
                <Card key={id} className="border-border bg-white">
                    <CardHeader className="flex flex-row items-center justify-between pb-4">
                        <div className="h-4 w-28 animate-pulse rounded bg-muted" />
                        <div className="size-14 animate-pulse rounded-lg bg-muted" />
                    </CardHeader>
                    <CardContent>
                        <div className="h-10 w-20 animate-pulse rounded bg-muted" />
                    </CardContent>
                </Card>
            ))}
        </div>
    );
};

export const SkillChartSkeleton = () => (
    <Card className="group col-span-1 border-border bg-white lg:col-span-2">
        <CardHeader className="space-y-3 pb-4">
            <div className="h-7 w-56 animate-pulse rounded-sm bg-muted" />
            <div className="h-4 w-72 animate-pulse rounded-sm bg-muted/70" />
        </CardHeader>
        <CardContent className="pb-6">
            <div className="h-[340px] w-full animate-pulse rounded-sm bg-muted/50" />
        </CardContent>
    </Card>
);

export const ProjectListSkeleton = () => {
    const skeletonIds = ['s1', 's2', 's3', 's4', 's5', 's6'];

    return (
        <Card className="border-border bg-white">
            <CardHeader>
                <div className="h-6 w-40 animate-pulse rounded bg-muted" />
                <div className="h-4 w-56 animate-pulse rounded bg-muted" />
            </CardHeader>
            <CardContent>
                <div className="space-y-4">
                    {skeletonIds.map((id) => (
                        <div
                            key={id}
                            className="flex h-[60px] items-center justify-between border-b border-border pb-4 last:border-0"
                        >
                            <div className="flex-1 space-y-2">
                                <div className="h-5 w-3/4 animate-pulse rounded bg-muted" />
                                <div className="h-4 w-1/2 animate-pulse rounded bg-muted" />
                            </div>
                            <div className="h-6 w-24 animate-pulse rounded bg-muted" />
                        </div>
                    ))}
                </div>
            </CardContent>
        </Card>
    );
};

export const ActivityListSkeleton = () => {
    const skeletonIds = ['s1', 's2', 's3', 's4', 's5', 's6'];

    return (
        <Card className="border-border bg-white">
            <CardHeader>
                <div className="h-6 w-40 animate-pulse rounded bg-muted" />
                <div className="h-4 w-56 animate-pulse rounded bg-muted" />
            </CardHeader>
            <CardContent>
                <div className="space-y-4">
                    {skeletonIds.map((id) => (
                        <div
                            key={id}
                            className="flex h-[60px] items-center justify-between border-b border-border pb-4 last:border-0"
                        >
                            <div className="flex items-center gap-3">
                                <div className="size-10 shrink-0 animate-pulse rounded-full bg-muted" />
                                <div className="space-y-2">
                                    <div className="h-5 w-48 animate-pulse rounded bg-muted" />
                                    <div className="h-4 w-32 animate-pulse rounded bg-muted" />
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            </CardContent>
        </Card>
    );
};