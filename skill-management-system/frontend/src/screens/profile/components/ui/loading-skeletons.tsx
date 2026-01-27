import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import {useTranslations} from "next-intl";

export const ProfileHeaderSkeleton = ({ isOwnProfile }: { isOwnProfile: boolean }) => {
    return (
        <div className="rounded-lg border border-border bg-white p-6 shadow-edag lg:p-8">
            <div className="flex flex-col gap-6 lg:flex-row lg:items-start lg:justify-between">
                <div className="flex flex-col items-center gap-6 sm:flex-row sm:items-start">
                    <div className="size-20 shrink-0 animate-pulse rounded-lg bg-muted" />

                    <div className="flex-1 space-y-4 text-center sm:text-left">
                        <div className="space-y-2">
                            <div className="h-8 w-48 animate-pulse rounded bg-muted" />
                            <div className="flex flex-wrap items-center justify-center gap-2 sm:justify-start">
                                <div className="h-6 w-32 animate-pulse rounded-full bg-muted" />
                                <div className="h-6 w-24 animate-pulse rounded-full bg-muted" />
                            </div>
                        </div>

                        <div className="grid gap-2">
                            <div className="h-5 w-56 animate-pulse rounded bg-muted" />
                            <div className="h-5 w-40 animate-pulse rounded bg-muted" />
                            <div className="h-5 w-48 animate-pulse rounded bg-muted" />
                        </div>
                    </div>
                </div>

                {isOwnProfile && (
                    <div className="flex flex-col items-center gap-4 lg:items-end">
                        <div className="flex gap-2">
                            <div className="h-9 w-24 animate-pulse rounded-md bg-muted" />
                            <div className="h-9 w-32 animate-pulse rounded-md bg-muted" />
                        </div>
                        <div className="h-24 w-full animate-pulse rounded-lg bg-muted lg:w-64" />
                    </div>
                )}
            </div>
        </div>
    );
};

export const ProfileStatsSkeleton = () => {
    return (
        <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-4">
            {Array.from({ length: 4 }, (_, i) => (
                <Card key={i} className="border-border bg-white">
                    <CardHeader className="flex flex-row items-center justify-between pb-4">
                        <div className="h-4 w-28 animate-pulse rounded bg-muted" />
                        <div className="size-10 animate-pulse rounded-lg bg-muted" />
                    </CardHeader>
                    <CardContent>
                        <div className="h-10 w-20 animate-pulse rounded bg-muted" />
                    </CardContent>
                </Card>
            ))}
        </div>
    );
};

export const SkillsSkeleton = ({ isOwnProfile }: { isOwnProfile: boolean }) => {
    const t = useTranslations('profile');

    return (
        <Card className="border-border bg-white shadow-edag">
            <CardHeader>
                <div className="flex items-center justify-between">
                    <CardTitle className="text-xl font-bold tracking-tight">{t('skills.title')}</CardTitle>
                    {isOwnProfile && <div className="h-9 w-32 animate-pulse rounded-md bg-muted" />}
                </div>
            </CardHeader>
            <CardContent className="space-y-6">
                {[1, 2].map((i) => (
                    <div key={i} className="space-y-4">
                        <div className="h-6 w-48 animate-pulse rounded bg-muted" />
                        <div className="grid gap-4 sm:grid-cols-2">
                            {[1, 2, 3].map((j) => (
                                <div key={j} className="rounded-lg border border-border bg-muted/30 p-4">
                                    <div className="space-y-3">
                                        <div className="flex items-start justify-between gap-2">
                                            <div className="flex-1 space-y-2">
                                                <div className="h-5 w-32 animate-pulse rounded bg-muted" />
                                                <div className="h-5 w-20 animate-pulse rounded bg-muted" />
                                            </div>
                                        </div>
                                        <div className="space-y-1.5">
                                            <div className="flex items-center justify-between">
                                                <div className="h-4 w-24 animate-pulse rounded bg-muted" />
                                                <div className="h-4 w-12 animate-pulse rounded bg-muted" />
                                            </div>
                                            <div className="flex gap-1">
                                                {[1, 2, 3, 4, 5].map((level) => (
                                                    <div
                                                        key={level}
                                                        className="h-2 flex-1 animate-pulse rounded-full bg-muted"
                                                    />
                                                ))}
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </div>
                ))}
            </CardContent>
        </Card>
    );
};

export const ProjectsSkeleton = () => {
    const t = useTranslations('profile');

    return (
        <Card className="border-border bg-white shadow-edag">
            <CardHeader>
                <CardTitle className="text-xl font-bold tracking-tight">{t('projects.title')}</CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
                {[1, 2, 3].map((i) => (
                    <div key={i} className="rounded-lg border border-border bg-muted/30 p-4">
                        <div className="space-y-3">
                            <div className="flex flex-col gap-2 sm:flex-row sm:items-start sm:justify-between">
                                <div className="flex-1 space-y-2">
                                    <div className="h-5 w-48 animate-pulse rounded bg-muted" />
                                    <div className="h-4 w-32 animate-pulse rounded bg-muted" />
                                </div>
                                <div className="h-6 w-24 animate-pulse rounded-full bg-muted" />
                            </div>
                            <div className="space-y-2">
                                <div className="h-4 w-full animate-pulse rounded bg-muted" />
                                <div className="h-4 w-3/4 animate-pulse rounded bg-muted" />
                            </div>
                            <div className="h-4 w-40 animate-pulse rounded bg-muted" />
                            <div className="flex flex-wrap gap-1.5">
                                {[1, 2, 3, 4].map((j) => (
                                    <div key={j} className="h-5 w-16 animate-pulse rounded bg-muted" />
                                ))}
                            </div>
                        </div>
                    </div>
                ))}
            </CardContent>
        </Card>
    );
};