## Frontend production Dockerfile (multi-stage)
## Builds Next.js app and serves with `next start`.

FROM node:25-alpine AS builder
WORKDIR /app

# Install dependencies
COPY package*.json ./
RUN npm ci

# Copy source and build
COPY . .
ENV NEXT_TELEMETRY_DISABLED=1
ENV SKIP_ENV_VALIDATION=true
ENV NEXT_PUBLIC_API_URL=http://api.skill-management.edag.com
ENV NEXT_PUBLIC_APP_URL=http://skill-management.edag.com
ENV NODE_ENV=production

RUN npm run build


FROM node:25-alpine AS runner
WORKDIR /app
ENV NODE_ENV=production
ENV NEXT_TELEMETRY_DISABLED=1

# Install only production deps
COPY package*.json ./
RUN npm ci --omit=dev

# Copy build output and static assets
COPY --from=builder /app/.next ./.next
COPY --from=builder /app/public ./public

EXPOSE 3000
CMD ["npm","run","start"]
