#!/bin/bash

# 设置环境变量
INFLUXDB_HOST="127.0.0.1"      # 修改为你的 InfluxDB 主机地址
INFLUXDB_PORT="64300"           # 修改为你的 InfluxDB 端口
DATABASE="telegraf"       # 修改为你的数据库名称
MEASUREMENT="user_access"       # 修改为你的测量名称

# 获取当前时间
NOW=$(date +"%Y-%m-%dT%H:%M:%SZ")
ONE_MINUTE_AGO=$(date -d "1 minute ago" +"%Y-%m-%dT%H:%M:%SZ")

# 查询 InfluxDB
QUERY="SELECT COUNT(*) FROM $MEASUREMENT.$MEASUREMENT WHERE time > now()-1m"
RESULT=$(curl -s -G "http://$INFLUXDB_HOST:$INFLUXDB_PORT/query" \
    --data-urlencode "db=$DATABASE" \
    --data-urlencode "q=$QUERY")

# 提取数量
COUNT=$(echo "$RESULT" | jq '.results[0].series[0].values[0][1]')

# 记录到日志文件
LOG_FILE="/tmp/influxdb_user_access.log"
echo "$NOW - User Access Count: $COUNT" >> "$LOG_FILE"

cat "$LOG_FILE"