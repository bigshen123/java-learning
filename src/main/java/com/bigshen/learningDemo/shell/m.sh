#!/bin/bash
set -e

log_error() {
    echo -e "\033[31m[ERROR]: $1\033[0m"
}
log_info() {
    echo -e "\033[32m[INFO]: $1\033[0m"
}

get_arch() {
    local -r processor_type=$(uname -m)

    case "$processor_type" in
    "x86_64")
        archPkg="AmdPkg"
        arch=amd64
        ;;
    "aarch64")
        archPkg="ArmPkg"
        arch=arm64
        ;;
    *)
        echoRed "ERROR: Unknow processor type!"
        exit 1
        ;;
    esac
}

on_exit() {
    rc=$?
    if [ $rc -ne 0 ]; then
        log_error "The script is exiting with status $rc"
    fi
}

# 将on_exit函数与EXIT信号关联
trap on_exit EXIT

print_help() {
    if [ "$1" == "-h" ] || [ "$1" == "--help" ] || [ "$1" == "-help" ]; then
        log_info "Usage:"
        log_info "m arg1 arg2"
        log_info
        log_info "For example:"
        log_info "m tlog rms,  exec [ tail -f /var/log/nsag/RMS.log ]"
        log_info "m vlog rms,  exec [ vim /var/log/nsag/RMS.log ]"
        log_info "m vconf rms, exec [ vim /var/data/confs/repository/rms-production.yml ]"
        log_info "m exec rms,  exec [ docker exec -w /gw-cloud-rms/lib/BOOT-INF/lib/ -it rms bash ]"
        log_info "m pull rms,  exec [ from /var/cfg/.env , docker pull and up ]"
        log_info "m debug rms, exec [ add docker-compose.yml file , pps and rms add  'command: -d' ]"
        log_info "m nsag http://xxx.tar.gz  NSAG ,  [ 将会云模式安装NSAG ]"
        log_info "m nsag http://xxx.tar.gz  NSAG hardware , [ 将会硬件模式安装NSAG ]"
        log_info "m nsag http://xxx.tar.gz  IGMP ,  [ 将会云模式安装IGMP ]"
        log_info "m nsag http://xxx.tar.gz  IGMP hardware ,  [ 将会硬件模式安装IGMP ]"
        log_info "m status,    exec [ cat /var/run/docker.stat/* ]"
        exit 0
    fi
}

is_empty() {
    local tmp_var=$1
    if [ "$tmp_var" == "" ]; then
        return 1
    fi
}

is_exists() {
    local tmp_var=$1
    if [ ! -f "$tmp_var" ]; then
        return 1
    fi
}
is_empty_with_exit() {
    is_empty "$1" && rc=$? || rc=$?
    if [ $rc -ne 0 ]; then
        log_error "Not fount var, -> $1 "
        exit 1
    fi

}
is_file_exists_with_exit() {
    is_exists "$1" && rc=$? || rc=$?
    if [ $rc -ne 0 ]; then
        log_error "Not fount file, -> $1 "
        exit 1
    fi
}

is_continue() {
    while true; do
        read -r -p "请确认是否进行操作[Y/n] " input

        case $input in
        [yY][eE][sS] | [yY])
            log_info "Yes"
            break
            ;;

        [nN][oO] | [nN])
            log_info "No"
            exit 0
            ;;
        *)
            log_error "Invalid input..."
            ;;
        esac
    done

}

check_ping_success() {
    # 检查是否指定了域名参数
    if [ -z "$1" ]; then
        log_error "请指定要解析的域名"
        return 1
    fi

    # 使用 ping 命令检查域名是否可以解析
    if ping -c 1 "$1" >/dev/null 2>&1; then
        log_info "域名 $1 可以解析"
        return 0
    else
        log_error "域名 $1 无法解析"
        return 1
    fi
}

