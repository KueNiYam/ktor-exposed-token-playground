# 패치노트 (Changelog)

## [Unreleased]
### Added
### Changed  
### Fixed
### Removed

## [v1.6.0] - 2025-11-11
### Added
- TokenizerPort 인터페이스 구현 (헥사고날 아키텍처 완성)
- API DTO 분리 (TokenizeDto.kt, MethodsDto.kt)
- DTO 팩토리 메서드 구현

### Changed
- API 파라미터 단순화 (methods → method)
- Triple 사용 제거, Pair 구조로 개선
- 문서 정리 및 중복 제거

### Removed
- 불필요한 중복 코드 및 추상화

## [v1.5.0] - 2025-11-11
### Changed
- 코드 품질 개선 및 아키텍처 정리
  - TokenizerRegistry를 infrastructure → domain으로 이동 (도메인 서비스)
  - 불필요한 ListMethodsUseCase 제거
  - TokenizeUseCase 메서드 정리 (사용되지 않는 메서드 제거)
  - 의존성 주입 통일 (CLI도 생성자 주입으로 변경)

### Removed
- ListMethodsUseCase 클래스 (중복 로직 제거)
- TokenizeUseCase.execute(text, methodName) 메서드 (사용 안됨)
- TokenizerRegistry 불필요한 메서드들
- infrastructure 패키지 (빈 폴더)

### Fixed
- 헥사고날 아키텍처 레이어 배치 정정
- 코드 중복 및 불필요한 복잡성 제거
- 의존성 주입 일관성 개선

## [v1.4.0] - 2025-11-11
### Added
- ops/ 폴더 구조 도입
  - ops/cli/: CLI 실행 스크립트
  - ops/api/: API 배포 스크립트  
  - ops/docker/: Docker 설정 파일들
  - ops/README.md: 운영 가이드

### Changed
- 운영 스크립트와 코드 완전 분리
- Docker 파일들을 ops/docker/로 이동
- 배포 스크립트를 ops/api/로 이동
- CLI 스크립트를 ops/cli/로 이동

### Fixed
- 프로젝트 구조 더욱 명확하게 정리
- 관심사 분리 (코드 vs 운영) 완성

## [v1.3.0] - 2025-11-11
### Changed
- 헥사고날 아키텍처 구조 개선
  - adapter 상위 모듈 생성
  - api와 cli를 adapter 하위 모듈로 재구성
  - 패키지 구조: adapter.primary (CLI, API 어댑터)
- 프로젝트 구조 정리
  - 불필요한 루트 src/ 폴더 제거
  - 멀티모듈 구조 완전 정착

### Fixed
- 모듈 구조 일관성 개선
- 헥사고날 아키텍처 패턴 완전 적용

## [v1.2.0] - 2025-11-11
### Added
- 멀티모듈 아키텍처 구현
  - core: 순수 비즈니스 로직 모듈 (48KB)
  - cli: CLI 전용 모듈 (1.7MB)
  - api: REST API 서버 모듈 (15MB)
- 독립 배포 스크립트
  - CLI: ./cli/tokenize.sh
  - API: ./api/deploy-api.sh
- Docker 컨테이너화 지원
- 모듈별 Gradle 빌드 설정

### Changed
- 단일 JAR에서 멀티모듈로 완전 분리
- JAR 크기 85% 최적화 (CLI 기준)
- 의존성 분리 (CLI에서 Ktor 제거)
- 배포 프로세스 완전 분리

### Fixed
- 포트 충돌 문제 해결 (독립 실행)
- 의존성 혼재 문제 해결
- 크기 비효율성 문제 해결

## [v1.1.0] - 2025-11-11
### Added
- 헥사고날 아키텍처 구현
  - Domain 계층: Tokenizer sealed interface, Token, TokenizedText
  - Application 계층: TokenizeUseCase, ListMethodsUseCase
  - Primary Adapter: CliAdapter
  - Infrastructure: TokenizerRegistry
- API 개발 계획 문서 (API_DEVELOPMENT_PLAN.md)
- 표준 Gradle 디렉토리 구조 적용
- 의존성 주입 패턴 도입

### Changed
- TokenizerInfo → TokenizerMeta 리네이밍 (더 간결한 네이밍)
- 패키지 구조 재편성 (domain, application, adapters, infrastructure)
- UseCase 패턴으로 비즈니스 로직 분리
- 문서 파일들을 docs/ 폴더로 이동

### Fixed
- 코드 구조 개선으로 유지보수성 향상
- 테스트 커버리지 유지 (모든 기능 정상 작동 확인)

## [v1.0.0] - 2025-11-07
### Added
- Sealed interface 기반 토큰화 시스템 구현
- 14가지 토큰화 방법 지원
  - 공백 기준, 단어 기준, 문자 기준, 구두점 포함
  - 서브워드 (BPE), 바이트 기준, 길이 기준
  - 빈도 기반, TF-IDF 기반, 언어 혼합 토큰화
- 통합된 Token 모델 (value, score?, type?)
- JSON 출력 형식 지원
- 명령어 기반 인터페이스 (tokenize, list, help)
- JUnit 5 기반 단위 테스트 (8개 테스트)
- Gradle 빌드 시스템 적용
- 실행 스크립트 (tokenize.sh)
- 색상 코딩된 터미널 출력
- 실행시간 측정 및 메타데이터 제공

### Changed
- 프로젝트 구조를 Gradle 표준 디렉토리로 변경
- 기존 3개 인터페이스를 1개 sealed interface로 통합
- 토큰화 결과 타입 통합 (TokenizationResult)

### Fixed
- 토큰화 방법별 일관성 없던 API 통일
- 메모리 효율성 개선

### Removed
- 기존 run.sh 스크립트 (tokenize.sh로 대체)
- 중복된 토큰화 결과 타입들
