'use client';

import React, { useRef, useState, useCallback } from 'react';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';

interface AutocompleteInputProps {
    id: string;
    label: string;
    value: string;
    suggestions: string[];
    onChange: (value: string) => void;
    onSelect?: (value: string) => void;
    placeholder?: string;
    maxLength?: number;
    required?: boolean;
    disabled?: boolean;
    noSuggestionsText?: string;
}

export function AutocompleteInput({
                                      id,
                                      label,
                                      value,
                                      suggestions,
                                      onChange,
                                      onSelect,
                                      placeholder,
                                      maxLength = 100,
                                      required = false,
                                      disabled = false,
                                      noSuggestionsText = 'No suggestions found',
                                  }: AutocompleteInputProps) {
    const [showSuggestions, setShowSuggestions] = useState(false);
    const [highlightedIndex, setHighlightedIndex] = useState(-1);
    const inputRef = useRef<HTMLInputElement>(null);

    const filteredSuggestions = suggestions.filter((suggestion) =>
        suggestion.toLowerCase().includes(value.toLowerCase())
    );

    const displaySuggestions = value.trim() ? filteredSuggestions : suggestions;

    const handleSelect = useCallback(
        (suggestion: string) => {
            if (onSelect) {
                onSelect(suggestion);
            } else {
                onChange(suggestion);
            }
            // Close suggestions after selection
            setShowSuggestions(false);
            setHighlightedIndex(-1);

            // Refocus the input after a brief delay to allow the selection to process
            setTimeout(() => {
                inputRef.current?.focus();
            }, 50);
        },
        [onChange, onSelect]
    );

    const handleKeyDown = useCallback(
        (e: React.KeyboardEvent<HTMLInputElement>) => {
            if (!showSuggestions || displaySuggestions.length === 0) return;

            if (e.key === 'ArrowDown') {
                e.preventDefault();
                setHighlightedIndex((prev) =>
                    prev < displaySuggestions.length - 1 ? prev + 1 : prev
                );
            } else if (e.key === 'ArrowUp') {
                e.preventDefault();
                setHighlightedIndex((prev) => (prev > 0 ? prev - 1 : -1));
            } else if (e.key === 'Enter' && highlightedIndex >= 0) {
                e.preventDefault();
                const suggestion = displaySuggestions[highlightedIndex];
                if (suggestion) handleSelect(suggestion);
            } else if (e.key === 'Escape') {
                setShowSuggestions(false);
                setHighlightedIndex(-1);
            }
        },
        [showSuggestions, displaySuggestions, highlightedIndex, handleSelect]
    );

    const handleInputClick = useCallback(() => {
        if (!disabled) {
            setShowSuggestions(true);
        }
    }, [disabled]);

    return (
        <div className="space-y-2">
            <Label htmlFor={id}>
                {label}
                {required && <span className="text-destructive ml-1">*</span>}
            </Label>
            <div className="relative">
                <Input
                    ref={inputRef}
                    id={id}
                    value={value}
                    onChange={(e) => onChange(e.target.value)}
                    onKeyDown={handleKeyDown}
                    onFocus={() => !disabled && setShowSuggestions(true)}
                    onClick={handleInputClick}
                    onBlur={() => setTimeout(() => setShowSuggestions(false), 200)}
                    placeholder={placeholder}
                    required={required}
                    disabled={disabled}
                    maxLength={maxLength}
                    autoComplete="off"
                />
                {!disabled && showSuggestions && displaySuggestions.length > 0 && (
                    <div className="absolute z-50 w-full mt-1 bg-popover border border-border rounded-md shadow-md max-h-60 overflow-y-auto">
                        {displaySuggestions.map((suggestion, index) => (
                            <button
                                key={suggestion}
                                type="button"
                                className={`w-full text-left px-3 py-2 text-sm hover:bg-accent hover:text-accent-foreground ${
                                    index === highlightedIndex ? 'bg-accent text-accent-foreground' : ''
                                }`}
                                onMouseDown={(e) => {
                                    e.preventDefault();
                                    handleSelect(suggestion);
                                }}
                            >
                                {suggestion}
                            </button>
                        ))}
                    </div>
                )}
                {!disabled && suggestions.length === 0 && value.trim() !== '' && (
                    <p className="mt-1 text-xs text-muted-foreground">
                        {noSuggestionsText}
                    </p>
                )}
            </div>
        </div>
    );
}
