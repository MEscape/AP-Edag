import { fetchBaseQuery } from "@reduxjs/toolkit/query";
import type { BaseQueryFn, FetchArgs, FetchBaseQueryError } from "@reduxjs/toolkit/query";
import { getBaseUrl } from "@/utils/helpers";
import { getSession, signOut } from "next-auth/react";

const baseQuery = fetchBaseQuery({
  baseUrl: getBaseUrl(),
  prepareHeaders: async (headers) => {
    // Get locale from document lang attribute or browser language
    const locale = document.documentElement.lang || navigator.language || 'en';
    headers.set('Accept-Language', locale);

    // Get NextAuth session (access token)
    const session = await getSession();

    if (session?.accessToken) {
      headers.set('Authorization', `Bearer ${session.accessToken}`);
    }

    return headers;
  },
});

export const baseQueryWithLocale: BaseQueryFn<
    string | FetchArgs,
    unknown,
    FetchBaseQueryError
> = async (args, api, extraOptions) => {
  let result = await baseQuery(args, api, extraOptions);

  // Check if we got a 401 Unauthorized response (token revoked or invalid)
  if (result.error?.status === 401) {
    // Log out the user and redirect to home
    await signOut({ redirect: true, callbackUrl: '/' });
  }

  return result;
};
