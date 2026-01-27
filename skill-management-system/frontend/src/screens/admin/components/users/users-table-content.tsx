import { useTranslations } from 'next-intl';
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table';
import { useFormatLocalizedDate } from '@/hooks/use-format-localized-date';
import type { User } from '@/store/api/admin-api';
import { EmptyState } from "@/components/ui/empty-state";
import { Users } from "lucide-react";

interface UsersTableContentProps {
    users: User[];
}

export const UsersTableContent = ({ users }: UsersTableContentProps) => {
    const t = useTranslations('admin.users');
    const { formatDate } = useFormatLocalizedDate();

    if (users.length === 0) {
        return <EmptyState icon={Users} message={t('noUsers')} />;
    }

    return (
        <div className="rounded-lg border border-border">
            <Table>
                <TableHeader>
                    <TableRow>
                        <TableHead>{t('table.username')}</TableHead>
                        <TableHead>{t('table.email')}</TableHead>
                        <TableHead>{t('table.name')}</TableHead>
                        <TableHead>{t('table.createdAt')}</TableHead>
                        <TableHead>{t('table.updatedAt')}</TableHead>
                    </TableRow>
                </TableHeader>
                <TableBody>
                    {users.map((user) => (
                        <TableRow key={user.id}>
                            <TableCell className="font-medium">{user.username}</TableCell>
                            <TableCell>{user.email}</TableCell>
                            <TableCell>
                                {user.firstName} {user.lastName}
                            </TableCell>
                            <TableCell className="text-sm text-muted-foreground">
                                {formatDate(user.createdAt, { day: '2-digit', month: 'short', year: 'numeric' })}
                            </TableCell>
                            <TableCell className="text-sm text-muted-foreground">
                                {formatDate(user.updatedAt, { day: '2-digit', month: 'short', year: 'numeric' })}
                            </TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </div>
    );
};