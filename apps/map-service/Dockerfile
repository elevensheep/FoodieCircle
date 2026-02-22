FROM node:20-alpine AS builder

WORKDIR /app

COPY package*.json ./
COPY nest-cli.json ./
COPY tsconfig.json ./
COPY tsconfig.build.json ./
COPY apps/map-service ./apps/map-service
COPY libs ./libs

RUN npm ci
RUN npm run build:map-service

FROM node:20-alpine

WORKDIR /app

COPY --from=builder /app/dist ./dist
COPY --from=builder /app/node_modules ./node_modules
COPY --from=builder /app/package.json ./

EXPOSE 8082

CMD ["node", "dist/apps/map-service/main"]
