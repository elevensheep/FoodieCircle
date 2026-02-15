# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

FoodiCircle is a Spring Boot 4.0.2 web application using Java 17 and Gradle 9.3.0. The project is in early development (skeleton stage).

## Build & Run Commands

```bash
./gradlew build        # Build the project
./gradlew bootRun      # Run the application
./gradlew test         # Run all tests
./gradlew clean        # Clean build artifacts
```

To run a single test class:
```bash
./gradlew test --tests "com.example.FoodiCircle.SomeTestClass"
```

## Architecture

- **Framework**: Spring Boot with `spring-boot-starter-webmvc`
- **Package root**: `com.example.FoodiCircle`
- **Entry point**: `DemoApplication.java` (`@SpringBootApplication`)
- **Config**: `src/main/resources/application.properties`
- **Testing**: JUnit 5 via `spring-boot-starter-webmvc-test`
