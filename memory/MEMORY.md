# FoodiCircle Project Memory

## Project Overview
Closed-loop restaurant sharing map service using KakaoTalk social network. Backend is NestJS TypeScript (migrating from Spring Boot Java).

## Architecture
- **Target**: NestJS monorepo (CLAUDE.md spec)
- **Structure**: `apps/` for services, `libs/` for shared code
- **DB**: PostgreSQL 16 + PostGIS, schema-per-service pattern
- **Message Queue**: Kafka

## Conversion Status
- `apps/user-service/` — DONE: NestJS TypeScript (converted from Spring Boot)
- `apps/map-service/` — DONE: NestJS TypeScript (converted from Spring Boot)
- `user-service/` — OLD: Spring Boot Java (not yet removed)
- `map-service/` — OLD: Spring Boot Java (not yet removed)
- `feed-service/` — Still Spring Boot

## Key Files
- `package.json` — Root NestJS workspace (npm)
- `nest-cli.json` — NestJS monorepo config (user-service + common lib)
- `tsconfig.json` — Root TypeScript config
- `libs/common/src/dto/api-response.ts` — Shared ApiResponse wrapper
- `apps/user-service/src/main.ts` — NestJS entry point, port 8081
- `docker-compose.yml` — user-service now builds from `apps/user-service/Dockerfile`

## NestJS user-service Structure
```
apps/user-service/src/
  main.ts              # bootstrap, ValidationPipe, port 8081
  app.module.ts        # ConfigModule, TypeORM (user_schema), AuthModule, HealthModule
  auth/
    auth.controller.ts # POST /api/user/auth/login/kakao
    auth.service.ts    # kakaoLogin() business logic
    auth.module.ts
    dto/               # KakaoLoginRequestDto, KakaoLoginResponseDto
    kakao/             # KakaoApiClient, response interfaces
  health/
    health.controller.ts # GET /api/user/health
  jwt/
    jwt-token.provider.ts # generateAccessToken/RefreshToken, validateToken
  user/
    user.entity.ts     # TypeORM entity (users table, user_schema)
    user.repository.ts # findByKakaoId(), save()
    user.module.ts
```

## NestJS map-service Structure
```
apps/map-service/src/
  main.ts              # bootstrap, ValidationPipe (transform+implicit), port 8082
  app.module.ts        # ConfigModule, TypeORM (map_schema), all feature modules
  health/
    health.controller.ts # GET /api/map/health
  circle/
    circle.entity.ts, circle-member.entity.ts
    circle.repository.ts, circle-member.repository.ts
    circle.service.ts, circle.controller.ts, circle.module.ts
    dto/create-circle.dto.ts, circle-response.dto.ts
  restaurant/
    restaurant.entity.ts, restaurant.repository.ts
    kakao-local-api.client.ts  # axios to Kakao Local API, env: KAKAO_API_KEY
    restaurant-search.service.ts, restaurant-search.controller.ts
    restaurant.module.ts
    dto/kakao-place.dto.ts, restaurant-search-response.dto.ts
  review/
    review.entity.ts (ReviewVisibility enum), review-image.entity.ts
    review.repository.ts, review.service.ts, review.controller.ts
    review.module.ts
    dto/create-review.dto.ts, review-response.dto.ts
  file/
    file-storage.service.ts  # writes multer buffer to disk, env: FILE_UPLOAD_DIR
  event/
    marker-created.event.ts
    marker-event.publisher.ts  # kafkajs producer, topic: 'marker-created'
```

## API Endpoints (map-service)
- `GET /api/map/health`
- `POST /api/map/groups` — header: X-User-Id, body: {name}
- `GET /api/map/groups` — header: X-User-Id
- `GET /api/map/restaurants/search` — query: keyword, x, y, radius
- `POST /api/map/reviews` — multipart/form-data, header: X-User-Id, fields: externalId, restaurantName, address, category, x, y, content, rating, visibility, groupId; files: images[]
- `GET /api/map/reviews/feed` — query: groupId, page, size

## Key Architecture Notes (map-service)
- Circular dep between ReviewModule ↔ RestaurantModule resolved with forwardRef()
- CircleModule exports CircleMemberRepository (used by ReviewModule for getFeed)
- DB schema: map_schema
- tsconfig.app.json needs explicit paths override for @app/common (baseUrl is apps/map-service/)
- File uploads use multer memory storage → FileStorageService writes buffer to disk
- @types/multer added to devDependencies

## API Endpoints (user-service)
- `POST /api/user/auth/login/kakao` — body: `{authCode}`, returns JWT tokens
- `GET /api/user/health` — health check

## DB Schema (user_schema.users)
- id (SERIAL PK), kakao_id (BIGINT UNIQUE), nickname, email, profile_image_url
- uuid (UUID UNIQUE, auto-generated @BeforeInsert), created_at, updated_at

## JWT
- Algorithm: HS256, secret: Base64-encoded env var `JWT_SECRET`
- Access token expiry: 3600000ms (1h), Refresh: 604800000ms (7d)
- Subject: user UUID

## Testing
- All tests: `npm test` from root
- 28 tests, 12 suites — all passing
- Tests use Jest + @nestjs/testing, axios mocked with jest.mock('axios')

## Environment Variables (user-service)
DB_HOST, DB_USERNAME, DB_PASSWORD, DB_NAME, JWT_SECRET, JWT_ACCESS_TOKEN_EXPIRY,
JWT_REFRESH_TOKEN_EXPIRY, KAKAO_CLIENT_ID, KAKAO_REDIRECT_URI,
KAKAO_TOKEN_URI, KAKAO_USER_INFO_URI, KAFKA_BROKERS