install_nsag() {
    # 获取文件的URL
    url=$1
    install_app=$2
    install_device_type=$3
    if [ "$install_app" == "" ]; then
        install_app=NSAG
    fi

    if [ "$install_device_type" == "" ]; then
        install_device_type=cloud
    fi

    # 获取文件名称
    filename="${url##*/}"

    # 指定存放目录
    dir="/home/download/$(date +"%H%M%S")"
    mkdir -p "$dir"

    log_info "将要下载并安装NSAG网关，执行命令为：[INSTALL_APP=$install_app INSTALL_DEVICE_TYPE=$install_device_type REUSE_OLD_DATA=false SKIP_SPACE_CHECK=true NSAG_SKIP_SPACE_CHECK=true $dir/NSAG/nsag_install.sh]"
    is_continue

    check_ping_success debian.koal.com && rc=$? || rc=$?
    if [ $rc -ne 0 ]; then
        cat /etc/resolv.conf >/etc/resolv.conf.back
        echo "nameserver 10.0.1.9" >/etc/resolv.conf
        sleep 1
    fi

    # 使用curl命令下载文件
    log_info "将要下载文件到 [$dir/$filename]"
    curl -o "$dir/$filename" "$url"

    # 获取文件的扩展名
    ext="${filename##*.}"

    # 如果文件是.zip结尾，则使用unzip解压
    if [ "$ext" == "zip" ]; then
        unzip -P "KOa1UpgradeV7" "$dir/$filename" -d "$dir"
        log_info "unzip解压缩目录为：$dir"
    # 如果文件是.tar.gz结尾，则使用tar命令解压
    elif [ "$ext" == "gz" ]; then
        tar -zxvf "$dir/$filename" -C "$dir"
        log_info " tar解压缩目录为：$dir"
    else
        log_error "匹配文件名称后缀错误"
        exit 1
    fi

    log_info " 将会删除安装文件包，防止磁盘空间不足 $dir/$filename"
    rm -f "$dir/$filename"

    # 执行安装命令
    if [ -f $dir/NSAG/nsag_install.sh ]; then
        local exec_cmd="INSTALL_APP=$install_app INSTALL_DEVICE_TYPE=$install_device_type REUSE_OLD_DATA=false SKIP_SPACE_CHECK=true NSAG_SKIP_SPACE_CHECK=true $dir/NSAG/nsag_install.sh"
        echo "$exec_cmd" >/var/m_install_nsag.sh
        log_info "将要执行 [$exec_cmd] 已记录安装命令： /var/m_install_nsag.sh"
        if [ "${INSTALL_DEVICE_TYPE}" == "hardware" ]; then
            log_info "硬件模式安装时，可能会修改网关名称，导致当前ssh连接中断，请进行确认是否继续！！！"
        fi
        is_continue
        INSTALL_APP="$install_app" INSTALL_DEVICE_TYPE="$install_device_type" REUSE_OLD_DATA=false SKIP_SPACE_CHECK=true NSAG_SKIP_SPACE_CHECK=true $dir/NSAG/nsag_install.sh
        usermod -s /bin/bash koal || true
        systemctl enable ssh >/dev/null 2>&1 || true
        systemctl enable sshd >/dev/null 2>&1 || true
        systemctl start ssh >/dev/null 2>&1 || true
        systemctl start sshd >/dev/null 2>&1 || true
    else
        log_error "没有发现$dir/NSAG/nsag_install.sh"
    fi
    #    ntpdate ntp.aliyun.com 2>dev/null &
    log_info "安装成功!!!!!!!!!!!!!!"
}

debug_rms_pps() {

    touch /var/data/confs/repository/rms-production.yml
    touch /var/data/confs/repository/pps-production.yml

    if grep "0.0.0.0" /var/data/confs/repository/rms-production.yml; then
        log_info "存在[0.0.0.0], 当前环境可能已经完成过debug,Skip modify yml file ..."
    else
        echo "server:" >>/var/data/confs/repository/rms-production.yml
        echo "  address: \"0.0.0.0\"" >>/var/data/confs/repository/rms-production.yml
        echo "server:" >>/var/data/confs/repository/pps-production.yml
        echo "  address: \"0.0.0.0\"" >>/var/data/confs/repository/pps-production.yml
    fi

    grep "command: -d" /var/cfg/docker-compose.yml && rc=$? || rc=$?
    if [ $rc -eq 0 ]; then
        log_error "ERROR: rms pps already exists [command: -d], Skip ..."
        exit 1
    fi
    declare -i line=$(grep -n "  rms:" /var/cfg/docker-compose.yml | cut -d ":" -f 1)
    line=line+1
    sed -i "${line}i \    command: -d" /var/cfg/docker-compose.yml

    declare -i line=$(grep -n "  pps:" /var/cfg/docker-compose.yml | cut -d ":" -f 1)
    line=line+1
    sed -i "${line}i \    command: -d" /var/cfg/docker-compose.yml
    docker-compose -f /var/cfg/docker-compose.yml up -d rms pps

}

