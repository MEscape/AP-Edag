import { withAuth } from 'next-auth/middleware';
import createIntlMiddleware from 'next-intl/middleware';
import { routing } from '@/libs/i18nRouting';

const intlMiddleware = createIntlMiddleware(routing);

export default withAuth(
    (req) => {
      return intlMiddleware(req);
    },
    {
      callbacks: {
        authorized: ({ token, req }) => {
          const { pathname } = req.nextUrl;

          // Public routes
          const publicRoutes = ['/', '/de', '/en'];

          const isPublicRoute = publicRoutes.some(
              (route) => pathname === route
          );

          // Allow public pages without authentication
          if (isPublicRoute) return true;

          // Block if no token (user not authenticated)
          if (!token) {
            console.log('No token found - redirecting to login');
            return false;
          }

          // Get user roles array
          const userRoles = token.roles as string[] | undefined;

          // If roles are missing, deny access to protected routes
          if (!userRoles || userRoles.length === 0) {
            return false;
          }

          // Check for manager-only routes
          const managerRoutes = ['/dashboard/discover/projects'];
          const isManagerRoute = managerRoutes.some(
              (route) => pathname.startsWith(route)
          );

          if (isManagerRoute) {
            // Check if user has 'manager' OR 'admin' role
            const hasAccess = userRoles.includes('manager');
            if (!hasAccess) {
              return false;
            }
          }

          // Check for admin-only routes
          const adminRoutes = ['/dashboard/admin'];
          const isAdminRoute = adminRoutes.some(
              (route) => pathname.startsWith(route)
          );

          if (isAdminRoute) {
            // Check if user has 'admin' role
            if (!userRoles.includes('admin')) {
              return false;
            }
          }

          // All other authenticated routes are allowed
          return true;
        },
      },
    }
);

export const config = {
  matcher: [
    '/((?!api|_next|_vercel|.*\\..*).*)',
    '/api/(.*)',
  ],
};
