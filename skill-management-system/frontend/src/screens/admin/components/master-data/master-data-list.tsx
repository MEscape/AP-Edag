import { Card, CardContent } from '@/components/ui/card';
import { EmptyState } from '@/components/ui/empty-state';
import { LucideIcon } from 'lucide-react';
import React from "react";

interface MasterDataItem {
    id: string;
    name: string;
}

interface MasterDataListProps<T extends MasterDataItem> {
    items: T[];
    emptyMessage: string;
    emptyIcon: LucideIcon;
    renderExtra?: (item: T) => React.ReactNode;
}

export const MasterDataList = <T extends MasterDataItem>({
                                                             items,
                                                             emptyMessage,
                                                             emptyIcon,
                                                             renderExtra,
                                                         }: MasterDataListProps<T>) => {

    if (items.length === 0) {
        return <EmptyState icon={emptyIcon} message={emptyMessage} />;
    }

    return (
        <div className="space-y-2 max-h-80 overflow-y-auto">
            {items.map((item) => (
                <Card key={item.id} className="border-border bg-white">
                    <CardContent className="flex items-center justify-between p-4">
                        <div className="flex items-center gap-2">
                            <p className="font-medium text-foreground">{item.name}</p>
                            {renderExtra && renderExtra(item)}
                        </div>
                    </CardContent>
                </Card>
            ))}
        </div>
    );
};