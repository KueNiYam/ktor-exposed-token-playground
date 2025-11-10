# 멀티모듈 아키텍처 계획

## 📋 현재 상태

### 완료된 작업 ✅
- **헥사고날 아키텍처**: Domain, Application, Adapters, Infrastructure
- **CLI 인터페이스**: 14가지 토큰화 방법, JSON/텍스트 출력
- **REST API**: 기본 엔드포인트 (`/api/health`, `/api/methods`, `/api/tokenize`)
- **단일 JAR**: 모든 기능이 하나의 JAR에 포함

### 현재 문제점 ❌
- **의존성 혼재**: CLI와 API 코드가 같은 JAR에 포함
- **배포 복잡성**: CLI용과 API용 분리 배포 불가
- **포트 충돌**: 동시 실행 시 충돌 발생
- **크기 비효율**: CLI만 사용해도 Ktor 의존성 포함

## 🏗️ 멀티모듈 아키텍처 설계

### 모듈 구조
```
funny/
├── settings.gradle.kts          # 멀티모듈 설정
├── build.gradle.kts            # 루트 공통 설정
├── core/                       # 🎯 핵심 비즈니스 로직
│   ├── build.gradle.kts        #   - 의존성 최소화
│   └── src/main/kotlin/        #   - 도메인 + 유스케이스
│       ├── domain/
│       ├── application/
│       └── infrastructure/
├── cli/                        # 🖥️ CLI 전용 모듈
│   ├── build.gradle.kts        #   - core 모듈 의존
│   ├── tokenize.sh            #   - 실행 스크립트
│   └── src/main/kotlin/
│       └── CliMain.kt
└── api/                        # 🌐 API 서버 전용 모듈
    ├── build.gradle.kts        #   - core + Ktor 의존
    ├── Dockerfile             #   - 컨테이너화
    ├── deploy-api.sh          #   - 배포 스크립트
    └── src/main/kotlin/
        └── ServerMain.kt
```

### 의존성 관계
```
cli ──→ core ←── api
       ↑
   domain + usecases
```

## 🎯 모듈별 역할

### Core 모듈
- **Domain**: Tokenizer, Token, TokenizedText, TokenizerMeta
- **Application**: TokenizeUseCase, ListMethodsUseCase
- **Infrastructure**: TokenizerRegistry
- **의존성**: Kotlin stdlib만 (최소화)

### CLI 모듈
- **Adapter**: CliAdapter
- **Entry Point**: CliMain.kt
- **의존성**: core 모듈
- **배포**: `cli/build/libs/cli.jar`

### API 모듈
- **Adapter**: WebAdapter
- **Entry Point**: ServerMain.kt
- **의존성**: core 모듈 + Ktor
- **배포**: `api/build/libs/api.jar` + Docker

## 🚀 배포 전략

### 1. CLI 배포
```bash
# CLI 전용 빌드
./gradlew :cli:build

# 실행
java -jar cli/build/libs/cli.jar tokenize "텍스트"
# 또는
./cli/tokenize.sh tokenize "텍스트"
```

### 2. API 배포
```bash
# API 전용 빌드
./gradlew :api:build

# 로컬 실행
java -jar api/build/libs/api.jar

# Docker 배포
cd api && docker build -t tokenizer-api .
docker run -p 8080:8080 tokenizer-api
```

### 3. 전체 빌드
```bash
# 모든 모듈 빌드
./gradlew build

# 개별 모듈 빌드
./gradlew :core:build
./gradlew :cli:build  
./gradlew :api:build
```

## 📦 JAR 크기 최적화

### Before (단일 모듈)
- `funny.jar`: ~50MB (CLI + API + Ktor 의존성)

### After (멀티 모듈)
- `core.jar`: ~5MB (비즈니스 로직만)
- `cli.jar`: ~10MB (core + CLI 의존성)
- `api.jar`: ~45MB (core + Ktor 의존성)

## 🔄 마이그레이션 계획

### Phase 1: 모듈 구조 생성
- [ ] `settings.gradle.kts` 멀티모듈 설정
- [ ] 각 모듈별 `build.gradle.kts` 생성
- [ ] 디렉토리 구조 생성

### Phase 2: 코드 이동
- [ ] 기존 코드를 core 모듈로 이동
- [ ] CliAdapter → cli 모듈
- [ ] WebAdapter → api 모듈

### Phase 3: 빌드 및 배포 스크립트
- [ ] 각 모듈별 실행 스크립트
- [ ] Docker 설정 분리
- [ ] 테스트 검증

### Phase 4: 문서 업데이트
- [ ] README.md 업데이트
- [ ] 배포 가이드 작성

## 🎯 기대 효과

1. **명확한 분리**: CLI와 API 완전 독립 배포
2. **크기 최적화**: 필요한 의존성만 포함
3. **개발 효율성**: 모듈별 독립 개발/테스트
4. **확장성**: 새로운 인터페이스 추가 용이
5. **유지보수성**: 관심사 분리로 코드 관리 개선
