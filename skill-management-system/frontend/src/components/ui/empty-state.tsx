import { LucideIcon } from 'lucide-react';

interface EmptyStateProps {
  icon: LucideIcon;
  message: string;
}

export const EmptyState = ({ icon: Icon, message }: EmptyStateProps) => (
  <div className="flex flex-col items-center justify-center py-12 text-center">
    <div className="mb-4 flex size-16 items-center justify-center rounded-full bg-muted">
      <Icon className="size-8 text-muted-foreground" />
    </div>
    <p className="text-sm text-muted-foreground">{message}</p>
  </div>
);

