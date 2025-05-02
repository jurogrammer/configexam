#!/bin/bash

set -e  # 에러 발생 시 스크립트 즉시 종료

echo "[🚀] Running Gradle build (parallel)..."
./gradlew clean build --parallel

echo "[🐳] Starting Docker Compose (with build)..."
docker-compose up --build
