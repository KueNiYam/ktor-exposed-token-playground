FROM openjdk:17-jre-slim

WORKDIR /app

# JAR 파일 복사
COPY build/libs/funny-1.1.0.jar app.jar

# 포트 노출
EXPOSE 8080

# 서버 실행
CMD ["java", "-cp", "app.jar", "ServerMainKt"]
