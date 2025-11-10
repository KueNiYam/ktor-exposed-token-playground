# 텍스트 토큰화 프로그램

한국어와 영어 텍스트를 14가지 방법으로 토큰화하는 Kotlin 프로그램입니다.

## 주요 기능

- 14가지 토큰화 방법 지원
- **CLI 인터페이스**: 명령줄에서 직접 사용
- **REST API**: HTTP 서버로 웹 서비스 제공
- JSON 및 텍스트 출력 형식
- 색상 코딩된 터미널 출력
- 실행시간 측정 및 메타데이터 제공

## 사용법

### CLI 사용법
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

### REST API 사용법
```bash
# API 서버 실행
./deploy-api.sh

# 또는 직접 실행
./gradlew run -PmainClass=ServerMainKt
```

**API 엔드포인트:**
```bash
# 헬스체크
curl http://localhost:8080/api/health

# 토큰화 방법 목록
curl http://localhost:8080/api/methods

# 토큰화 실행
curl -X POST http://localhost:8080/api/tokenize \
  -H "Content-Type: application/json" \
  -d '{"text": "안녕하세요!", "methods": [1, 14]}'
```

## 배포

### 로컬 개발
```bash
# CLI
./tokenize.sh tokenize "텍스트"

# API 서버
./deploy-api.sh
```

### Docker 배포
```bash
# Docker 빌드 & 실행
docker build -t tokenizer-api .
docker run -p 8080:8080 tokenizer-api

# Docker Compose
docker-compose up --build
```

## 개발

```bash
# 테스트 실행
./gradlew test

# 빌드
./gradlew build
```

## 문서

- [🚀 배포 계획](docs/AI_PLAN.md) - 배포 프로세스 및 컨테이너화
- [📝 패치노트](docs/CHANGELOG.md) - 버전별 변경사항

## 라이선스

MIT License
