#!/bin/bash

KOTLINC="./kotlinc/bin/kotlinc"
KOTLIN="./kotlinc/bin/kotlin"

echo "Kotlin 컴파일 중..."
$KOTLINC src/Main.kt -include-runtime -d Main.jar

if [ $? -eq 0 ]; then
    echo "컴파일 완료!"
    echo "실행 중..."
    echo ""
    java -jar Main.jar "$@"
else
    echo "컴파일 실패"
    exit 1
fi
