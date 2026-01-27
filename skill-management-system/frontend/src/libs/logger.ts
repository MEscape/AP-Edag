import { configure, getConsoleSink, getJsonLinesFormatter, getLogger } from '@logtape/logtape';

// Configure Logtape to log to console only
await configure({
  sinks: {
    console: getConsoleSink({ formatter: getJsonLinesFormatter() }),
  },
  loggers: [
    {
      category: ['logtape', 'meta'],
      sinks: ['console'],
      lowestLevel: 'warning', // only log warnings and above
    },
    {
      category: ['app'],
      sinks: ['console'],
      lowestLevel: 'debug', // log debug and above
    },
  ],
});

// Export logger for use in the app
export const logger = getLogger(['app']);
