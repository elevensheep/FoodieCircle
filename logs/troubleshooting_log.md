# Troubleshooting Log

## 2026-02-17
### 1. `docker-compose` Command Not Found
- **Issue**: `./dev.sh` 실행 시 `docker-compose: command not found` 에러 발생.
- **Cause**: 최신 Docker Desktop/CLI 환경에서는 `docker-compose` (Standalone) 대신 `docker compose` (Plugin) 명령어를 사용함.
- **Solution**: `dev.sh` 스크립트 내 명령어를 `docker compose`로 수정함.

### 2. `bitnami/kafka:3.6` Image Pull Failed
- **Issue**: `docker compose up` 실행 시 `bitnami/kafka:3.6` 이미지를 찾을 수 없다는 에러 발생 (`manifest unknown`).
- **Cause**: 해당 태그가 Docker Hub에서 제거되었거나 `bitnami` 저장소 정책 변경으로 인해 접근 불가.
- **Solution**:
    - 1차 시도: `apache/kafka:3.7.0` (공식 이미지)로 교체했으나, ZooKeeper 모드 설정 충돌(KRaft formatting error)로 실행 실패.
    - 최종 해결: ZooKeeper 환경에서 안정적인 `confluentinc/cp-kafka:7.6.1` 및 `confluentinc/cp-zookeeper:7.6.1` 이미지로 교체하여 정상 실행 확인.

### 3. Docker Compose `version` Warning
- **Issue**: `docker compose up` 실행 시 `WARN[...] the attribute version is obsolete` 경고 발생.
- **Cause**: Docker Compose v2부터는 `docker-compose.yml` 파일의 최상단 `version` 속성이 더 이상 필요하지 않음(Obsolete).
- **Solution**: `docker-compose.yml`에서 `version: '3.8'` 라인을 제거함.

### 4. API Documentation (Swagger) Not Accessible
- **Issue**: `docker compose up` 실행 후 `GRAVITY.md`에 명시된 Swagger URL(`localhost:8081` 등) 접속 불가.
- **Cause**: `docker compose up`은 DB, Kafka 등 **인프라**만 실행하며, 실제 Spring Boot 애플리케이션은 실행하지 않음.
- **Solution**:
    - 인프라 실행: `./dev.sh up`
    - 애플리케이션 실행: `./dev.sh run <service-name>` (예: `./dev.sh run user-service`)
    - 각 서비스를 실행해야 해당 포트(8081 등)가 열리고 Swagger UI에 접근 가능함.

### 5. Application Startup Failure (DB Connection)
- **Issue**: `user-service` 실행 시 `HikariPool-1 - Exception during pool initialization` 또는 `database "foodiecircle" does not exist` 에러 발생.
- **Cause**:
    1.  **DB 이름 불일치**: `docker-compose.yml`은 `foodicircle`로 설정되어 있었으나, `application.properties`는 `foodiecircle`(오타)로 설정됨.
    2.  **Schema 누락**: Hibernate가 `user_schema` 등을 찾지 못함.
- **Solution**:
    1.  모든 `application.properties`의 DB명을 `foodicircle`로 수정.
    2.  `init.sql` 스크립트를 생성하여 `user_schema`, `map_schema`, `feed_schema`, `schedule_schema`를 자동 생성하도록 `docker-compose.yml`에 볼륨 마운트 추가.
    3.  DB 재설정을 위해 볼륨 초기화 후 재실행 (`docker compose down -v` -> `./dev.sh up`).
