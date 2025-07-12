FROM amazoncorretto:17-alpine3.21 as builder

WORKDIR /app

COPY gradle /app/gradle
COPY gradlew gradlew.bat settings.gradle build.gradle /app/

RUN chmod +x gradlew

RUN ./gradlew dependencies --no-daemon

COPY src /app/src

# Build the application
RUN ./gradlew bootJar --no-daemon -x test

FROM eclipse-temurin:17-jre-ubi9-minimal

# 작업 디렉토리 생성
WORKDIR /app

# festival 루트의 모든 파일을 컨테이너로 복사
COPY --from=builder /app/build/libs/app.jar app.jar

EXPOSE 8080

# (예시) 실행
CMD ["java", "-jar", "app.jar"]