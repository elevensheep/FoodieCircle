# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

FoodieCircle is a closed-loop restaurant sharing map service leveraging the KakaoTalk social network. Instead of reviews from strangers, it visualizes trusted restaurant information verified by real acquaintances (friends, family, colleagues) on a map shared within groups (Circles). Users create markers based on Kakao Maps, and these markers explicitly show 'who recommended it' to enhance the reliability of the information.

> **IMPORTANT**: This project is connected to Notion via MCP. Always refer to Notion for core business logic and detailed requirements using the available MCP tools.


## Key Features

1.  **Kakao Account Integration**
    -   Login and invite friends via KakaoTalk account without a separate signup process.
2.  **Group (Circle) Management**
    -   Create and manage private groups for specific purposes (e.g., 'Work Lunch', 'Reunion', 'Couple Map').
3.  **Custom Marker Sharing**
    -   Retrieve accurate location information via Kakao Local API.
    -   Display custom icons with the author's profile picture when markers are registered on the map.
4.  **Feed & List View**
    -   View friends' latest restaurant entries in a timeline format in addition to the map view.
5.  **Schedule Management**
    -   Create dining schedules for specific days and share them with group members.

## Development Environment

### Frontend (Android App)
-   **Language:** Kotlin (100% Native)
-   **Target OS:** Android (Min SDK: 26, Target SDK: 34)
-   **Architecture:** MVVM (Model-View-ViewModel) + Clean Architecture
-   **Major Libraries:**
    -   **Maps:** Kakao Maps Android SDK v2
    -   **Network:** Retrofit2, OkHttp3
    -   **Image Loading:** Glide or Coil
    -   **DI:** Hilt (Dependency Injection)
    -   **Login:** Kakao User SDK (OAuth 2.0)

### Backend (API Server)
-   **Framework:** NestJS
-   **Language:** TypeScript
-   **Build Tool:** npm (or pnpm/yarn)
-   **Architecture:** Microservices Architecture (MSA)
-   **Security:** Passport + JWT (JSON Web Token)
-   **Major Libraries:**
    -   @nestjs/platform-express
    -   TypeORM
    -   class-validator & class-transformer

### Database
-   **RDBMS:** PostgreSQL 16
-   **Spatial Extension:** PostGIS (Location-based query optimization and distance calculation)
-   **ORM:** TypeORM

### Message Queue
-   **System:** Apache Kafka 3.6
-   **Use Cases:**
    -   Asynchronous event processing (e.g., Feed updates)
    -   Notification dispatch (Push notifications)
    -   Log aggregation & analytics data collection
-   **Client:** @nestjs/microservices (Kafka)

### Infrastructure (Self-Hosted)
-   **Server OS:** Ubuntu 24.04
-   **Web Server:** Nginx
-   **Deployment:** Docker & Docker Compose
-   **CI/CD:** GitHub Actions

## Build & Run Commands (Backend)

```bash
npm run build      # Build the project
npm run start:dev  # Run the application in watch mode
npm run test       # Run all tests
npm run prebuild   # Clean build artifacts
```

To run a single test file:
```bash
npm run test -- some.service.spec.ts
```

## Architecture Details

### Monorepo Structure (NestJS Workspaces / npm workspaces)

```
FoodiCircle/
├── libs/common/             # Shared library (ApiResponse, DTOs, Events)
├── apps/user-service/       # :8081 - User identity, Kakao Login, Auth
├── apps/map-service/        # :8082 - Circles, Markers, Location Logic
├── apps/feed-service/       # :8083 - Friend activities, Timeline
├── package.json             # Root workspace config
├── nest-cli.json            # NestJS CLI/monorepo config
└── docker-compose.yml
```

### Per-Service Package Structure

Each service follows the standard NestJS module structure under `src/`:
-   `*.controller.ts`: REST API endpoints (`/api/{service}/...`)
-   `*.service.ts`: Business logic
-   `*.repository.ts`: Data access (TypeORM)
-   `*.entity.ts`: Database entities
-   `dto/`: Service-specific DTOs
-   `event/`: Kafka producers/consumers

### API Conventions

-   All endpoints are prefixed with `/api`
-   Responses use `ApiResponse<T>` wrapper with `status`, `message`, and `data` fields
-   Use `ApiResponse.success(message)` or `ApiResponse.success(message, data)` factory methods
-   Controllers use constructor injection (no field injection)

## Docker Container Design

### Container Scope Principle

-   **Microservices Architecture (MSA)**: Each business domain runs as a separate containerized service.
-   **Kafka as Backbone**: Services do not call each other directly (minimize HTTP/Feign); they allow **asynchronous communication via Kafka**.
-   **Structure**:
    -   `user-service`: User & Auth
    -   `map-service`: Markers & Circles
    -   `feed-service`: Feeds & Timeline

### Container Composition

| Container | Image | Role |
|---|---|---|
| `user-service` | Custom | User identity, Kakao Login, Auth |
| `map-service` | Custom | Circles, Markers, Location Logic |
| `feed-service` | Custom | Friend activities, Timeline generation |
| `db` | `postgis/postgis:16-3.4` | Shared Database (Schema per service recommended) |
| `kafka` | `bitnami/kafka:3.6` | **Event Bus** (Inter-service communication) |
| `nginx` | `nginx:alpine` | API Gateway (routes /api/user, /api/map, etc.) |

### Key Rules

-   **Event Driven**: State changes in one service (e.g., "New Marker Created") must be published to Kafka. Other services (e.g., Feed) subscribe to react.
-   **Loose Coupling**: Services should not share DB tables directly.
-   **API Gateway**: All external traffic goes through Nginx (Gateway), which routes to specific services.
-   `depends_on`: All services must wait for `db` and `kafka` to be healthy before starting.

## Git Convention

-   **feat**: Add a new feature
-   **fix**: Fix a bug
-   **docs**: Documentation changes
-   **style**: Code style changes (formatting, missing semi-colons, etc.)
-   **refactor**: Code refactoring
-   **test**: Add or modify tests
-   **chore**: Configuration file changes (build system, package manager, etc.)
-   **perf**: Improve performance
-   **build**: Build-related settings changes
-   **remove**: Remove files
-   **rename**: Rename files

## MCP Tools

-   **Notion**: Core business logic and detailed requirements
-   **GitHub**: Code management and version control

## Development Workflow

1.  **Understand Requirements (Notion)**
    -   Use Notion MCP to clarify requirements and business logic.
2.  **Red: Write a Failing Test**
    -   Create a test case that captures the requirement or bug reproduction.
    -   Run the test to confirm it fails (compilation error or assertion failure).
3.  **Green: Make it Pass**
    -   Write the minimum amount of code necessary to make the test pass.
    -   Do not focus on code quality at this stage, just pass the test.
4.  **Refactor: Improve Code Quality**
    -   Clean up the code, remove duplication, and improve design.
    -   Ensure all tests still pass after refactoring.
5.  **Documentation & Sync**
    -   Update Notion MCP/TODO cards with implementation details and status.

## Documentation Guidelines

-   **Project Review**: When reviewing code or architecture, create or update a review file in the `reviews/` directory.
-   **Troubleshooting Log**: Record all build errors, runtime exceptions, and their solutions in `logs/troubleshooting_log.md`.
-   **Walkthrough**: After implementing features or fixes, update `walkthrough.md` with verification steps and results.
-   **Language**: All documentation must be written in **Korean (Hangul)**.