tlog() {
    local log_path=${log_map[${param_one}]}
    log_info "will exec : [ tail -f -n 20 $log_path]"
    cd /var/log/nsag/
    eval tail -f -n 20 "$log_path"
}

vlog() {

    local log_path=${log_map[${param_one}]}
    is_file_exists_with_exit "$log_path"
    which vim >/dev/null && rc=$? || rc=$?
    log_info "will edit $conf_path"
    cd /var/log/nsag/
    if [ $rc -eq 0 ]; then
        vim "$log_path"
    else
        vi "$log_path"
    fi
}

vconf() {
    local conf_path=${conf_map[${param_one}]}
    is_file_exists_with_exit "$conf_path"
    which vim >/dev/null && rc=$? || rc=$?
    log_info "will edit $conf_path"
    cd /var/data/confs/repository/
    if [ $rc -eq 0 ]; then
        vim "$conf_path"
    else
        vi "$conf_path"
    fi
}

exec_docker_with_option() {
    local work_dir=$1
    local name=$param_one
    local shell
    if docker exec "$name" which bash >/dev/null 2>&1; then
        shell="bash"
    else
        shell="sh"
    fi
    docker exec -e name="${name^^}" -it $name sh -c 'touch ~/.bashrc && grep "PS1=*容器中*" ~/.bashrc > /dev/null || echo "export PS1=\"\[\033[0;31m\][${name}容器中:\u@\h \W]\\$ \[\033[0m\]\"" >> ~/.bashrc'
    docker exec -e PS1="\[\033[0;31m\][${name}容器中:\u@\h \W]\\$ \[\033[0m\]" -w $work_dir -it $name $shell
}

exec() {
    case $param_one in
    "rms")
        exec_docker_with_option /gw-cloud-rms/lib/BOOT-INF/lib/
        ;;
    "pps")
        exec_docker_with_option /gw-cloud-pps/lib/BOOT-INF/lib/
        ;;
    "trp")
        exec_docker_with_option /opt/TRP/data/0/conf
        ;;
    "vpn")
        exec_docker_with_option /opt/VPN/data/0/conf
        ;;
    "node-exporter")
        exec_docker_with_option /
        ;;
    "influxdb")
        docker exec -it influxdb bash -c 'influx -host 0.0.0.0 -port 64300 -database telegraf -execute "SHOW RETENTION POLICIES"'
        log_info "influx -host 0.0.0.0 -port 64300 -database telegraf -execute \"SHOW RETENTION POLICIES\""
        exec_docker_with_option /
        ;;
    *)
        exec_docker_with_option /
        ;;
    esac
}

recreate() {
    (
        grep -i "$param_one" /var/cfg/.env >/dev/null && rc=$? || rc=$?
        if [ $rc -eq 0 ]; then
            docker rm -f "$param_one"
            docker-compose -f /var/cfg/docker-compose.yml up -d "$param_one"
        else
            log_error "not fount docker image [$param_one] from /var/cfg/.env"
        fi
    )
}

etcd() {
    case $param_one in
    "onlykey")
        etcdctl get --prefix=true --keys-only=true $param_two
        ;;
    "json")
        etcdctl get -w json $param_two
        ;;
    *)
        log_error "Invalid input..."
        exit 1
        ;;
    esac
}

