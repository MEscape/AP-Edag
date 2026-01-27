'use client';

import { useTranslations } from 'next-intl';
import { Mail, MapPin, Calendar } from 'lucide-react';
import { useFormatLocalizedDate } from '@/hooks/use-format-localized-date';

export const ContactInfo = ({
                                email,
                                location,
                                joinDate,
                            }: {
    email: string;
    location?: string;
    joinDate: string;
}) => {
    const t = useTranslations('profile');
    const { formatDate } = useFormatLocalizedDate();

    return (
        <div className="grid gap-2 text-sm text-muted-foreground">
            <div className="flex items-center gap-2">
                <Mail className="size-4" />
                <span>{email}</span>
            </div>

            {location && (
                <div className="flex items-center gap-2">
                    <MapPin className="size-4" />
                    <span>{location}</span>
                </div>
            )}

            <div className="flex items-center gap-2">
                <Calendar className="size-4" />
                <span>
          {t('joinedDate', {
              date: formatDate(joinDate, {
                  day: '2-digit',
                  month: 'short',
                  year: 'numeric',
              }),
          })}
        </span>
            </div>
        </div>
    );
};
