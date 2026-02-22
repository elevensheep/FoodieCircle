# GRAVITY.md

**Antigravity** 에이전트를 위한 프로젝트 가이드 문서입니다.

##  Project Structure
- **common**: 공통 DTO, 유틸리티, 설정 등을 포함하는 모듈
- **user-service**: 사용자 관리, 인증/인가 (Spring Security + JWT 추정)
- **map-service**: 지도 데이터 처리, 위치 기반 서비스 (PostGIS 사용 추정)
- **feed-service**: 피드 관리, 게시물 포스팅
- **group-service**: 그룹/커뮤니티 관리 (기존 schedule-service 대체 추정)

## 🛠️ Operational Guidelines
- **Helper Script**: `./dev.sh` 스크립트를 사용하여 프로젝트를 관리합니다.
    - `chmod +x dev.sh` (최초 1회 실행)
    - `./dev.sh up`: 인프라(DB, Kafka 등) 실행
    - `./dev.sh build`: 전체 프로젝트 빌드
    - `./dev.sh run <service-name>`: 특정 서비스 실행 (예: `./dev.sh run user-service`)
- **Manual Build**: `./gradlew clean build`
- **Infrastructure**: `docker-compose.yml` (PostgreSQL/PostGIS, Apache Kafka, Zookeeper)

## 📚 API Documentation (Swagger)
⚠️ **주의**: 아래 주소에 접속하려면 `./dev.sh up` (인프라) 실행 후, **반드시 각 서비스도 실행해야 합니다** (예: `./dev.sh run user-service`).

각 서비스의 API 문서는 다음 주소에서 확인할 수 있습니다.
- **User Service**: `http://localhost:8081/swagger-ui/index.html`
- **Map Service**: `http://localhost:8082/swagger-ui/index.html`
- **Feed Service**: `http://localhost:8083/swagger-ui/index.html`
- **Group Service**: `http://localhost:8084/swagger-ui/index.html`

## 🩺 Troubleshooting
주요 이슈와 해결 방법은 `logs/troubleshooting_log.md`에 기록되어 있습니다.
- **Docker Compose**: `docker-compose` 대신 `docker compose` 명령어를 사용해야 합니다 (`dev.sh`에 적용됨).
- **Kafka Image**: `bitnami/kafka` 대신 `apache/kafka` 공식 이미지를 사용합니다.

## 📝 Rules & Conventions
1. **Language**: 모든 문서는 **한글**로 작성합니다.
2. **Commit Message**: `CLAUDE.md`의 규칙을 따릅니다.
3. **Artifacts**: 중요 변경 사항은 `reviews/`에 기록하고, 작업 완료 후 `walkthrough.md`를 업데이트합니다.

---
> 이 문서는 Antigravity 에이전트가 프로젝트를 이해하고 일관된 작업을 수행하기 위한 기준점입니다.
