import {Card, CardContent, CardHeader} from '@/components/ui/card';

export const EmployeeGridSkeleton = () => {
    return (
        <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
            {Array.from({ length: 12 }).map((_, i) => (
                <Card key={i} className="border-border bg-white">
                    <CardContent className="p-6">
                        <div className="space-y-5">
                            <div className="flex items-start gap-4">
                                <div className="size-14 animate-pulse rounded-lg bg-muted" />
                                <div className="flex-1 space-y-2">
                                    <div className="h-5 w-3/4 animate-pulse rounded bg-muted" />
                                    <div className="h-4 w-1/2 animate-pulse rounded bg-muted" />
                                    <div className="h-6 w-24 animate-pulse rounded bg-muted" />
                                </div>
                            </div>

                            <div className="space-y-2">
                                <div className="h-4 w-full animate-pulse rounded bg-muted" />
                                <div className="h-4 w-full animate-pulse rounded bg-muted" />
                            </div>

                            <div className="flex gap-4">
                                {[1, 2, 3].map((j) => (
                                    <div key={j} className="flex-1 space-y-2">
                                        <div className="h-8 w-12 animate-pulse rounded bg-muted" />
                                        <div className="h-3 w-full animate-pulse rounded bg-muted" />
                                    </div>
                                ))}
                            </div>

                            <div className="space-y-3">
                                <div className="h-4 w-20 animate-pulse rounded bg-muted" />
                                <div className="flex flex-wrap gap-2">
                                    {[1, 2, 3].map((j) => (
                                        <div key={j} className="h-7 w-20 animate-pulse rounded bg-muted" />
                                    ))}
                                </div>
                            </div>

                            <div className="h-11 w-full animate-pulse rounded bg-muted" />
                        </div>
                    </CardContent>
                </Card>
            ))}
        </div>
    );
};

export const FilterPanelSkeleton = () => {
    return (
        <Card className="border-border bg-white shadow-sm">
            <CardHeader>
                <div className="h-6 w-32 animate-pulse rounded bg-muted" />
            </CardHeader>
            <CardContent className="space-y-6">
                {[1, 2, 3, 4, 5].map((i) => (
                    <div key={i} className="space-y-3">
                        <div className="h-5 w-24 animate-pulse rounded bg-muted" />
                        <div className="h-10 w-full animate-pulse rounded bg-muted" />
                    </div>
                ))}
            </CardContent>
        </Card>
    );
};