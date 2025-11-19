FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# API JAR 복사 (빌드 컨텍스트는 프로젝트 루트)
COPY adapter/api/build/libs/tokenizer-api-1.1.0.jar app.jar

# 포트 노출
EXPOSE 8080

# 서버 실행
CMD ["java", "-jar", "app.jar"]
