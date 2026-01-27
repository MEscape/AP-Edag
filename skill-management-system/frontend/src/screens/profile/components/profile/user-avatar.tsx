'use client';

export const UserAvatar = ({
                               firstName,
                               lastName,
                           }: {
    firstName: string;
    lastName: string;
}) => {
    const initials = `${firstName[0]}${lastName[0]}`;

    return (
        <div className="flex size-20 items-center justify-center rounded-lg bg-primary/10 text-2xl font-bold text-primary">
            {initials}
        </div>
    );
};
