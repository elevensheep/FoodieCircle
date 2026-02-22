FROM node:20-alpine AS builder

WORKDIR /app

# Copy root configurations
COPY package.json package-lock.json tsconfig.json nest-cli.json ./

# Copy all source code for the monorepo context
COPY libs/ ./libs/
COPY apps/ ./apps/

# Install all dependencies and build feed-service
RUN npm install
RUN npm run build feed-service

FROM node:20-alpine

WORKDIR /app

# Copy only production files
COPY --from=builder /app/package.json ./
# In a pure production image, we would do npm i --omit=dev or prune
COPY --from=builder /app/node_modules ./node_modules
COPY --from=builder /app/dist/apps/feed-service ./dist

EXPOSE 8083

CMD ["node", "dist/main.js"]
