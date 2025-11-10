#!/bin/bash

# CLI 모듈 빌드 및 실행 스크립트

echo "빌드 중..."
cd "$(dirname "$0")/.."
./gradlew :cli:build > /dev/null 2>&1

if [ $? -ne 0 ]; then
    echo "빌드 실패"
    exit 1
fi

echo "실행 중..."
java -jar cli/build/libs/tokenizer-cli-1.1.0.jar "$@"
