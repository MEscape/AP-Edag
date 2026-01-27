export const ProjectsSkeleton = () => {
    return (
        <div className="space-y-4">
            {[...Array(6)].map((_, index) => (
                <div
                    key={index}
                    className="flex items-center justify-between gap-4 rounded-lg border border-border bg-muted/30 p-4"
                >
                    <div className="flex-1 space-y-2">
                        <div className="h-5 w-3/4 animate-pulse rounded bg-muted" />
                        <div className="h-4 w-1/2 animate-pulse rounded bg-muted" />
                    </div>
                    <div className="h-6 w-24 shrink-0 animate-pulse rounded bg-muted" />
                </div>
            ))}
        </div>
    );
};