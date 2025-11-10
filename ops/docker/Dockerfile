FROM openjdk:17-jre-slim

WORKDIR /app

# JAR 파일 복사 (ops/docker에서 실행되므로 상대경로 조정)
COPY ../../adapter/api/build/libs/tokenizer-api-1.1.0.jar app.jar

# 포트 노출
EXPOSE 8080

# 서버 실행
CMD ["java", "-jar", "app.jar"]
