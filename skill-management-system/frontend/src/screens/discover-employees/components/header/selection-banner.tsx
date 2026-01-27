import { useTranslations } from 'next-intl';
import { Check, X } from 'lucide-react';
import { Card, CardContent } from '@/components/ui/card';
import { Button } from '@/components/ui/button';

interface SelectionBannerProps {
    isFilterMode: boolean;
    selectedCount: number;
    onCancel: () => void;
    onConfirm: () => void;
}

export const SelectionBanner = ({
                                    isFilterMode,
                                    selectedCount,
                                    onCancel,
                                    onConfirm,
                                }: SelectionBannerProps) => {
    const t = useTranslations('discoverEmployees');
    const mode = isFilterMode ? 'filterMode' : 'selectionMode';

    return (
        <Card className="border-primary/20 bg-primary/5">
            <CardContent className="flex items-center justify-between gap-4 p-4">
                <div className="flex-1">
                    <h3 className="font-semibold text-foreground">
                        {t(`${mode}.title`, { count: selectedCount })}
                    </h3>
                    <p className="text-sm text-muted-foreground">{t(`${mode}.description`)}</p>
                </div>

                <div className="flex gap-2">
                    <Button variant="outline" onClick={onCancel} className="gap-2">
                        <X className="size-4" />
                        {t(`${mode}.cancel`)}
                    </Button>

                    <Button onClick={onConfirm} disabled={selectedCount === 0} className="gap-2">
                        <Check className="size-4" />
                        {t(`${mode}.confirm`)}
                    </Button>
                </div>
            </CardContent>
        </Card>
    );
};