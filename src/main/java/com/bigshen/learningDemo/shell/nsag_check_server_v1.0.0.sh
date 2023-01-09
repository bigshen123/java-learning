#!/bin/bash

print_highest_memory_processed() {
    echo "########### Top 10 the highest memory usage #############"
    ps aux | head -1
    ps aux | sort -rn -k4 | head -10
    echo ""
}
print_highest_cpu_processed() {
    echo "########### Top 10 the highest CPU usage #############"
    ps aux | head -1
    ps -eo pid,pcpu,pmem,args --sort=-pcpu | head -10
    echo ""
}

print_docker_mem_info() {
    echo
    echo "########### container(s) resource usage #############"
    docker_stats=$(docker stats -a --no-stream)
    echo "$docker_stats"
    docker_sum_mem=$(echo "$docker_stats" | awk -F " " 'NR!=1{print $4}' | awk -F "MIB" '{print $1}' | awk '{sum+=$1}END{print sum}')
    sum_mem=$(free -m | awk 'NR==2{print $2}')
    docker_mem_usage=$(awk -v num1=${docker_sum_mem} -v num2=${sum_mem} 'BEGIN{print(num1/num2 * 100.0)}')
    echo
    echo "All docker containers occupy a total memory of ${docker_sum_mem} M, which is [${docker_mem_usage}%] of the total memory"
    docker_sum_cpu=$(echo "$docker_stats" | awk -F " " 'NR!=1{print $3}' | awk -F "%" '{print $1}' | awk '{sum+=$1}END{print sum}')
    echo "All docker containers occupy the current cpu utilization rate of [${docker_sum_cpu}%]"
    echo
}

print_kill_process_info() {
    dmesg -T | grep -E -i -B 10 'killed process' && rc=$? || rc=$?
    if [ $rc -eq 0 ]; then
        echo "ERROR: found killed process from kern.log, By exec [ dmesg -T | grep -E -i -B 10 'killed process' ]"
        check_result="false"
    fi
    dmesg -T | grep -E -i -B 10 'out of memory' && rc=$? || rc=$?
    if [ $rc -eq 0 ]; then
        echo "ERROR: found killed process from kern.log, By exec [ dmesg -T | grep -E -i -B 10 'out of memory' ]"
        check_result="false"
    fi
}

print_linux_info() {
    echo "############# basic info #############"
    source /etc/os-release
    echo System_Arch: "${PRETTY_NAME}" $(uname -m)
    echo System_Now_Time: $(date "+%Y-%m-%d %H:%M:%S")
    echo System_Boot_Time: $(date -d "$(awk -F. '{print $1}' /proc/uptime) second ago" +"%Y-%m-%d %H:%M:%S")
    echo GW_Product_Version: $(cat /var/cfg/product.version | grep upgrade | awk -F "=" '{print $2}')
    echo
    echo "#############  cpu info   #############"
    cpu_info=$(lscpu)
    echo "$cpu_info" | grep -i "^CPU(s):"
    echo "$cpu_info" | grep -i "^Model name:"
    echo
    echo "#############  mem info   #############"
    free -h
    echo
    echo "############ ip address ###############"

    network_cards=$(ip addr show | grep -o "^[0-9]*: [^:]*" | awk '{print $2}')
    # 遍历每个网卡
    for card in $network_cards; do
        # 获取网卡的IPV4地址
        ipv4_address=$(ip -4 addr show $card | grep "inet" | awk '{print $2}')
        # 如果存在IPV4地址，则输出网卡信息
        if [ ! -z "$ipv4_address" ]; then
            echo "$card: $ipv4_address"
        fi
    done
    print_docker_mem_info

}

check_current_user() {
    if [ $(id -u) -ne 0 ]; then
        echo "ERROR: The current user is not root，Exit the script"
        check_result="false"
    fi
}

