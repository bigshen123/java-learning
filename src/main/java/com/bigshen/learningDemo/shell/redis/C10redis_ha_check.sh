#!/bin/bash

## netman容器keepalived的检测脚本五秒执行一次该检测脚本->连接redis检测连通性

REDIS_HOST="127.0.0.1"
REDIS_PORT="64000"
REDIS_PASSWORD="kl123456"

if redis-cli -h "$REDIS_HOST" -p "$REDIS_PORT" -a "$REDIS_PASSWORD" ping &>/dev/null; then
    exit 0
else
    exit 1
fi
