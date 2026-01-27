export const ProjectDetailHeaderSkeleton = () => {
    return (
        <div className="space-y-6">
            <div className="space-y-3">
                <div className="flex flex-wrap items-start justify-between gap-4">
                    <div className="h-9 w-2/3 animate-pulse rounded bg-muted" />
                    <div className="h-7 w-24 animate-pulse rounded bg-muted" />
                </div>
                <div className="h-5 w-full animate-pulse rounded bg-muted" />
                <div className="h-5 w-4/5 animate-pulse rounded bg-muted" />
            </div>

            <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
                {[...Array(4)].map((_, i) => (
                    <div
                        key={i}
                        className="flex items-center gap-3 rounded-lg border border-border bg-muted/30 p-4"
                    >
                        <div className="size-10 shrink-0 animate-pulse rounded-lg bg-muted" />
                        <div className="min-w-0 flex-1 space-y-2">
                            <div className="h-3 w-16 animate-pulse rounded bg-muted" />
                            <div className="h-4 w-24 animate-pulse rounded bg-muted" />
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export const ProjectDetailInfoSkeleton = () => {
    return (
        <div className="space-y-6">
            <div className="space-y-2">
                <div className="h-5 w-24 animate-pulse rounded bg-muted" />
                <div className="h-4 w-full animate-pulse rounded bg-muted" />
                <div className="h-4 w-full animate-pulse rounded bg-muted" />
                <div className="h-4 w-3/4 animate-pulse rounded bg-muted" />
            </div>

            <div className="space-y-4 rounded-lg border border-border bg-muted/30 p-4">
                <div className="h-5 w-32 animate-pulse rounded bg-muted" />
                <div className="grid gap-4 sm:grid-cols-2">
                    {[...Array(2)].map((_, i) => (
                        <div key={i} className="space-y-1">
                            <div className="h-3 w-20 animate-pulse rounded bg-muted" />
                            <div className="h-4 w-32 animate-pulse rounded bg-muted" />
                        </div>
                    ))}
                </div>
                <div className="flex items-center gap-2 rounded-lg border border-border bg-white p-3">
                    <div className="size-4 shrink-0 animate-pulse rounded bg-muted" />
                    <div className="flex-1 space-y-1">
                        <div className="h-3 w-24 animate-pulse rounded bg-muted" />
                        <div className="h-4 w-32 animate-pulse rounded bg-muted" />
                    </div>
                </div>
            </div>

            <div className="space-y-2">
                <div className="h-5 w-40 animate-pulse rounded bg-muted" />
                <div className="h-4 w-48 animate-pulse rounded bg-muted" />
            </div>
        </div>
    );
};

export const ProjectDetailMembersSkeleton = () => {
    return (
        <div className="space-y-3">
            {[...Array(3)].map((_, i) => (
                <div
                    key={i}
                    className="flex items-center justify-between gap-4 rounded-lg border border-border bg-muted/30 p-4"
                >
                    <div className="flex min-w-0 flex-1 items-center gap-4">
                        <div className="size-12 shrink-0 animate-pulse rounded-lg bg-muted" />
                        <div className="min-w-0 flex-1 space-y-2">
                            <div className="h-5 w-32 animate-pulse rounded bg-muted" />
                            <div className="h-4 w-24 animate-pulse rounded bg-muted" />
                        </div>
                    </div>
                    <div className="h-9 w-28 shrink-0 animate-pulse rounded bg-muted" />
                </div>
            ))}
        </div>
    );
};

export const ProjectDetailTechnologiesSkeleton = () => {
    return (
        <div className="flex flex-wrap gap-2">
            {[...Array(6)].map((_, i) => (
                <div key={i} className="h-8 w-20 animate-pulse rounded bg-muted" />
            ))}
        </div>
    );
};