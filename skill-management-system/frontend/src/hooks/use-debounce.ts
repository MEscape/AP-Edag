import { useEffect, useState } from 'react';

/**
 * Custom hook for debouncing a value.
 * Delays updating the returned value until a specified amount of time has passed
 * since the last change.
 *
 * @template T - Type of the input value
 * @param value - The value to debounce
 * @param delay - Delay in milliseconds before updating the debounced value (default: 500ms)
 * @returns The debounced value
 */
export function useDebounce<T>(value: T, delay: number = 500): T {
  const [debouncedValue, setDebouncedValue] = useState<T>(value);

  useEffect(() => {
    // Set a timeout to update the value after the delay
    const handler = setTimeout(() => {
      setDebouncedValue(value);
    }, delay);

    // Cleanup: cancel the timeout if the value or delay changes
    return () => {
      clearTimeout(handler);
    };
  }, [value, delay]);

  return debouncedValue;
}
