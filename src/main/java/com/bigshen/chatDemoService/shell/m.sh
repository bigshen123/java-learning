#!/bin/bash
set -e

stop() {
    echo 'stop'
}
resume() {
    echo 'resume'
}
cleanup() {
    if [ $? -eq 0 ]; then
        echo "SUCCESS: 本次正常退出!"
    else
        echo "ERROR: 执行脚本,异常退出"
    fi
}

trap stop TSTP         #kill -20 会执行
trap resume CONT       # kill -18 会执行
trap cleanup EXIT QUIT # 当kill时,会执行

method=$1
target=$2
target1=$3

rms_log=/var/log/nsag/rms/rms.log
pps_log=/var/log/nsag/pps/pps.log
trp_m_log=/var/log/nsag/trp/error_manager.log
trp_error_log=/var/log/nsag/trp/error.log
trp_s_log=/var/log/nsag/trp/trp_sys/trp_sys.log
trp_test_m_log=/var/log/nsag/trp_test/error-manager.log
trp_test_s_log=/var/log/nsag/trp/trp_sys_test/trp_sys_test.log
telegraf_log=/var/log/nsag/telegraf/telegraf.log
vpn_log=/var/log/nsag/vpn/vpn.log
dms_log=/var/log/nsag/dms/dms.log
apisix_log=/var/log/nsag/apisix/apisix.log
kms_log=/var/log/nsag/kms/kms.log
kms_test_log=/var/log/nsag/kms_test/kms_test.log

#在
debug_rms() {
    declare -i line=$(grep -n "  rms:" /var/cfg/docker-compose.yml | cut -d ":" -f 1)
    line=line+1
    sed -i "${line}i \    command: -d" /var/cfg/docker-compose.yml

    declare -i line=$(grep -n "/var/data/rms/sqlite:" /var/cfg/docker-compose.yml | cut -d ":" -f 1)
    line=line+1
    sed -i "${line}i \      - /opt/rms/lib:/gw-cloud-rms/lib:rw" /var/cfg/docker-compose.yml

    cd /var/cfg
    docker-compose up -d rms
}

debug_pps() {
    declare -i line=$(grep -n "  pps:" /var/cfg/docker-compose.yml | cut -d ":" -f 1)
    line=line+1
    sed -i "${line}i \    command: -d" /var/cfg/docker-compose.yml

    declare -i line=$(grep -n "/var/data/pps/client:" /var/cfg/docker-compose.yml | cut -d ":" -f 1)
    line=line+1
    sed -i "${line}i \      - /opt/pps/lib:/gw-cloud-pps/lib:rw" /var/cfg/docker-compose.yml

    cd /var/cfg
    docker-compose up -d pps
}

while_yes_no() {
    while true; do
        read -r -p "请确认是否进行操作[Y/n] " input

        case $input in
        [yY][eE][sS] | [yY])
            echo "Yes"
            break
            ;;

        [nN][oO] | [nN])
            echo "No"
            exit 0
            ;;
        *)
            echo "Invalid input..."
            exit 1
            ;;
        esac
    done

}
vlog() {
    case $target in
    "rms")
        vim $rms_log
        ;;
    "pps")
        vim $pps_log
        ;;
    "trp")
        echo "使用:bn 切换编辑到下一个文件"
        vim $trp_s_log $trp_m_log
        ;;
    "trp_test")
        echo "使用:bn 切换编辑到下一个文件"
        vim $trp_test_s_log $trp_test_m_log
        ;;
    "telegraf")
        vim $telegraf_log
        ;;
    "dms")
        vim $dms_log
        ;;
    "netman")
        vim $netman_log
        ;;
    *)
        echo "Invalid input..."
        exit 1

        ;;
    esac
}
tlog() {
    case $target in
    "rms")
        tail -f -n 100 $rms_log
        ;;
    "pps")
        tail -f -n 100 $pps_log
        ;;
    "trp")
        if [ -f ${trp_m_log}.1 ]; then
            tail -f -n 20 $trp_s_log $trp_m_log ${trp_m_log}.1 ${trp_error_log}
        else
            tail -f -n 20 $trp_s_log $trp_m_log ${trp_error_log}
        fi
        ;;
    "trp_test")
        tail -f -n 20 $trp_test_s_log $trp_test_m_log
        ;;
    "telegraf")
        tail -f -n 100 $telegraf_log
        ;;
    "dms")
        tail -f -n 100 $dms_log
        ;;
    "netman")
        tail -f -n 100 $netman_log
        ;;
    *)
        echo "Invalid input..."
        exit 1
        ;;
    esac
}

