#!/bin/bash

echo "🚀 토큰화 API 서버 배포 스크립트"
echo "================================"

# 빌드
echo "📦 JAR 빌드 중..."
./gradlew build

if [ $? -ne 0 ]; then
    echo "❌ 빌드 실패"
    exit 1
fi

# 배포 방식 선택
echo ""
echo "배포 방식을 선택하세요:"
echo "1) 로컬 실행"
echo "2) Docker 빌드 & 실행"
echo "3) Docker Compose 실행"
read -p "선택 (1-3): " choice

case $choice in
    1)
        echo "🖥️  로컬에서 서버 실행 중..."
        java -cp build/libs/funny-1.1.0.jar ServerMainKt
        ;;
    2)
        echo "🐳 Docker 이미지 빌드 중..."
        docker build -t tokenizer-api .
        echo "🚀 Docker 컨테이너 실행 중..."
        docker run -p 8080:8080 tokenizer-api
        ;;
    3)
        echo "🐳 Docker Compose 실행 중..."
        docker-compose up --build
        ;;
    *)
        echo "❌ 잘못된 선택입니다."
        exit 1
        ;;
esac
