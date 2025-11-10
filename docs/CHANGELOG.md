# 패치노트 (Changelog)

## [Unreleased]
### Added
### Changed  
### Fixed
### Removed

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
