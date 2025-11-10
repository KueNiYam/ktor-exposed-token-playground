# 텍스트 토큰화 프로그램

한국어와 영어 텍스트를 14가지 방법으로 토큰화하는 Kotlin 프로그램입니다.

## 주요 기능

- 14가지 토큰화 방법 지원
- JSON 및 텍스트 출력 형식
- 색상 코딩된 터미널 출력
- 실행시간 측정 및 메타데이터 제공
- 명령어 기반 인터페이스

## 사용법

```bash
# 텍스트 토큰화
./tokenize.sh tokenize "안녕하세요 Hello!"

# JSON 출력
./tokenize.sh tokenize json "안녕하세요 Hello!"

# 토큰화 방법 목록
./tokenize.sh list

# 도움말
./tokenize.sh help
```

## 개발

```bash
# 테스트 실행
./gradlew test

# 빌드
./gradlew build
```

## 문서

- [📋 TODO List](docs/TODO.md) - 계획된 작업 목록
- [📝 패치노트](docs/CHANGELOG.md) - 버전별 변경사항

## 라이선스

MIT License
