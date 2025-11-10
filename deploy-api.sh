#!/bin/bash

# API 배포 스크립트 (ops 폴더로 위임)
exec ./ops/api/deploy-api.sh "$@"
