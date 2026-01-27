import { ReactNode } from 'react';
import { LucideIcon } from 'lucide-react';

interface PageHeaderProps {
    // Content
    title: string;
    description?: string | ReactNode;
    icon?: LucideIcon;

    // Right side content
    actions?: ReactNode;

    // Styling
    titleSize?: 'sm' | 'md' | 'lg' | 'xl';
    className?: string;
}

const titleSizeClasses = {
    sm: 'text-2xl lg:text-3xl',
    md: 'text-3xl lg:text-4xl',
    lg: 'text-4xl lg:text-5xl',
    xl: 'text-5xl lg:text-6xl',
};

export const PageHeader = ({
                               title,
                               description,
                               icon: Icon,
                               actions,
                               titleSize = 'lg',
                               className = '',
                           }: PageHeaderProps) => {
    return (
        <div className={`space-y-2 ${className}`}>
            <div className={`flex items-center ${actions ? 'justify-between' : ''}`}>
                <div className="flex items-center gap-3">
                    {Icon && <Icon className="size-8 text-primary lg:size-10" />}
                    <h1 className={`font-bold tracking-tight text-foreground ${titleSizeClasses[titleSize]}`}>
                        {title}
                    </h1>
                </div>

                {actions && <div className="flex items-center gap-3">{actions}</div>}
            </div>

            {description && (
                <div className="text-lg text-muted-foreground">
                    {description}
                </div>
            )}
        </div>
    );
};