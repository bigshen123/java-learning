#!/bin/bash

## macOs 获取 trash 命令
get_rm_command() {
    if [ -e "/opt/homebrew/bin/trash" ]; then
        SHELL_RM=$(which trash)
        SHELL_RM="$SHELL_RM -F"
    else
        SHELL_RM=$(which rm)
    fi
}

## 下面的 sed 命令 在 linux 下 需要 把 '' 删掉
build_confs() {
    for PORT in "${REDIS_CLUSTER_PORTS[@]}"; do
        local current_redis_conf="redis_${PORT}"
        $SHELL_RM -rf "${current_redis_conf}"
        mkdir -p "${current_redis_conf}/conf"
        local redis_conf_dir="${current_redis_conf}/conf/redis.conf"
        cp -f redis_template.conf "$redis_conf_dir"
        sed -i '' "s/\${INSIDE_PORT}/${PORT}/g" "$redis_conf_dir"
        sed -i '' "s/\${HOST_IP}/${HOST_IP}/g" "$redis_conf_dir"
        sed -i '' "s/\${REQUIREPASS}/${REQUIREPASS}/g" "$redis_conf_dir"
        sed -i '' "s/\${MASTERAUTH}/${MASTERAUTH}/g" "$redis_conf_dir"
    done

}

main() {
    get_rm_command
    build_confs
}

##########################################

## 替换成自己的机器 ip ，不要写成 127.0.0.1
HOST_IP="10.0.200.135"
## 登陆密码
REQUIREPASS=Wl123456
## 主机密码
MASTERAUTH=$REQUIREPASS
## 集群的内部端口，不要大于 9999
REDIS_CLUSTER_PORTS=(6381 6382 6383 6384 6385 6386)

main
