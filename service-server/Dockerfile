FROM openjdk:21-jdk-slim

# 빌드된 jar 파일 복사 (Gradle 빌드 결과물이 여기에 있어야 함)
ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} app.jar

# 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]
