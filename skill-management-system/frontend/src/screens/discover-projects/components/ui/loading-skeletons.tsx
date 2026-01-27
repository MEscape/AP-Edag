export const ProjectsSkeleton = () => {
    return (
        <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
            {[...Array(6)].map((_, index) => (
                <div
                    key={index}
                    className="rounded-lg border border-border bg-white p-6"
                >
                    <div className="space-y-5">
                        <div className="space-y-3">
                            <div className="flex items-start justify-between gap-3">
                                <div className="min-w-0 flex-1 space-y-2">
                                    <div className="h-6 w-3/4 animate-pulse rounded bg-muted" />
                                    <div className="h-4 w-full animate-pulse rounded bg-muted" />
                                    <div className="h-4 w-5/6 animate-pulse rounded bg-muted" />
                                </div>
                                <div className="size-8 animate-pulse rounded bg-muted" />
                            </div>
                            <div className="flex gap-2">
                                <div className="h-6 w-20 animate-pulse rounded bg-muted" />
                            </div>
                        </div>

                        <div className="space-y-2.5 border-t border-border pt-4">
                            {[1, 2, 3].map((i) => (
                                <div key={i} className="h-5 w-full animate-pulse rounded bg-muted" />
                            ))}
                        </div>

                        <div className="border-t border-border pt-4">
                            <div className="flex flex-wrap gap-2">
                                {[1, 2, 3, 4].map((i) => (
                                    <div key={i} className="h-7 w-20 animate-pulse rounded bg-muted" />
                                ))}
                            </div>
                        </div>

                        <div className="border-t border-border pt-4">
                            <div className="h-9 w-full animate-pulse rounded bg-muted" />
                        </div>
                    </div>
                </div>
            ))}
        </div>
    );
};

export const FilterPanelSkeleton = () => {
    return (
        <div className="rounded-lg border border-border bg-white p-6">
            <div className="space-y-6">
                <div className="h-6 w-24 animate-pulse rounded bg-muted" />

                {[1, 2, 3].map((section) => (
                    <div key={section} className="space-y-3">
                        <div className="h-5 w-32 animate-pulse rounded bg-muted" />
                        <div className="space-y-2">
                            {[1, 2, 3].map((item) => (
                                <div key={item} className="flex items-center gap-2">
                                    <div className="size-4 animate-pulse rounded bg-muted" />
                                    <div className="h-4 flex-1 animate-pulse rounded bg-muted" />
                                </div>
                            ))}
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};