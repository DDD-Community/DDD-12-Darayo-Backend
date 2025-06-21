FROM eclipse-temurin:17-alpine-3.21

# 작업 디렉토리 생성
WORKDIR /app

# festival 루트의 모든 파일을 컨테이너로 복사
COPY . .

# gradle 빌드
RUN ./gradlew build

EXPOSE 8080

# (예시) 실행
CMD ["java", "-jar", "/app/build/libs/app.jar"]