debug_dms() {
    log_info "检查kl-dms二进制  dlv二进制是否存在"
    is_file_exists_with_exit /tmp/kl-dms
    if [ -f /tmp/dlv ]; then
        cp /tmp/dlv /usr/local/bin/dlv
    fi
    is_file_exists_with_exit /usr/local/bin/dlv
    chmod +x /tmp/kl-dms /usr/local/bin/dlv
    systemctl stop kl-dms
    systemctl stop kl-dms-upgrade
    #kill dms pid
    dms_pid=$(ps -ef | grep -E "kl-dms|abcdefgssdd" | grep -v abcdefgssdd | awk -F " " '{print $2}')
    log_info "start dms pid is $dms_pid"
    dlv attach $dms_pid --headless --listen=:2345 --api-version=2 --accept-multiclient
}

debug_dms_agent() {
    log_info "检查kl-dms二进制  dlv二进制是否存在"
    is_file_exists_with_exit /tmp/kl-dms
    if [ -f /tmp/dlv ]; then
        cp /tmp/dlv /usr/local/bin/dlv
    fi
    is_file_exists_with_exit /usr/local/bin/dlv
    chmod +x /tmp/kl-dms /usr/local/bin/dlv
    systemctl stop kl-dms
    systemctl stop kl-dms-upgrade
    #kill dms pid
    dms_pid=$(ps -ef | grep -E "/usr/local/bin/kl-dms|abcdefgssdd" | grep -v abcdefgssdd | awk -F " " '{print $2}')
    log_info "start dms agent pid is $dms_pid"
    dlv attach $dms_pid --headless --listen=:2346 --api-version=2 --accept-multiclient
}

exists_docker_server_with_exit() {
    docker ps -a | grep $param_one && rc=$? || rc=$?
    if [ $rc -ne 0 ]; then
        log_error "当前NSAG没有找到此容器，不进行处理！"
        exit
    fi
}

debug() {
    case $param_one in
    "dms")
        debug_dms
        ;;
    "redis")
        iptables -F REDIS_INPUT #移除redis防火墙
        sed -i "s/127.0.0.1/0.0.0.0/g" /var/cfg/redis/redis.conf
        docker restart redis
        ;;
    "dms-agent")
        debug_dms_agent
        ;;
    "pps" | "rms")
        debug_rms_pps
        ;;
    "influxdb")
        sed -i "s/127.0.0.1/0.0.0.0/g" /var/cfg/influxdb/influxdb.conf
        docker restart influxdb
        ;;
    "mariadb")
        exists_docker_server_with_exit
        sed -i "s/127.0.0.1/0.0.0.0/g" /var/cfg/mariadb/my.cnf
        docker restart mariadb
        ;;
    "psql")
        exists_docker_server_with_exit
        sed -i "s/127.0.0.1/0.0.0.0/g" /var/cfg/psql/postgresql.conf
        docker restart psql
        ;;
    "etcd")
        exists_docker_server_with_exit
        #提前定义一些占位符
        old_etcd_placeholder="ETCD_LISTEN_CLIENT_URLS=http://127.0.0.1:64600,http://172.30.30.1:64600,http://172.30.30.65:64600"
        new_etcd_placeholder="ETCD_LISTEN_CLIENT_URLS=http://0.0.0.0:64600"
        sed -i "s#$old_etcd_placeholder#$new_etcd_placeholder#g" /var/cfg/docker-compose.yml
        docker-compose -f /var/cfg/docker-compose.yml up -d etcd
        ;;
    *)
        log_error "Invalid input..."
        exit 1
        ;;
    esac
}

