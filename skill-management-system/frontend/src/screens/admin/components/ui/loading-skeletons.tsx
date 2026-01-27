export const LoadingSkeletons = () => {
    return (
        <div className="space-y-3">
            {[...Array(8)].map((_, index) => (
                <div
                    key={index}
                    className="flex items-center justify-between rounded-lg border border-border bg-white p-4"
                >
                    <div className="flex items-center gap-3">
                        <div className="size-8 animate-pulse rounded bg-muted" />
                        <div className="space-y-2">
                            <div className="h-4 w-40 animate-pulse rounded bg-muted" />
                            <div className="h-3 w-24 animate-pulse rounded bg-muted" />
                        </div>
                    </div>
                    <div className="flex items-center gap-2">
                        <div className="h-8 w-8 animate-pulse rounded bg-muted" />
                        <div className="h-8 w-8 animate-pulse rounded bg-muted" />
                    </div>
                </div>
            ))}
        </div>
    );
};

export const RoleRequestsSkeleton = () => {
    return (
        <div className="space-y-4">
            {[...Array(3)].map((_, index) => (
                <div
                    key={index}
                    className="rounded-lg border border-border bg-white p-6 shadow-sm"
                >
                    <div className="flex items-start justify-between">
                        <div className="flex-1 space-y-4">
                            <div className="flex items-center gap-3">
                                <div className="size-12 animate-pulse rounded-full bg-muted" />
                                <div className="space-y-2">
                                    <div className="h-5 w-40 animate-pulse rounded bg-muted" />
                                    <div className="h-4 w-48 animate-pulse rounded bg-muted" />
                                </div>
                            </div>

                            <div className="flex flex-wrap gap-4">
                                <div className="flex items-center gap-2">
                                    <div className="size-4 animate-pulse rounded bg-muted" />
                                    <div className="h-4 w-32 animate-pulse rounded bg-muted" />
                                </div>
                                <div className="flex items-center gap-2">
                                    <div className="size-4 animate-pulse rounded bg-muted" />
                                    <div className="h-4 w-24 animate-pulse rounded bg-muted" />
                                </div>
                            </div>

                            <div className="space-y-2">
                                <div className="h-4 w-full animate-pulse rounded bg-muted" />
                                <div className="h-4 w-3/4 animate-pulse rounded bg-muted" />
                            </div>
                        </div>

                        <div className="flex gap-2">
                            <div className="h-9 w-24 animate-pulse rounded-md bg-muted" />
                            <div className="h-9 w-24 animate-pulse rounded-md bg-muted" />
                        </div>
                    </div>
                </div>
            ))}
        </div>
    );
};

export const UsersTableSkeleton = () => {
    return (
        <div className="space-y-4">
            <div className="rounded-lg border border-border bg-white">
                <div className="grid grid-cols-4 gap-4 border-b border-border p-4">
                    <div className="h-4 w-24 animate-pulse rounded bg-muted" />
                    <div className="h-4 w-20 animate-pulse rounded bg-muted" />
                    <div className="h-4 w-32 animate-pulse rounded bg-muted" />
                    <div className="h-4 w-24 animate-pulse rounded bg-muted" />
                </div>

                {[...Array(5)].map((_, index) => (
                    <div key={index} className="grid grid-cols-4 gap-4 border-b border-border p-4 last:border-b-0">
                        <div className="flex items-center gap-3">
                            <div className="size-10 animate-pulse rounded-full bg-muted" />
                            <div className="space-y-2">
                                <div className="h-4 w-32 animate-pulse rounded bg-muted" />
                                <div className="h-3 w-40 animate-pulse rounded bg-muted" />
                            </div>
                        </div>
                        <div className="flex items-center">
                            <div className="h-6 w-16 animate-pulse rounded-full bg-muted" />
                        </div>
                        <div className="flex items-center">
                            <div className="h-4 w-28 animate-pulse rounded bg-muted" />
                        </div>
                        <div className="flex items-center gap-2">
                            <div className="h-8 w-8 animate-pulse rounded bg-muted" />
                            <div className="h-8 w-8 animate-pulse rounded bg-muted" />
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};
