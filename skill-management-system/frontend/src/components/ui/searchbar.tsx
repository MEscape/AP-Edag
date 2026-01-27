import { Search } from 'lucide-react';
import { Input } from '@/components/ui/input';
import React, { useState, useEffect } from 'react';
import { useDebounce } from '@/hooks/use-debounce';

interface SearchBarProps {
  onSearch: (searchTerm: string) => void;
  placeholder: string;
  isLoading?: boolean;
}

export const Searchbar = ({ onSearch, placeholder, isLoading }: SearchBarProps) => {
  const [searchTerm, setSearchTerm] = useState('');
  const debouncedSearchTerm = useDebounce(searchTerm, 500);

  useEffect(() => {
    onSearch(debouncedSearchTerm);
  }, [debouncedSearchTerm, onSearch]);

  return (
    <div className="relative w-full">
      <Search className="absolute left-4 top-1/2 size-5 -translate-y-1/2 text-muted-foreground" />
      <Input
        type="text"
        placeholder={placeholder}
        value={searchTerm}
        onChange={(e: React.ChangeEvent<HTMLInputElement>) => setSearchTerm(e.target.value)}
        disabled={isLoading}
        className="h-12 pl-12 pr-4 text-base font-medium transition-all focus-visible:border-primary/20 focus-visible:shadow-md"
      />
    </div>
  );
};
