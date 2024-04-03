#!/bin/bash

init_param() {
    SHELL_SLAVE_SYNC_USER="${SLAVE_SYNC_USER:-sync_admin}"
    SHELL_SLAVE_SYNC_PASSWORD="${SLAVE_SYNC_PASSWORD:-123456}"
    SHELL_ADMIN_USER="${ADMIN_USER:-root}"
    SHELL_ADMIN_PASSWORD="${ADMIN_PASSWORD:-123456}"
    SHELL_MASTER_HOST="${MASTER_HOST:-localhost}"
    SHELL_MASTER_PORT="${MASTER_PORT:-3306}"
}

get_master_logFile_Position() {
    #连接master数据库，查询二进制数据，并解析出logfile和pos，这里同步用户要开启 REPLICATION CLIENT权限，才能使用SHOW MASTER STATUS;
    RESULT=$(mysql -u"$SHELL_SLAVE_SYNC_USER" -h"$SHELL_MASTER_HOST" -p"$SHELL_SLAVE_SYNC_PASSWORD" -e "SHOW MASTER STATUS;" | grep -v grep | tail -n +2 | awk '{print $1,$2}')
    LOG_FILE_NAME=$(echo "$RESULT" | awk '{print $1}')
    LOG_FILE_POS=$(echo "$RESULT" | awk '{print $2}')
}

join_master() {
    #设置连接master的同步相关信息
    if [ "${MASTER_EABLE_GTID}" != "on" ]; then
        SYNC_SQL="change master to master_host='$SHELL_MASTER_HOST',master_user='$SHELL_SLAVE_SYNC_USER',master_password='$SHELL_SLAVE_SYNC_PASSWORD',master_log_file='$LOG_FILE_NAME',master_log_pos=$LOG_FILE_POS;"
    else
        ## change master to master_host='mysqlMaster',master_user='sync_admin',master_password='123456',master_log_file='mysql-bin.000003',MASTER_AUTO_POSITION=5620;
        SYNC_SQL="change master to master_host='$SHELL_MASTER_HOST',master_user='$SHELL_SLAVE_SYNC_USER',master_password='$SHELL_SLAVE_SYNC_PASSWORD',MASTER_AUTO_POSITION=1;"
    fi
    #开启同步
    START_SYNC_SQL="start slave;"
    #查看同步状态
    STATUS_SQL="show slave status\G;"
    mysql -u"$SHELL_ADMIN_USER" -p"$SHELL_ADMIN_PASSWORD" -e "$SYNC_SQL  $START_SYNC_SQL $STATUS_SQL "
}

#####################################
#定义连接master进行同步的账号
SHELL_SLAVE_SYNC_USER=
#定义连接master进行同步的账号密码
SHELL_SLAVE_SYNC_PASSWORD=
#定义slave数据库账号
SHELL_ADMIN_USER=
#定义slave数据库密码
SHELL_ADMIN_PASSWORD=
#定义连接master数据库host地址
SHELL_MASTER_HOST=
#定义连接 master 数据库的端口
# shellcheck disable=SC2034
SHELL_MASTER_PORT=
# 主机的 logfile
LOG_FILE_NAME=
# 主机的 Position
LOG_FILE_POS=

## 初始化参数
init_param

## 获取主机的log_file 和  position
if [ "${MASTER_EABLE_GTID}" != "on" ]; then
    get_master_logFile_Position
fi

## 加入主机
join_master
