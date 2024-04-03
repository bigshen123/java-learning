#!/bin/bash

## redis状态发生切换时，keepalived->传入需要变更的状态，此脚本调用redis变更状态

LOG() {
    echo "REDIS_HA: $1"
    logger -t REDIS_HA -p user.info "$1"
}

REDIS_HOST="127.0.0.1"
REDIS_PORT="64000"
REDIS_AUTH="kl123456"
REMOTEIP=$(curl "http://127.0.0.1:60100/v1.0/networks/ha/script/args/N10redis_ha_notify" -H "Content-Type: application/json" -X GET)
REMOTEIP=${REMOTEIP//\"/}

# check Redis
redis_check() {
    if redis-cli -h "$REDIS_HOST" -p "$REDIS_PORT" -a "$REDIS_AUTH" ping &>/dev/null; then
        exit 0
    else
        exit 1
    fi
}

# change Redis master
change_master() {
    LOG "Start changing role to master..."
    LOG "Running REPLICAOF NO ONE"

    # 循环检测是否正在加载数据集
    while true; do
        # 获取 Redis 服务器信息
        info=$(redis-cli -h "$REDIS_HOST" -p "$REDIS_PORT" -a "$REDIS_AUTH" info)
        # 检查是否正在加载数据集 loading: 表示是否正在进行加载dump文件的标志 1:是 0:不是
        if [[ $info == *"loading":1* ]]; then
            echo "Redis is still loading the dataset."
            sleep 1
        else
            echo "Redis has finished loading the dataset."
            break
        fi
    done

    # 切换为主服务器
    if redis-cli -h "$REDIS_HOST" -p "$REDIS_PORT" -a "$REDIS_AUTH" REPLICAOF NO ONE &>/dev/null; then
        LOG "Unlinked from the old master node."
    else
        LOG "Failed to unlink from the old master node."
        exit 1
    fi
}

# change Redis slave
change_backup() {
    LOG "Start changing role to slave..."
    LOG "Running REPLICAOF $REMOTEIP $REDIS_PORT"

    # 循环检测是否正在加载数据集
    while true; do
        # 获取 Redis 服务器信息
        info=$(redis-cli -h "$REDIS_HOST" -p "$REDIS_PORT" -a "$REDIS_AUTH" info)
        # 检查是否正在加载数据集 loading: 表示是否正在进行加载dump文件的标志 1:是 0:不是
        if [[ $info == *"loading":1* ]]; then
            echo "Redis is still loading the dataset."
            sleep 1
        else
            echo "Redis has finished loading the dataset."
            break
        fi
    done

    # 切换为从服务器
    if redis-cli -h "$REDIS_HOST" -p "$REDIS_PORT" -a "$REDIS_AUTH" REPLICAOF "$REMOTEIP" "$REDIS_PORT" &>/dev/null; then
        LOG "Role changed to slave successfully."
    else
        LOG "Failed to change role to slave."
        exit 1
    fi
}

STATE=$1

case $STATE in
"")
    redis_check
    ;;
"master")
    change_master
    ;;
"backup" | "fault")
    change_backup
    ;;
*)
    echo "Invalid state: $STATE"
    exit 1
    ;;
esac
