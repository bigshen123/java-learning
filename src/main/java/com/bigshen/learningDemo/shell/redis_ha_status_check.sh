#!/bin/bash
## 检测redis集群的主从状态脚本
set -e
REDIS_PORT=64000
REDIS_PASSWORD=kl123456
check_redis_ha_status() {
    ## docker exec -i redis redis-cli -p 64000 -a kl123456 info replication | grep "role" | awk -F ':' '{print $2}' | tr -d '\r'
    local REDIS_HA_STATUS=$(docker exec -i redis redis-cli -p "${REDIS_PORT}" -a "${REDIS_PASSWORD}" info replication | grep "role" | awk -F ':' '{print $2}' | tr -d '\r')
    echo "$REDIS_HA_STATUS"
    if [ "$REDIS_HA_STATUS" == "master" ]; then
        exit 0
    elif [ "$REDIS_HA_STATUS" == "slave" ]; then
        exit 1
    fi
}
check_redis_ha_status
