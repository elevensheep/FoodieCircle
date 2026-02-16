# GRAVITY.md

**Antigravity** 에이전트를 위한 프로젝트 가이드 문서입니다.

## 🚀 Project Context
- **Project Name**: FoodiCircle
- **Tech Stack**: Spring Boot 3.2.5, Java 17, Gradle 8.8, PostgreSQL (PostGIS), Kafka
- **Infrastructure**: Docker Compose

## 📝 Documentation Rules (Must Follow)
이 프로젝트에서 작업할 때는 다음 규칙을 엄격히 준수해야 합니다.

1.  **Language**: 모든 문서와 커밋 메시지는 **한글(Korean)**로 작성합니다.
2.  **Artifacts Management**:
    -   **Reviews**: 코드/아키텍처 리뷰 시 `reviews/` 폴더에 작성합니다.
    -   **Logs**: 오류 발생 시 `logs/troubleshooting_log.md`에 기록합니다.
    -   **Walkthrough**: 작업 완료 후 `walkthrough.md`에 검증 절차를 업데이트합니다.

## 🛠️ Operational Guidelines
- **Build Verification**: 변경 사항 적용 후 반드시 `./gradlew clean build`로 검증합니다.
- **Docker**: 인프라 변경 시 `docker-compose up -d`로 반영 확인합니다.

---
> 이 문서는 Antigravity 에이전트가 프로젝트를 이해하고 일관된 작업을 수행하기 위한 기준점입니다.
