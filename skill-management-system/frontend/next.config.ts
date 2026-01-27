import type { NextConfig } from 'next';
import createNextIntlPlugin from 'next-intl/plugin';
import './src/libs/env';

// Define the base Next.js configuration
const baseConfig: NextConfig = {
  devIndicators: {
    position: 'bottom-right',
  },
  poweredByHeader: false,
  reactStrictMode: true,
  reactCompiler: true,
  experimental: {
    turbopackFileSystemCacheForDev: true
  }
};

// Initialize the Next-Intl plugin
let configWithPlugins = createNextIntlPlugin('./src/libs/i18n.ts')(baseConfig);

// Conditionally enable bundle analysis
if (process.env.ANALYZE === 'true') {
  const withBundleAnalyzer = require('@next/bundle-analyzer');
  configWithPlugins = withBundleAnalyzer()(configWithPlugins);
}

const nextConfig = configWithPlugins;
export default nextConfig;