dlog() {
    case $target in
    "rms")
        cp $rms_log ${rms_log}_jjc_$(date +%Y%m%d)_$(date +%H%M%S)
        echo "" >/var/log/nsag/rms/rms.log
        ;;
    "pps")
        cp $pps_log ${pps_log}_jjc_$(date +%Y%m%d)_$(date +%H%M%S)
        echo "" >/var/log/nsag/pps/pps.log
        ;;
    "trp")
        cp $trp_m_log ${trp_m_log}_jjc_$(date +%Y%m%d)_$(date +%H%M%S)
        echo "" >$trp_m_log
        cp $trp_s_log ${trp_s_log}_jjc_$(date +%Y%m%d)_$(date +%H%M%S)
        echo "" >${trp_s_log}
        ;;
    "trp_test")
        cp $trp_test_m_log ${trp_test_m_log}_$(date +%Y%m%d)_$(date +%H%M%S)
        echo "" >$trp_test_m_log
        cp $trp_test_s_log ${trp_test_s_log}_$(date +%Y%m%d)_$(date +%H%M%S)
        echo "" >$trp_test_s_log
        ;;
    "telegraf")
        cp $telegraf_log ${telegraf_log}_$(date +%Y%m%d)_$(date +%H%M%S)
        echo "" >$telegraf_log
        ;;
    "dms")
        cp $dms_log ${dms_log}_$(date +%Y%m%d)_$(date +%H%M%S)
        echo "" >$dms_log
        ;;
    "netman")
        cp $netman_log ${netman_log}_$(date +%Y%m%d)_$(date +%H%M%S)
        echo "" >${netman_log}
        ;;
    *)
        echo "Invalid input..."
        exit 1
        ;;
    esac
}

exec() {
    case $target in
    "rms")
        docker exec -it rms bash
        ;;
    "pps")
        docker exec -it pps bash
        ;;
    "trp")
        docker exec -w /opt/TRP/data/0 -it trp bash
        ;;
    "vpn")
        docker exec -w /opt/VPN/data/0 -it trp bash
        ;;
    "trp_test")
        docker exec -w /opt/TRP/data/0 -it trp_test bash
        ;;
    "etcd")
        echo docker exec -it etcd etcdctl put /hello world --endpoints=127.0.0.1:64600
        echo docker exec -it etcd etcdctl get --prefix=true "/hello" --endpoints=127.0.0.1:64600
        ;;
    "telegraf")
        docker exec -it telegraf bash
        ;;
    *)
        echo "Invalid input..."
        exit 1
        ;;
    esac
}