check_base_server() {
    echo "########### Base service #############"

    # 判断rms/pps/trp是否启动成功
    curl -s http://127.0.0.1:60700/actuator/health | grep '^{"status":"UP"' >/dev/null 2>&1 && rc=$? || rc=$?
    if [ $rc -ne 0 ]; then
        echo "ERROR: rms service check failed, not start successfully"
        check_result="false"
    fi
    curl -s http://127.0.0.1:60501/v1/manage/actuator/health | grep '^{"status":"UP"' >/dev/null 2>&1 && rc=$? || rc=$?
    if [ $rc -ne 0 ]; then
        echo "ERROR: pps service check failed, not start successfully"
        check_result="false"
    fi
    curl -s http://127.0.0.1:61100/actuator/health | grep '^{"status":"UP"' >/dev/null 2>&1 && rc=$? || rc=$?
    if [ $rc -ne 0 ]; then
        echo "ERROR: trp service check failed, not start successfully"
        check_result="false"
    fi
    nsag_services_status=""
    ## 获取基础docker服务状态信息
    base_services="kms_test kms ocsp telegraf kapacitor redis console kong influxdb psql"
    for service in $base_services; do
        status=$(sudo docker inspect $service 2>&1 | grep "Status" | awk -F '"' '{print $4}')
        if [ "$status" = "running" ]; then
            status="running"
        else
            status="exit"
        fi
        nsag_services_status="${nsag_services_status}${service}:  ${status}\n"
    done
    echo -e "$nsag_services_status" | grep exit >/dev/null 2>&1 && rc=$? || rc=$?
    if [ $rc -eq 0 ]; then
        echo "ERROR: service check failed,some server not start successfully:"
        echo -e "$nsag_services_status" | grep exit
        check_result="false"
    fi
    echo
}

check_all_mem() {

    echo "#############  check mem usage  #############"
    mem_usage=$(free -m | awk 'NR==2{print $3/$2 * 100.0}')
    echo "The current physical memory usage ratio is ${mem_usage}%"
    local result=$(awk -v num1=${mem_usage} 'BEGIN{print(num1<80)?"0":"1"}')
    if [ "$result" == "1" ]; then
        echo "ERROR: Physical memory usage is too high, ${mem_usage}% > 80%"
        check_result="false"
    fi

    swap_usage=$(free -m | awk 'NR==3{if($2 == "0") $2=1;print $3/$2 * 100.0}')
    echo "The current virtual memory usage ratio is ${swap_usage}%"

    local result=$(awk -v num1=${swap_usage} 'BEGIN{print(num1<50)?"0":"1"}')
    if [ "$result" == "1" ]; then
        echo "ERROR: Virtual memory usage is too high, ${swap_usage}% > 50%"
        check_result="false"
    fi
    echo
    print_highest_memory_processed

    local result=$(awk -v num1=${mem_usage} -v num2=${docker_mem_usage} 'BEGIN{print(num1 - num2 > 30)?"0":"1"}')
    if [ "$result" == "0" ]; then
        echo "ERROR: Third-party programs occupy more than 30% of the memory capacity,${mem_usage} - ${docker_mem_usage} > 30%"
        check_result="false"
    fi

}

check_all_cpu() {
    echo "#############  check cpu usage  #############"
    cpu_usage=$(top -b -n2 | grep -i "Cpu(s)" | awk -F "ni, " 'NR==2{print $NF}' | awk -F " " '{print 100.0-$1}')
    echo "The current cpu utilization is ${cpu_usage}%"
    local result=$(awk -v num1=${cpu_usage} 'BEGIN{print(num1>60)?"0":"1"}')
    if [ "$result" == "0" ]; then
        echo "ERROR: CPU idle utilization is less than 40%: ${cpu_usage}%"
        check_result="false"
    fi
    echo
    print_highest_cpu_processed
}

