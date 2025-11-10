# Ops - 운영 스크립트 및 설정

이 폴더는 애플리케이션 운영에 필요한 스크립트와 설정 파일들을 포함합니다.

## 구조

```
ops/
├── cli/                    # CLI 관련 운영 스크립트
│   └── tokenize.sh        # CLI 실행 스크립트
├── api/                   # API 관련 운영 스크립트  
│   └── deploy-api.sh      # API 배포 스크립트
└── docker/                # Docker 관련 설정
    ├── Dockerfile         # API 서버 Docker 이미지
    └── docker-compose.yml # Docker Compose 설정
```

## 사용법

### CLI 실행
```bash
# 루트에서
./tokenize.sh tokenize "텍스트"

# 또는 직접
./ops/cli/tokenize.sh tokenize "텍스트"
```

### API 배포
```bash
# 루트에서
./deploy-api.sh

# 또는 직접
./ops/api/deploy-api.sh
```

### Docker 배포
```bash
# Docker 빌드 & 실행
cd ops/docker
docker build -t tokenizer-api .
docker run -p 8080:8080 tokenizer-api

# Docker Compose
cd ops/docker  
docker-compose up --build
```
