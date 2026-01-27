import { dirname } from 'node:path';
import { fileURLToPath } from 'node:url';
import antfu from '@antfu/eslint-config';
import jsxA11y from 'eslint-plugin-jsx-a11y';
import tailwind from 'eslint-plugin-tailwindcss';

export default antfu(
  {
    react: true,
    nextjs: true,
    typescript: true,

    // Configuration preferences
    lessOpinionated: true,
    isInEditor: false,

    // Code style (ESLint Stylistic replaces Prettier)
    stylistic: {
      semi: true,
      indent: 2, // 2 spaces (Antfu default)
      quotes: 'single', // Single quotes (Antfu default)
      jsx: true, // Enable JSX formatting
    },

    // Format settings
    formatters: {
      css: true,
      html: true,
      markdown: true,
    },

    // Ignored paths
    ignores: [
      '**/dist/**',
      '**/build/**',
      '**/.next/**',
      '**/node_modules/**',
      '**/coverage/**',
    ],
  },
  // --- Accessibility Rules ---
  jsxA11y.flatConfigs.recommended,

  // --- Tailwind CSS Rules ---
  ...tailwind.configs['flat/recommended'],
  {
    settings: {
      tailwindcss: {
        // Point to your tailwind config, not CSS file
        config: `${dirname(fileURLToPath(import.meta.url))}/tailwind.config.ts`,
        callees: ['cn', 'cva', 'clsx'], // Common utility functions
      },
    },
  },

  // --- Custom Rule Overrides ---
  {
    rules: {
      // General
      'antfu/no-top-level-await': 'off',
      'node/prefer-global/process': 'off',

      // TypeScript
      'ts/consistent-type-definitions': ['error', 'type'],
      'ts/no-explicit-any': 'warn', // More lenient during development

      // React
      'react/prefer-destructuring-assignment': 'off',
      'react-hooks/exhaustive-deps': 'warn',

      // Style (ESLint Stylistic)
      'style/brace-style': ['error', '1tbs'],
      'style/arrow-parens': ['error', 'always'], // Consistent with Prettier
      'style/comma-dangle': ['error', 'always-multiline'],
      'style/max-len': ['warn', { code: 120, ignoreUrls: true, ignoreStrings: true }],

      // Tailwind
      'tailwindcss/no-custom-classname': 'warn',
      'tailwindcss/classnames-order': 'warn', // Auto-sort Tailwind classes

      // Accessibility
      'jsx-a11y/alt-text': 'error',
      'jsx-a11y/anchor-is-valid': 'warn', // Next.js Link handling
    },
  },
);