install() {
    echo "请把相关jar包或者文件夹放在/tmp/ .."
    while_yes_no
    case $target in
    "rms")
        cd /tmp
        local name=$(ls -lt | grep gw-cloud-rms | awk -F ' ' '{print $NF}' | awk 'NR==1{print}')
        if [ -z "$name" ]; then
            echo "未发现对应jar包!!!"
            exit 1
        fi
        mkdir -p /opt/rms/lib
        rm -rf /opt/rms/lib/*
        cd /opt/rms/lib
        mv /tmp/$name /opt/rms/lib/
        unzip $name
        docker restart rms
        m tlog rms
        ;;
    "pps")
        cd /tmp
        local name=$(ls -lt | grep gw-cloud-pps | awk -F ' ' '{print $NF}' | awk 'NR==1{print}')
        if [ -z "$name" ]; then
            echo "未发现对应jar包!!!"
            exit 1
        fi
        mkdir -p /opt/pps/lib
        rm -rf /opt/pps/lib/*
        cd /opt/pps/lib
        mv /tmp/$name /opt/pps/lib
        unzip $name
        docker restart pps
        m tlog pps
        ;;
    "trp")
        cd /tmp
        if [ ! -d /tmp/script ]; then
            echo "未发现对应/tmp/script文件夹!!!"
            exit 1
        else
            echo "存在文件夹/tmp/script, 准备install .."
        fi
        docker cp /tmp/script trp:/opt/TRP/data/0/
        docker restart trp
        m tlog trp
        ;;
    "trp_test")
        cd /tmp
        if [ ! -d /tmp/script ]; then
            echo "未发现对应/tmp/script文件夹!!!"
            exit 1
        else
            echo "存在文件夹/tmp/script, 准备install .."
        fi
        docker cp /tmp/script trp_test:/opt/TRP/data/0/
        docker restart trp_test
        m tlog trp_test
        ;;
    *)
        echo "Invalid input..."
        exit 1
        ;;
    esac
}

recreate() {
    case $target in
    "rms")
        cd /var/cfg
        docker-compose stop rms
        docker-compose rm rms
        docker-compose up -d rms
        ;;
    "pps")
        cd /var/cfg
        docker-compose stop pps
        docker-compose rm pps
        docker-compose up -d pps
        ;;
    "trp")
        cd /var/cfg
        docker-compose stop trp
        docker-compose rm trp
        docker-compose up -d trp
        ;;
    "trp_test")
        cd /var/cfg
        docker-compose stop trp_test
        docker-compose rm trp_test
        docker-compose up -d trp_test
        ;;
    "etcd")
        cd /var/cfg
        docker-compose stop etcd
        docker-compose rm etcd
        docker-compose up -d etcd
        ;;
    "telegraf")
        cd /var/cfg
        docker-compose stop rms
        docker-compose rm rms
        docker-compose up -d rms
        ;;
    *)
        echo "Invalid input..."
        exit 1
        ;;
    esac
}
etcd() {
    case $target in
    "onlykey")
        etcdctl get --prefix=true --keys-only=true $target1
        ;;
    "json")
        etcdctl get -w json $target1
        ;;
    *)
        echo "Invalid input..."
        exit 1
        ;;
    esac
}

debug() {
    case $target in
    "pps")
        debug_pps
        ;;
    "etcd")
        #提前定义一些占位符
        old_etcd_placeholder="ETCD_LISTEN_CLIENT_URLS=http://127.0.0.1:64600,http://172.30.30.1:64600,http://172.30.30.65:64600"
        new_etcd_placeholder="ETCD_LISTEN_CLIENT_URLS=http://0.0.0.0:64600"
        sed -i "s#$old_etcd_placeholder#$new_etcd_placeholder#g" /var/cfg/docker-compose.yml
        cd /var/cfg
        docker-compose up -d etcd
        ;;
    "rms")
        debug_rms
        ;;
    *)
        echo "Invalid input..."
        exit 1
        ;;
    esac
}

case $method in
"debug")
    debug
    ;;
"vlog")
    vlog
    ;;
"tlog")
    tlog
    ;;
"dlog")
    dlog
    ;;
"exec")
    exec
    ;;
"install")
    install
    ;;
"status")
    ls /var/run/docker.stat/* | cat
    echo "-------------------------"
    ls /var/run/docker.stat/* | xargs cat
    ;;
"recreate")
    recreate
    ;;
"etcd")
    etcd
    ;;
*)
    echo "Invalid input..."
    exit 1
    ;;
esac
