#!/bin/bash

# 토큰화 프로그램 실행 스크립트

echo "빌드 중..."
./gradlew build --quiet

if [ $? -eq 0 ]; then
    echo "실행 중..."
    echo ""
    java -jar build/libs/funny-1.0.jar "$@"
else
    echo "빌드 실패"
    exit 1
fi
