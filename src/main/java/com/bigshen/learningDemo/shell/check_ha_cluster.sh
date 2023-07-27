#!/bin/bash
set -e
## 加入集群后，keepalived.conf中加入vrrp_script
## 切换要求：
## 1、当检测到trp服务异常且备机trp服务正常时
## 2、当检测trp、vpn、spa、pps、rms的健康状态，当主机状态都异常并且备机全正常时切到备机

record_log() {
    msg=$1
    echo "$msg"
    logger "$msg"
}

check_ha_server_status() {

    if [ ! -f /run/ha.status ]; then
        ## /run/ha.status文件不存在，不进行检测
        record_log "/run/ha.status does not exist"
        exit 0
    fi

    local ha_status=$(cat /run/ha.status | cut -d '@' -f1)
    if [ "${ha_status}" != master ]; then
        ## 备机不进行检测
        exit 0
    fi

    local primary_trp_status=$(curl -s http://127.0.0.1:60080/api/v1/cluster/trp/actuator/health | jq -r '.status' 2>/dev/null || echo "unknown")
    if [ "$primary_trp_status" != "UP" ]; then
        record_log "Error: primary trp server is down!"
        local secondary_trp_status=$(curl -s http://127.0.0.1:60080/api/v1/secondary/trp/actuator/health | jq -r '.status' 2>/dev/null || echo "unknown")
        if [ "$secondary_trp_status" != "UP" ]; then
            record_log "Error: secondary trp server is down!"
            exit 0
        fi
        exit 1
    fi

    ## 获取当前机器是主机还是从机
    local ha_status=$(cat /var/cfg/node-local-config/node-env.properties | grep "env.CLUSTER_ROLE" | awk -F '=' '{print $2}')
    if [ "${ha_status}" == "secondary" ]; then
        ## 从节点直接退出检测
        exit 0
    fi

    ## trp服务健康检测
    local primary_manage_address=$(curl -s http://127.0.0.1:60080/api/v1/rms/ha/me | jq -r ".data.primaryManageAddress" 2>/dev/null || echo "unknown")
    local secondary_manage_address=$(curl -s http://127.0.0.1:60080/api/v1/rms/ha/me | jq -r ".data.secondaryManageAddress" 2>/dev/null || echo "unknown")

    local primary_trp_status=$(curl -ks "${primary_manage_address}/api/v1/trp/actuator/health" | jq -r '.status' 2>/dev/null || echo "unknown")
    if [ "$primary_trp_status" != "UP" ]; then
        record_log "Error: primary trp server is down!"
        local secondary_trp_status=$(curl -ks "${secondary_manage_address}/api/v1/trp/actuator/health" | jq -r '.status' 2>/dev/null || echo "unknown")
        if [ "$secondary_trp_status" != "UP" ]; then
            record_log "Error: secondary trp server is down!"
            exit 0
        fi
        exit 1
    fi

    ## pps服务健康检测
    curl -ks "${primary_manage_address}/api/v1/pps/actuator/health" \
    -H "kl-x-consumer-token: ${nsag_token}" --compressed --insecure >/home/koal/pps_status.txt
    local primary_pps_status=$(cat /home/koal/pps_status.txt | jq -r '.status' 2>/dev/null || echo "unknown")
    if [ "$primary_pps_status" != "UP" ]; then
        record_log "Error: primary pps server is down!"
        local secondary_pps_status=$(curl -ks "${secondary_manage_address}/api/v1/pps/actuator/health" | jq -r '.status' 2>/dev/null || echo "unknown")
        if [ "$secondary_pps_status" != "UP" ]; then
            record_log "Error: secondary pps server is down!"
            exit 1
        fi
        exit 1
    fi

    ## rms服务健康检测
    local primary_rms_status=$(curl -ks "${primary_manage_address}/api/v1/rms/actuator/health" | jq -r '.status' 2>/dev/null || echo "unknown")
    if [ "$primary_rms_status" != "UP" ]; then
        record_log "Error: primary rms server is down!"
        local secondary_rms_status=$(curl -ks "${secondary_manage_address}/api/v1/rms/actuator/health" | jq -r '.status' 2>/dev/null || echo "unknown")
        if [ "$secondary_rms_status" != "UP" ]; then
            record_log "Error: secondary rms server is down!"
            exit 1
        fi
        exit 1
    fi

    if [ ! -f "$HA_STATUS_FILE" ]; then
        ## /run/ha.status文件不存在，不进行检测
        record_log "/run/ha.status does not exist"
        exit 1
    fi

    # Check the status of keepalived, only check on the master keepalived
    local keepalived_status=$(cut -d '@' -f1 "$HA_STATUS_FILE")
    if [ "$keepalived_status" != "master" ]; then
        exit 0
    fi

    # Check the status of the primary and secondary TRP servers
    local ha_status=$(grep "env.CLUSTER_ROLE" /var/cfg/node-local-config/node-env.properties | awk -F '=' '{print $2}')
    case "$ha_status" in
    primary)
        local local_trp_status=$(get_status "http://$TRP_SERVER/api/v1/cluster/trp/actuator/health")
        local remote_trp_status=$(get_status "http://$TRP_SERVER/api/v1/secondary/trp/actuator/health")
        ;;
    secondary)
        local local_trp_status=$(get_status "http://$TRP_SERVER/api/v1/trp/actuator/health")
        local remote_trp_status=$(get_status "http://$TRP_SERVER/api/v1/cluster/trp/actuator/health")
        ;;
    esac

    if [ "$local_trp_status" != "UP" ]; then
        record_log "Error: primary trp server is down!"
        if [ "$remote_trp_status" != "UP" ]; then
            record_log "Error: secondary trp server is down!"
            exit 1
        fi
        exit 1
    fi
    if [ "$local_trp_status" != "UP" ]; then
        record_log "Error: local trp server is down!"
        exit 1
    fi
}

check_ha_server_status