check_error_from_log() {
    echo "########### Error log #############"
    echo ""
    print_kill_process_info
    rms_core_dump_count=$(zgrep -i "Core dump written" /var/log/nsag/RMS.log* | wc -l)
    if [ "$rms_core_dump_count" != "0" ]; then
        echo "ERROR: rms found core dump! By exec [ zgrep -i \"Core dump written\" /var/log/nsag/RMS.log* ]"
        check_result="false"
    fi
    pps_core_dump_count=$(zgrep -i "Core dump written" /var/log/nsag/PPS.log* | wc -l)
    if [ "$pps_core_dump_count" != "0" ]; then
        echo "ERROR: pps found core dump! By exec [ zgrep -i \"Core dump written\" /var/log/nsag/PPS.log* ]"
        check_result="false"

    fi
    trp_core_dump_count=$(zgrep -i "core dump" /var/log/nsag/TRP/error.log* | wc -l)
    if [ "$trp_core_dump_count" != "0" ]; then
        echo "ERROR: trp found core dump! By exec [ zgrep -i \"core dump\" /var/log/nsag/TRP/error.log* ]"
        check_result="false"
    fi

    trp_core_dump_count=$(zgrep -i "coredump" /var/log/nsag/TRP/error.log* | wc -l)
    if [ "$trp_core_dump_count" != "0" ]; then
        echo "ERROR: trp found core dump! By exec [ zgrep -i \"coredump\" /var/log/nsag/TRP/error.log* ]"
        check_result="false"
    fi
}

check_disk() {
    if [ "$SKIP_SPACE_CHECK" == "true" ]; then
        return 0
    fi
    #限制单位为G
    local -r check_dirs=("/ 20" "/var/log 20" "/var/log/influxdb 20")
    for dir in "${check_dirs[@]}"; do
        path=$(echo "$dir" | awk -F " " '{print $1}')
        limit=$(echo "$dir" | awk -F " " '{print $2}')
        if [ -d "$path" ]; then
            avail=$(df -h "$path" | awk -F ' ' 'NR==2 {print $4}' | sed 's/.$//')
        else
            mkdir -p "$path"
            avail=$(df -h "$path" | awk -F ' ' 'NR==2 {print $4}' | sed 's/.$//')
            rm -r "$path"
        fi
        local result=$(awk -v num1=${limit} -v num2=${avail} 'BEGIN{print(num1<num2)?"0":"1"}')
        if [ "$result" == "1" ]; then
            echo "ERROR: Check $path failure, Disk free space ${avail}G is less than ${limit}G "
            check_result="false"
        fi
    done
}

