import {useTranslations} from "next-intl";

const SKILL_LEVEL_THRESHOLDS = {
    expert: 80,
    advanced: 60,
    intermediate: 40,
    basic: 20,
} as const;

export const getSkillLevelLabel = (
    score: number,
    t: ReturnType<typeof useTranslations<'profile'>>
): string => {
    if (score >= SKILL_LEVEL_THRESHOLDS.expert) return t('skillLevel.expert');
    if (score >= SKILL_LEVEL_THRESHOLDS.advanced) return t('skillLevel.advanced');
    if (score >= SKILL_LEVEL_THRESHOLDS.intermediate) return t('skillLevel.intermediate');
    if (score >= SKILL_LEVEL_THRESHOLDS.basic) return t('skillLevel.basic');
    return t('skillLevel.beginner');
};