status() {
    local tmp_arr=($(ls /var/run/docker.stat/*))
    local docker_ps=$(docker ps -a)
    for ((i = 0; i < ${#tmp_arr[@]}; i++)); do
        local name=$(echo "${tmp_arr[i]}" | awk -F "/" '{print $NF}')
        tmp_value=$(cat "${tmp_arr[i]}")
        log_info "${tmp_arr[i]}  =  $tmp_value"
        echo "$docker_ps" | grep "$name"
    done

    if [ "$param_one" == "ready" ]; then
        log_info "will make pps trp status to ready ...  [ctrl+c] stop ..."
        for ((i = 1; i < 1000000; i++)); do
            chmod -R 777 /var/run/docker.stat/
            echo "ready" >/var/run/docker.stat/pps
            echo "ready" >/var/run/docker.stat/trp
            #echo "ready" >/var/run/docker.stat/rms
            sleep 0.4
        done
    fi
}
set_ssh() {
    #------------------------- copy-ssh公钥 --------------
    log_info "!!!!!!!!请确认当前环境是否为测试环境，是否要设置ssh!!!!!!!!"
    is_continue
    mkdir -p /root/.ssh/
    touch /root/.ssh/authorized_keys
    if [ -f /root/.ssh/authorized_keys ]; then
        if ! grep "AAAAB3NzaC1yc2EAAAADAQABAAABgQDERKCEQ6a8mRHMKg52UlC7hjnUaGunOcRXM" /root/.ssh/authorized_keys >/dev/null; then
            echo 'ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABgQDERKCEQ6a8mRHMKg52UlC7hjnUaGunOcRXM/LLGLc75qbyCeeO41/EXX7oogDetIqJ8Uaz2g46TW+Bjv8lxjYmaqQkvl9wuGrC6TZ2HWjtHarbX1nILEt6i8GiDgo2bQJcA4zKYnhcH9YaHNIDxFvVXKmSiCadN5Mh4hbUwR6Ie6YZ1WNomZZGthQpGryFkJKe8/nNxHUNJRo9MluBnQdOrJ9CmSKeLmUKAADmji6q1LXcURa+0QjpPP2DWcPlwQlvJoJyqbl06pVKg2C8KsGJpWRU/7GhJHte29AkboRVKl6J+0KjSWpirgSJbB6zMitBcXx77LBd+TchgToQtmgZE8if29h+ei2Bt+r3rHyBa+yJzN8UoVKxno9zBEsvOqAXlID8m5+IR/0Ht8euhvAe5qyD2lY4ZjT7ZVS6n4VfO4wfIqts1cADIUvkJBH5aQPiQoLkPqnrKnbg7V2JY1nYgQ5Mzt3Cki9xREvqdfjndpM8cmGoNStX6Yxd9Ax6Ajk= root@PS-10' >>/root/.ssh/authorized_keys
        fi
        if ! grep "2EAAAADAQABAAABgQDvaMuwm4s63gtRr0v28DtIe06TYF+mlEohzRwOgMT2Ylm4UorEGnsjK5" /root/.ssh/authorized_keys >/dev/null; then
            echo 'ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABgQDvaMuwm4s63gtRr0v28DtIe06TYF+mlEohzRwOgMT2Ylm4UorEGnsjK5m4b/raYBeYN+pen3jqnmiMtYFs/Kxjx64p3YU0J5D/BTd2tBfZi3eCg+Je45vBbbadld+nVVYw07GAjafjPmipGl6DDbDe5qFvPFkk6df+5COra2fYVpzDD9cDRHBTqISFGqhoouaptaI4cXmpkiJIIxr0PqxeO3IW7QN0zu3i69yP+iltaAJFWpLNDdcLT37eS1hF6dCEaiS9FCeyTCbstRYOkg9/hEZ4UQt7rDQ5BNgS7LC37QR7Qi/ANH0BlK2YtlwkWy+HSfYAshgl6h+bgMrB+GQzJcw2Z74ammXfGIRut2X/ZchO3Ih3gbWQULgsQFeacy0L26ucmeDzPd8R8NzYu1sQlRJBEw/F//DuoQc/ySIId6FzWPZ/zIFsXp+qJ0VxlqtcG6nHTXOqB/y30dBHu4oOrKi+htZjNggrf770Mu7tiW+HmnI7jUB9aPsI5tWOtJs= fightshadow@outlook.com' >>/root/.ssh/authorized_keys
        fi
    else
        log_error "/root/.ssh/authorized_keys not exists"
    fi

    grep "koal ALL=(ALL:ALL) NOPASSWD:ALL" /etc/sudoers >/dev/null 2>&1 && rc=$? || rc=$?
    if [ $rc -ne 0 ]; then
        echo "koal ALL=(ALL:ALL) NOPASSWD:ALL" >>/etc/sudoers
    fi
}

restart_server() {
    docker ps -a | grep $param_one >/dev/null 2>&1 && rc=$? || rc=$?
    if [ $rc -eq 0 ]; then
        log_info "--------------Will exec [docker restart $param_one]-------------- \n"
        docker restart $param_one
    else
        log_info "\n--------------Will exec [systemctl restart $param_one]-------------- \n\n"
        systemctl restart $param_one
    fi
}

try_upload_license_by_old_interface() {
  # ���义文件路径
  license_file="/tmp/license_new_"${TIMESTAMP}".conf"
  json_content=$(jq -Rs . "$license_file")

    local license_value=$(<"$license_file")
    http_code=$(curl -s -o /dev/null -w "%{http_code}" 'http://127.0.0.1:60200/sys/license' \
        -H 'content-type: application/json;charset=UTF-8' \
        -H 'user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36' \
--data-raw "{\"license\":$json_content}" \
        --insecure)
    if [ "$http_code" != "200" ]; then
        log_error "通过旧接口,上传license失败"
        exit 1
    fi
    log_info "通过旧接口,更新临时license成功"
    exit 0
}

make_license() {
  which jq >/dev/null && rc=$? || rc=$?
  if [ $rc -ne 0 ]; then
    if [ "$arch" == "amd64" ]; then
        curl http://debian.koal.com/upload/nsag-7/jq-release/amd64/jq -o /usr/local/bin/jq
    else
        curl http://debian.koal.com/upload/nsag-7/jq-release/arm64/jq -o /usr/local/bin/jq
    fi
    chmod +x  /usr/local/bin/jq
  fi

    license_info=$(curl -s http://127.0.0.1:60200/sys/license)
    LICENSE_SERIAL=$(echo "$license_info" | jq -r ".data.licenseInfos.serial")
    LICENSE_VERSION=$(echo "$license_info" | jq -r ".data.licenseInfos.version")
    is_valid=$(echo "$license_info" | jq -r ".data.isValid")
    if [ $is_valid == "true" ]; then
        log_info "当前环境licene有效,是否强制更新license?(y/n)"
        is_continue
    fi

    if [ "$LICENSE_SERIAL" == "" ] || [ "$LICENSE_SERIAL" == "null" ]; then
        log_error "未获取到license序列号"
        exit 1
    fi

    if [ "$LICENSE_VERSION" == "null" ] || [ "$LICENSE_VERSION" == "" ]; then
        log_error "未获取到license版本信息"
        exit 1
    fi

    echo "$LICENSE_VERSION" | grep -i "v" >/dev/null && rc=$? || rc=$?
    if [ $rc -eq 0 ]; then
        LICENSE_VERSION="v"
    else
        LICENSE_VERSION="c"
    fi

    TIMESTAMP=$(date +"%Y-%m-%d")
    rm -f /tmp/${TIMESTAMP}_license_test.log
    touch /tmp/${TIMESTAMP}_license_test.log

    curl -s 'http://10.0.247.105:8080/api/v1/getLicense' \
        -H 'Accept: */*' \
        -H 'Accept-Language: zh-CN,zh;q=0.9,en;q=0.8' \
        -H 'Cache-Control: no-cache' \
        -H 'Connection: keep-alive' \
        -H 'Content-Type: application/x-www-form-urlencoded; charset=UTF-8' \
        -H 'Origin: http://10.0.247.105:8080' \
        -H 'Pragma: no-cache' \
        -H 'Referer: http://10.0.247.105:8080/' \
        -H 'User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36' \
        -H 'X-Requested-With: XMLHttpRequest' \
        --data-raw 'serialNum='"$LICENSE_SERIAL"'&licenseVersion='"$LICENSE_VERSION"'&items=+-trp++-tfp++-vpn++-mobile++-pc++-xc++-pms++-bonding++-vrrp++-cluster++-spa++-ipsec++--maxUsers+10000000+--maxSessions+1000000+--maxResources+20000+--maxNodes+1000+--moreConf+&beginDate='"$TIMESTAMP"'' \
        --insecure -o /tmp/${TIMESTAMP}_license_test.log

    license_file_name=$(cat /tmp/${TIMESTAMP}_license_test.log)
    TIMESTAMP=$(date +%Y%m%d%H%M%S)

    cp -f /var/cfg/license/license.conf /var/cfg/license/license.conf_${TIMESTAMP}_backup
    rm -f /tmp/license_new_"${TIMESTAMP}".conf
    touch /tmp/license_new_"${TIMESTAMP}".conf
    curl -s http://10.0.247.105:8080/api/v1/getLicense/${license_file_name} -o /tmp/license_new_"${TIMESTAMP}".conf
    base64_encoded=$(base64 -w 0 /tmp/license_new_"${TIMESTAMP}".conf)
    http_code=$(curl -s -o /dev/null -w "%{http_code}" 'http://127.0.0.1:60200/sys/licenseNew' \
        -H 'content-type: application/json;charset=UTF-8' \
        -H 'user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36' \
        --data-raw '{"license":"'${base64_encoded}'"}')

    if [ "$http_code" != "200" ]; then
        log_error "上传license失败"
        try_upload_license_by_old_interface
        exit 1
    fi
    log_info "更新临时license成功"
    exit 0
}

backup_trp_conf() {
    local time=$(date "+____%H点%M分钟_%S秒")
    docker cp trp:/opt/TRP/data/0/conf /tmp/jjc_tmp/trp/conf_"$param_one"
    curl -s http://127.0.0.1:61100/actuator/health | grep '^{"status":"UP"' >/dev/null 2>&1 && rc=$? || rc=$?
    if [ $rc -ne 0 ]; then
        log_error "ERROR: trp service check failed, not start successfully"
        exit 1
    fi
    log_info "备份trp配置在: /tmp/jjc_tmp/trp/conf_$param_one"
    log_info "eg. diff --no-dereference -rq dir1 dir2"
}

backup_conf() {
    TIMESTAMP=$(date +"%Y-%m-%d")
    mkdir -p /tmp/jjc_tmp/trp
    backup_trp_conf
}
pull_and_up_docker() {

    check_ping_success git.koal.com && rc=$? || rc=$?
    if [ $rc -ne 0 ]; then
        log_error "git.koal.com 域名解析失败"
        cat /etc/resolv.conf >/etc/resolv.conf.back
        echo "nameserver 10.0.1.9" >/etc/resolv.conf
        sleep 1
    fi

    REGISTRY="git.koal.com:4567"
    # 尝试执行一次无密码的Docker login操作
    if ! docker login --username="" --password="" $REGISTRY >/dev/null 2>&1; then
        log_error "You are not logged into the Docker registry at $REGISTRY."
        exit 1
    else
        log_info "You are already logged in to the Docker registry at $REGISTRY."
    fi

    docker pull "$(grep -i "^${param_one//-/_}_image" /var/cfg/.env | awk -F "=" '{print $2}')"
    (
        cd /var/cfg
        #docker rm -f ${param_one}
        echo
        log_info "will exec [docker-compose -f /var/cfg/docker-compose.yml up -d ${param_one,,}]"
        docker-compose -f /var/cfg/docker-compose.yml up -d "${param_one,,}"
    )
}

kill_pids_by_grep_process_name() {
    local partten
    partten=$1
    if [ "$parrten" == "" ]; then
        log_error "搜索的进程名称为空！"
    fi

    ps -ef | grep $partten | grep -v grep
    pid=$(ps -ef | grep $partten | grep -v grep | awk '{print $2}')
    log_info "将要kill上面的进程 ..."
    is_continue
    # 判断是否找到匹配的进程
    if [ -n "$pid" ]; then
        log_info "进程 $keyword 的PID为：$pid"
        # 执行kill命令终止进程
        kill -9 $pid
    else
        log_error "未找到进程 $keyword"
    fi
}
pre_check() {
    if grep "^ID=ubuntu" /etc/os-release; then
        log_info 将要进行前置检查
        sed -i "s/Upgrade \"1\"/Upgrade \"0\"/" /etc/apt/apt.conf.d/20auto-upgrades
        kill_pids_by_grep_process_name "unattended-upgr"
    fi
}

init_log_map() {
    log_dir=/var/log/nsag
    log_map[rms]=${log_dir}/RMS.log
    log_map[pps]=${log_dir}/PPS.log
    log_map[dms]=${log_dir}/dms.log
    log_map[netman]=${log_dir}/netman.log
    log_map[psql]=${log_dir}/PSQL.log
    log_map[redis]=${log_dir}/REDIS.log
    log_map[console]=${log_dir}/CONSOLE.log
    log_map[influxdb]=${log_dir}/INFLUXDB.log
    log_map[kapacitor]=${log_dir}/KAPACITOR.log
    log_map[kms]=${log_dir}/KMS.log
    log_map[kms_test]=${log_dir}/KMS_TEST.log
    log_map[kong]=${log_dir}/KONG.log
    log_map[vpn]=${log_dir}/VPN.log
    if [ -d "${log_dir}/TRP_ERROR/" ]; then
        log_map[trp]="${log_dir}/TRP_ERROR/*.log"
    else
        log_map[trp]="${log_dir}/TRP/*.log"
    fi
}

init_conf_map() {
    conf_dir="/var/data/confs/repository/"
    conf_map[rms]=${conf_dir}/rms-production.yml
    conf_map[pps]=${conf_dir}/pps-production.yml
    conf_map[trp]=${conf_dir}/trp-production.yml
    conf_map[vpn]=${conf_dir}/vpn-production.yml
    conf_map[node]=${conf_dir}/node-production.yml
    conf_map[env]=/var/cfg/.env
    conf_map[.env]=/var/cfg/.env
    conf_map[docker]=/var/cfg/docker-compose.yml
    conf_map[compose]=/var/cfg/docker-compose.yml
    conf_map[docker-compose]=/var/cfg/docker-compose.yml
    conf_map[redis]=/var/cfg/redis/redis.conf
    conf_map[psql]=/var/cfg/psql/postgresql.conf
}
#-------------------------
method=$1
param_one=$2
param_two=$3
get_arch
declare -A log_map 2>/dev/null
declare -A conf_map 2>/dev/null && rc=$? || rc=$?
if [ $rc -ne 0 ]; then
    log_error "初始化map失败,当前bash版本太低! 跳过初始化!"
else
    init_conf_map
    init_log_map
fi

print_help $1
# 匹配函数
function fuzzy_match() {
    # 传入的参数为要匹配的字符串和匹配的模式
    # 使用通配符进行模糊匹配
    case "$2" in
    $1*) return 0 ;; # 匹配成功
    *) return 1 ;;   # 匹配失败
    esac
}

case $method in
# 使用匹配函数进行模糊匹配
# 这里可以列出所有需要进行模糊匹配的选项
# 注意，顺序很重要，将具体匹配放在前面，通用匹配放在最后
*)
    if fuzzy_match "$method" "pre_check"; then
        pre_check
    elif fuzzy_match "$method" "debug"; then
        debug
    elif fuzzy_match "$method" "tlog"; then
        tlog
    elif fuzzy_match "$method" "status"; then
        status
    elif fuzzy_match "$method" "ssh"; then
        set_ssh
    elif fuzzy_match "$method" "vlog"; then
        vlog
    elif fuzzy_match "$method" "vconf"; then
        vconf
    elif fuzzy_match "$method" "pull"; then
        pull_and_up_docker
    elif fuzzy_match "$method" "exec"; then
        exec
        status
    elif fuzzy_match "$method" "recreate"; then
        recreate
    elif fuzzy_match "$method" "etcd"; then
        etcd
    elif fuzzy_match "$method" "nsag"; then
        install_nsag "$2" "$3" "$4"
    elif fuzzy_match "$method" "restart"; then
        restart_server
    elif fuzzy_match "$method" "license"; then
        make_license
    elif fuzzy_match "$method" "backup"; then
        backup_conf
    else
        log_error "Invalid input..."
        exit 1
    fi
    ;;
esac