check_tcp_connect() {
    NULL=/dev/null
    ######################################################################
    tcpfile=/tmp/.tcp
    #查看并发连接数
    #CLOSED：无连接是活动的或正在进行
    #LISTEN：服务器在等待进入呼叫
    #SYN_RECV：一个连接请求已经到达，等待确认
    #SYN_SENT：应用已经开始，打开一个连接
    #ESTABLISHED：正常数据传输状态
    #FIN_WAIT1：应用说它已经完成
    #FIN_WAIT2：另一边已同意释放
    #ITMED_WAIT：等待所有分组死掉
    #CLOSING：两边同时尝试关闭
    #TIME_WAIT：另一边已初始化一个释放
    #LAST_ACK：等待所有分组死掉
    netstat -n | awk '/^tcp/ {++S[$NF]} END {for(a in S) print a, S[a]}' >$tcpfile
    grep TIME_WAIT $tcpfile >$NULL
    if [ $? -eq 1 ]; then
        echo "TIME_WAIT 0 " >>$tcpfile
    fi
    grep FIN_WAIT1 $tcpfile >$NULL
    if [ $? -eq 1 ]; then
        echo "FIN_WAIT1 0 " >>$tcpfile
    fi
    grep FIN_WAIT2 $tcpfile >$NULL
    if [ $? -eq 1 ]; then
        echo "FIN_WAIT2 0 " >>$tcpfile
    fi
    grep CLOSE_WAIT $tcpfile >$NULL
    if [ $? -eq 1 ]; then
        echo "CLOSE_WAIT 0 " >>$tcpfile
    fi
    grep LAST_ACK $tcpfile >$NULL
    if [ $? -eq 1 ]; then
        echo "LAST_ACK 0 " >>$tcpfile
    fi
    grep SYN_RECV $tcpfile >$NULL
    if [ $? -eq 1 ]; then
        echo "SYN_RECV 0 " >>$tcpfile
    fi
    grep CLOSING $tcpfile >$NULL
    if [ $? -eq 1 ]; then
        echo "CLOSING 0 " >>$tcpfile
    fi
    grep ESTABLISHED $tcpfile >$NULL
    if [ $? -eq 1 ]; then
        echo "ESTABLISHED 0 " >>$tcpfile
    fi
    local TIME_WAITV=$(grep TIME_WAIT $tcpfile | awk '{print $2}')
    local FIN_WAIT1V=$(grep FIN_WAIT1 $tcpfile | awk '{print $2}')
    local FIN_WAIT2V=$(grep FIN_WAIT2 $tcpfile | awk '{print $2}')
    local ESTABLISHEDV=$(grep ESTABLISHED $tcpfile | awk '{print $2}')
    local SYN_RECVV=$(grep SYN_RECV $tcpfile | awk '{print $2}')
    local CLOSINGV=$(grep CLOSING $tcpfile | awk '{print $2}')
    local CLOSE_WAITV=$(grep CLOSE_WAIT $tcpfile | awk '{print $2}')
    local LAST_ACKV=$(grep LAST_ACK $tcpfile | awk '{print $2}')
    echo
    echo "########## Tcp connections ##############"
    echo "TIME_WAIT   CONNECTIONS  $TIME_WAITV "
    echo "FIN_WAIT1   CONNECTIONS  $FIN_WAIT1V "
    echo "FIN_WAIT2   CONNECTIONS  $FIN_WAIT2V "
    echo "CLOSE_WAIT  CONNECTIONS  $CLOSE_WAITV "
    echo "ESTABLISHED CONNECTIONS  $ESTABLISHEDV "
    echo "SYN_RECV    CONNECTIONS  $SYN_RECVV "
    echo "LAST_ACKV   CONNECTIONS  $LAST_ACKV "
    echo "CLOSING     CONNECTIONS  $CLOSINGV "

}
check_license() {
    license -f /var/cfg/license/license.conf >/dev/null 2>&1
    if [ $? != 0 ]; then
        echo "ERROR: The license is invalid or expired!"
        check_result="false"
    fi
}

collect_log() {
    if [ "$check_result" == "true" ]; then
        echo
        echo
        echo "Check successful!"
        exit 0
    fi
    echo
    echo
    echo "Check Failed:"
    read -p "Please enter the name of the test result file:" project_name

    mkdir -p /store/$TIMESTAMP/RMS_LOG /store/$TIMESTAMP/PPS_LOG /store/$TIMESTAMP/TRP_LOG
    cp /var/log/nsag/RMS.log* /store/$TIMESTAMP/RMS_LOG
    cp /var/log/nsag/PPS.log* /store/$TIMESTAMP/PPS_LOG
    cp /var/log/nsag/TRP/error.log* /store/$TIMESTAMP/TRP_LOG
    cp /var/log/nsag/TRP/*manager.log* /store/$TIMESTAMP/TRP_LOG
    tar -zcvPf ./"${project_name}"_nsag_check_result.tar.gz /store/$TIMESTAMP/ >/dev/null
    echo "Check Failed, The result file : [ ./${project_name}_nsag_check_result.tar.gz ]"
    rm -rf /store/$TIMESTAMP/
}

check_result="true"
TIMESTAMP=$(date +%y%m%d_%H%M_%S)
main() {
    print_linux_info
    check_current_user
    check_base_server
    check_all_mem
    check_all_cpu
    check_error_from_log
    check_disk
    check_tcp_connect
    check_license
    collect_log
}
mkdir -p /store/"$TIMESTAMP"/
touch /store/"$TIMESTAMP"/check_result.log
main 2>&1 | tee /store/"$TIMESTAMP"/check_result.log