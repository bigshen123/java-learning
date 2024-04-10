#!/bin/bash

clear_redis() {
    local port="${1}"
    docker rm -f redis_${port}
    $SHELL_RM -rf ./redis_${port}
}

get_rm_command() {
    if [ -e "/opt/homebrew/bin/trash" ]; then
        SHELL_RM=$(which trash)
        SHELL_RM="$SHELL_RM -F"
    else
        SHELL_RM=$(which rm)
    fi
}

main() {
    local port="$1"
    get_rm_command
    if [ "${port}" == "" ]; then
        clear_redis 6381
        clear_redis 6382
        clear_redis 6383
        clear_redis 6384
        clear_redis 6385
        clear_redis 6386
        return
    fi
    clear_redis $port
}

param="$1"

main "${param}"
