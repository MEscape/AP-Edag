'use client';

import React, { useState, useEffect, useRef, useCallback } from 'react';
import { Label } from '@/components/ui/label';
import { useTranslations } from 'next-intl';

export interface SkillLevelSliderProps {
    readonly score: number;
    readonly onScoreChange: (score: number) => void;
    readonly label: string;
}

const SKILL_LEVEL_THRESHOLDS = {
    beginner: 0,
    basic: 21,
    intermediate: 41,
    advanced: 61,
    expert: 81,
};

function getSkillLevelLabel(score: number, t: ReturnType<typeof useTranslations>) {
    if (score >= SKILL_LEVEL_THRESHOLDS.expert) return t('skillLevel.expert');
    if (score >= SKILL_LEVEL_THRESHOLDS.advanced) return t('skillLevel.advanced');
    if (score >= SKILL_LEVEL_THRESHOLDS.intermediate) return t('skillLevel.intermediate');
    if (score >= SKILL_LEVEL_THRESHOLDS.basic) return t('skillLevel.basic');
    return t('skillLevel.beginner');
}

export function SkillLevelSlider({
                                     score,
                                     onScoreChange,
                                     label,
                                 }: SkillLevelSliderProps) {

    const t = useTranslations("common");

    const [isDragging, setIsDragging] = useState(false);
    const sliderRef = useRef<HTMLDivElement>(null);

    const updateSliderValue = useCallback(
        (e: MouseEvent | React.MouseEvent) => {
            if (!sliderRef.current) return;

            const rect = sliderRef.current.getBoundingClientRect();
            const x = Math.max(0, Math.min(e.clientX - rect.left, rect.width));
            const percent = x / rect.width;
            const value = Math.round(percent * 100);

            onScoreChange(Math.min(100, Math.max(0, value)));
        },
        [onScoreChange]
    );

    const handleMouseDown = useCallback(
        (e: React.MouseEvent) => {
            setIsDragging(true);
            updateSliderValue(e);
        },
        [updateSliderValue]
    );

    const handleKeyDown = useCallback(
        (e: React.KeyboardEvent) => {
            if (e.key === 'ArrowRight' || e.key === 'ArrowUp') {
                e.preventDefault();
                onScoreChange(Math.min(100, score + 5));
            }
            if (e.key === 'ArrowLeft' || e.key === 'ArrowDown') {
                e.preventDefault();
                onScoreChange(Math.max(0, score - 5));
            }
        },
        [score, onScoreChange]
    );

    useEffect(() => {
        if (!isDragging) return;

        const handleMove = (e: MouseEvent) => updateSliderValue(e);
        const handleUp = () => setIsDragging(false);

        document.addEventListener('mousemove', handleMove);
        document.addEventListener('mouseup', handleUp);

        return () => {
            document.removeEventListener('mousemove', handleMove);
            document.removeEventListener('mouseup', handleUp);
        };
    }, [isDragging, updateSliderValue]);

    const translatedLevel = getSkillLevelLabel(score, t);

    return (
        <div className="space-y-2">
            <Label>{label}</Label>

            <div
                ref={sliderRef}
                className="relative h-2 bg-muted rounded-full cursor-pointer"
                onMouseDown={handleMouseDown}
                role="slider"
                aria-valuemin={0}
                aria-valuemax={100}
                aria-valuenow={score}
                aria-label={label}
                tabIndex={0}
                onKeyDown={handleKeyDown}
            >
                <div
                    className="absolute inset-y-0 left-0 bg-primary rounded-full"
                    style={{ width: `${score}%` }}
                />

                <div
                    className="absolute top-1/2 -translate-y-1/2 w-4 h-4 bg-background rounded-full border-2 border-primary shadow-sm hover:scale-110 transition-transform"
                    style={{ left: `calc(${score}% - 0.5rem)` }}
                />
            </div>

            <div className="flex items-center justify-between text-sm">
                <span className="text-muted-foreground">{translatedLevel}</span>
                <span className="font-medium">{score}/100</span>
            </div>
        </div>
    );
}
