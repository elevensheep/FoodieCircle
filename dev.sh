#!/bin/bash

# dev.sh - Development Helper Script

function show_help {
    echo "Usage: ./dev.sh [command]"
    echo "Commands:"
    echo "  up           Start infrastructure (Docker)"
    echo "  down         Stop infrastructure (Docker)"
    echo "  build        Build all services (Gradle)"
    echo "  run <service> Run a specific service (e.g., ./dev.sh run user-service)"
    echo "  clean        Clean build artifacts"
}

if [ "$1" == "up" ]; then
    echo "Starting infrastructure..."
    docker compose up -d
elif [ "$1" == "down" ]; then
    echo "Stopping infrastructure..."
    docker compose down
elif [ "$1" == "build" ]; then
    echo "Building project..."
    ./gradlew clean build -x test
elif [ "$1" == "run" ]; then
    if [ -z "$2" ]; then
        echo "Please specify a service to run."
        exit 1
    fi
    echo "Running $2..."
    ./gradlew :$2:bootRun
elif [ "$1" == "clean" ]; then
    ./gradlew clean
else
    show_help
fi
