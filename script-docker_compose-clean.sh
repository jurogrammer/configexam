#!/bin/bash

set -e  # 에러 발생 시 스크립트 즉시 종료

echo "[🚀] Cleaning Gradle build (parallel)..."
./gradlew clean --parallel

echo "[🐳] Stopping and removing Docker containers, networks, and volumes..."
docker-compose down -v
