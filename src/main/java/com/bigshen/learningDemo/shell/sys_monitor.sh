#!/bin/bash

source "./modify_system_variable.sh"

##0、校验license
check_license() {
    curl -s http://127.0.0.1:60200/sys/license | grep \"isValid\":true >/dev/null && rc=$? || rc=$?
    if [ $rc -ne 0 ]; then
        echo "ERROR: The license is invalid or expired! "
    fi
}
##0、检查核心服务启动状态
check_service_status() {
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
    for service in $SERVICES; do
        serviceStatus=$(docker inspect --format='{{.State.Running}}' $service)
        if [ $serviceStatus == "true" ]; then
            echo "$service service ready!"
        else
            echo "$service service failed!"
        fi
    done
}

check_current_user() {
    if [ $(id -u) -ne 0 ]; then
        echo "ERROR: The current user is not root，Exit the script"
        check_result="false"
    fi
}

#1、系统基本信息
system_info() {
    echo -e "\033[32m############# 系统时间 #############\033[0m"
    echo System_Now_Time: $(date "+%Y-%m-%d %H:%M:%S")
    #获取系统时间也可以使用who -b
    echo System_Boot_Time: $(date -d "$(awk -F. '{print $1}' /proc/uptime) second ago" +"%Y-%m-%d %H:%M:%S")
    echo -e "\033[32m############ 主机名 ###############\033[0m"
    echo Hostname: $(hostname)
    echo -e "\033[32m########## Ip地址和网卡流量 ##########\033[0m"

    #获取服务器的网卡和IP地址，以便在网卡流量监控时使用。
    for Network in $(ifconfig | awk -F : '/^[[:alpha:]]/{print $1}'); do
        FLOWA=/tmp/.$Network
        grep $Network: /proc/net/dev | awk -F: '{print $2}' | awk '{print "INPUT "  $1 " "  "OUTPUT " $9}' >$FLOWA
        #第一次获取网卡流量，单位字节
        INPUTA=$(awk '{print $2}' $FLOWA)
        OUTPUTA=$(awk '{print $4}' $FLOWA)
        #获取网卡流量间隔时间
        sleep $NETTIME
        #第二次获取网卡流量，单位字节
        grep $Network: /proc/net/dev | awk -F: '{print $2}' | awk '{print "INPUT "  $1 " "  "OUTPUT " $9}' >$FLOWA
        INPUTB=$(awk '{print $2}' $FLOWA)
        OUTPUTB=$(awk '{print $4}' $FLOWA)
        #计算
        #转换网卡流量单位为KB
        INPUTC=$(echo -e "$INPUTB-$INPUTA" | bc)
        OUTPUTC=$(echo -e "$OUTPUTB-$OUTPUTA" | bc)
        INPUTMBA=$(echo -e $INPUTC | awk '{printf "%0.3f\n",$1/1024}')
        OUTPUTMBA=$(echo -e $OUTPUTC | awk '{printf "%0.3f\n",$1/1024}')
        INPUT=$(echo -e $INPUTC | awk '{printf "%0.3f\n",$1/1024/1024}')
        OUTPUT=$(echo -e $OUTPUTB | awk '{printf "%0.3f\n",$1/1024/1204}')
        #判断网卡流量使用量
        if [ $INPUTC -le 1048576 ]; then
            if [ $OUTPUTC -le 1048576 ]; then
                echo Network_Card: $Network Ip: $(/sbin/ifconfig $Network | grep "inet " | awk '{print $2}') "输入流量: " ${INPUTMBA}K/S "输出流量: " "${OUTPUTMBA}K/S "
            else
                echo Network_Card: $Network Ip: $(/sbin/ifconfig $Network | grep "inet " | awk '{print $2}') "输入流量: " ${INPUTMBA}K/S "输出流量: " "${OUTPUT}M/S "
            fi
        elif [ $INPUTC -gt 1048576 ]; then
            if [ $OUTPUTC -gt 1048576 ]; then
                echo Network_Card: $Network Ip: $(/sbin/ifconfig $Network | grep "inet " | awk '{print $2}') "输入流量: " ${INPUT}M/S "输出流量: " "${OUTPUTMBA}K/S "
            else
                echo Network_Card: $Network Ip: $(/sbin/ifconfig $Network | grep "inet " | awk '{print $2}') "输入流量: " ${INPUT}M/S "输出流量:" "${OUTPUT}M/S "
            fi
        fi
    done
}

#2、CPU监控
monitor_cpu() {
    echo -e "\033[32m############### CPU平均负载和系统进程数 ##############\033[0m"
    #us(user time)：用户进程执行消耗cpu时间；sy(system time)：系统进程执行消耗cpu时间；id：空闲时间（包括IO等待时间）；wa：等待IO时间。
    #CPULIMIT=90
    CPU_us=$(vmstat | awk '{print $13}' | sed -n '$p')
    CPU_sy=$(vmstat | awk '{print $14}' | sed -n '$p')
    CPU_id=$(vmstat | awk '{print $15}' | sed -n '$p')
    CPU_wa=$(vmstat | awk '{print $16}' | sed -n '$p')
    CPU_st=$(vmstat | awk '{print $17}' | sed -n '$p')
    #计算服务器CPU使用率
    CPU1=$(cat /proc/stat | grep 'cpu ' | awk '{print $2" "$3" "$4" "$5" "$6" "$7" "$8}')
    sleep $CPUTIME
    CPU2=$(cat /proc/stat | grep 'cpu ' | awk '{print $2" "$3" "$4" "$5" "$6" "$7" "$8}')
    IDLE1=$(echo -e $CPU1 | awk '{print $4}')
    IDLE2=$(echo -e $CPU2 | awk '{print $4}')
    CPU1_TOTAL=$(echo -e $CPU1 | awk '{print $1+$2+$3+$4+$5+$6+$7}')
    CPU2_TOTAL=$(echo -e $CPU2 | awk '{print $1+$2+$3+$4+$5+$6+$7}')
    IDLE=$(echo -e "$IDLE2-$IDLE1" | bc)
    CPU_TOTAL=$(echo -e "$CPU2_TOTAL-$CPU1_TOTAL" | bc)
    RATE=$(echo -e "scale=4;($CPU_TOTAL-$IDLE)/$CPU_TOTAL*100" | bc | awk '{printf "%.2f",$1}')

    echo "用户进程占用CPU时间: us=$CPU_us"
    echo "系统进程消耗CPU时间: sy=$CPU_sy"
    echo "CPU空闲时间:         id=$CPU_id"
    echo "等待I/O时间:         wa=$CPU_wa"
    echo "CPU使用率:           ${RATE}%"
    CPU_RATE=$(echo -e $RATE | cut -d. -f1)
    load_average=$(uptime | gawk -F':' '{print $NF}')
    running_process=$(top -b -d 1 -n 1 | sed -n '2p' | awk -F',' '{print $2}' | awk '{print $1}')
    #running_process=`ps aux |wc -l`
    total_process=$(ps -ef | wc -l)
    echo
    echo "CPU_load_average: ${load_average}"
    echo "running_process:""    " ${running_process}
    echo "total_process:""      " ${total_process}

    Host_running_time=$(uptime | sed 's/^.*up//' | awk -F "," '{print $1}')
    User_connection_number=$(uptime | cut -d , -f 3 | awk '{print $1}')
    #cat /proc/uptime| awk -F. '{run_days=$1 / 86400;run_hour=($1 % 86400)/3600;run_minute=($1 % 3600)/60;run_second=$1 % 60;printf("系统已运行：%d天%d时%d分%d秒\n",run_days,run_hour,run_minute,run_second)}'
    echo -e "Host_running_time:    $(cat /proc/uptime | awk -F. '{run_days=$1 / 86400;run_hour=($1 % 86400)/3600;run_minute=($1 % 3600)/60;run_second=$1 % 60;printf("%d天%d时%d分%d秒\n",run_days,run_hour,run_minute,run_second)}')"
    echo -e "User_connection_number:    ${User_connection_number}\n"
    #CPU占用最多的十个进程
    CPUA=$(expr $RATE \> $CPULIMIT)
    if [ $CPUA -eq 1 ]; then
        echo "$time1 CPU警告:当前CPU使用率$RATE%,大于$CPULIMIT%"
        # shellcheck disable=SC1101
        echo \
        echo "process使用CPU情况:"
        ps aux | sort -k3nr | head | awk 'BEGIN{printf "%-10s%-10s%-10s%-10s\n","USER","PID","%CPU","COMMAND"}{printf "%-10s%-10d%-10.1f%-10s\n",$1,$2,$3,$11}'
    fi
    echo
}

#3、内存监控
monitor_mem() {
    ####################################################################
    echo -e "\033[32m########### 系统内存使用情况 ##############\033[0m"
    #内存使用情况
    #MEMLIMIT=80
    Total_Mem=$(free -m | sed -n '2p' | awk '{print $2}')
    Usage_Mem=$(free -m | sed -n '2p' | awk '{print $3}')
    Free_Mem=$(free -m | sed -n '2p' | awk '{print $4}')
    Mem_Usage_Percent=$(free -m | sed -n '2p' | awk '{printf "%-1d",$3/$2*100}')
    #交换分区使用情况
    Swap_Total_Mem=$(free -m | grep Swap | sed -n 'p' | awk '{print $2}')
    Swap_Usage_Mem=$(free -m | grep Swap | sed -n 'p' | awk '{print $3}')
    Swap_Free_Mem=$(free -m | grep Swap | sed -n 'p' | awk '{print $4}')
    Swap_Mem_Usage_Percent=$(free -m | grep Swap | sed -n 'p' | awk '{printf "%-1d",$3/$2*100}')
    echo "总内存:"${Total_Mem}M
    echo "使用内存: ${Usage_Mem}M"
    echo "剩余内存: ${Free_Mem}M"
    echo -e "内存使用率: ${Mem_Usage_Percent}%\n"
    echo "交换分区使用情况:"
    echo "总内存: ${Swap_Total_Mem}M"
    echo "使用内存: ${Swap_Usage_Mem}M"
    echo "剩余内存: ${Swap_Free_Mem}M"
    echo -e "使用率: ${Swap_Mem_Usage_Percent}%\n"
    #判断内存使用是否超过80%
    MA=$(expr $Mem_Usage_Percent \> $MEMLIMIT)
    if [ $MA -eq 1 ]; then
        echo "$time1 内存警告,当前内存使用$Mem_Usage_Percent% 大于$MEMLIMIT%"
    fi

    MB=$(expr $Swap_Mem_Usage_Percent \> $MEMLIMIT)
    if [ $MB -eq 1 ]; then
        echo "$time1 内存警告,当前内存$Swap_Mem_Usage_Percent% 大于$MEMLIMIT%"
    fi
    echo "process使用内存情况:"
    ps aux | sort -k4nr | head | awk 'BEGIN{printf "%-10s%-10s%-10s%-10s\n","USER","PID","%MEM","COMMAND"}{printf "%-10s%-10d%-10.1f%-10s\n",$1,$2,$4,$11}'
    #PCPID=`ps aux |sort -k4nr |head |awk 'BEGIN{printf "%-10s%-10s%-10s%-10s\n","USER","PID","%MEM","COMMAND"}{printf "%-10s%-10d%-10.1f%-10s\n",$1,$2,$4,$11}' |grep -v -i user |awk '{print $2}'`
    #PDMEM=`ps aux |sort -k4nr |head |awk 'BEGIN{printf "%-10s%-10s%-10s%-10s\n","USER","PID","%MEM","COMMAND"}{printf "%-10s%-10d%-10.1f%-10s\n",$1,$2,$4,$11}' |grep -v -i user |awk '{print $3}'`
    #for i in $PCPID
    #do
    #    echo $i;ps -ef |grep $i |awk '{print $NF}' |head -1
    #done
    echo
}

#4、磁盘监控
monitor_disk() {
    #################################################################
    #ROOT=80
    #VAR=90
    #HOME=90
    #BOOT=80
    #DATA=90
    NULL=/dev/null
    echo -e "\033[32m############### 磁盘使用情况 ################\033[0m"
    disk=/tmp/.disk
    #parted适用于Ubuntu
    #parted -l |grep -i 'disk' |grep '/dev/[a-z]d[a-z]' |awk -F: '{print $1}' |awk '{print $2}' > /tmp/.disknumber
    #/sbin/fdisk -l |grep -i 'disk' |grep '/dev/[a-z]d[a-z]' |awk -F: '{print $1}' |awk '{print $2}' > /tmp/.disknumber
    disk_data_usage=$(df -h | grep "data$" | awk '{print $5}' | awk -F% '{print $1}')
    disk_root_usage=$(df -h | grep "/$" | awk '{print $5}' | awk -F% '{print $1}')
    disk_home_usage=$(df -h | grep "home$" | awk '{print $5}' | awk -F% '{print $1}')
    disk_var_usage=$(df -h /var/log/ | awk '{print $5}' | awk -F% '{print $1}' | awk 'NR==2{print}')
    disk_boot_usage=$(df -h | grep "boot" | awk '{print $5}' | awk -F% '{print $1}')
    disk_data2_usage=$(df -h | grep "data2" | awk '{print $5}' | awk -F% '{print $1}')
    disk_data3_usage=$(df -h | grep "data3" | awk '{print $5}' | awk -F% '{print $1}')
    disk_data4_usage=$(df -h | grep "data4" | awk '{print $5}' | awk -F% '{print $1}')
    disk_data5_usage=$(df -h | grep "data5" | awk '{print $5}' | awk -F% '{print $1}')
    #获取磁盘分区使用情况
    df -h | awk '{print $NF}' >/tmp/.disk
    grep home $disk >$NULL
    if [ $? -eq 0 ]; then
        if [ $disk_home_usage -ge $HOME ]; then
            echo "    "/home分区警告:当前使用$disk_home_usage%,大于$HOME
        else
            echo "    "当前/home分区使用:"         "$disk_home_usage%
        fi
    fi
    grep "/$" $disk >$NULL
    if [ $? -eq 0 ]; then
        if [ $disk_root_usage -ge $ROOT ]; then
            echo "    "/分区警告:当前使用$disk_root_usage%,大于$ROOT
        else
            echo "    "当前/分区使用:"         "$disk_root_usage%
        fi

    fi
    grep "var" $disk >$NULL
    if [ $? -eq 0 ]; then
        if [ $disk_var_usage -ge $VAR ]; then
            echo "    "/var/log分区警告:当前使用$disk_var_usage%,大于$VAR
        else
            echo "    "当前/var/log分区使用:"         "$disk_var_usage%
        fi

    fi
    grep "boot" $disk >$NULL
    if [ $? -eq 0 ]; then
        if [ $disk_boot_usage -ge $BOOT ]; then
            echo "    "/boot分区警告:当前使用$disk_boot_usage%，大于$BOOT
        else
            echo "    "当前/boot分区使用:"         "$disk_boot_usage%
        fi
    fi
    grep "data" $disk >$NULL
    if [ $? -eq 0 ]; then
        if [ $disk_data_usage -ge $DATA ]; then
            echo "    "/data分区警告:当前使用$disk_data_usage%，大于$DATA
        else
            echo "    "当前/data分区使用:"         "$disk_data_usage%
        fi
    fi
    grep "data2" $disk >$NULL
    if [ $? -eq 0 ]; then
        if [ $disk_data2_usage -ge $DATA ]; then
            echo "    "/data2分区警告:当前使用$disk_data2_usage%，大于$DATA
        else
            echo "    "当前/data2分区使用:"         "$disk_data2_usage%
        fi
    fi
    grep "data3" $disk >$NULL
    if [ $? -eq 0 ]; then
        if [ $disk_data3_usage -ge $DATA ]; then
            echo "    "/data3分区警告:当前使用$disk_data3_usage%，大于$DATA
        else
            echo "    "���前/data3分区使用:"         "$disk_data3_usage%
        fi
    fi
    grep "data4" $disk >$NULL
    if [ $? -eq 0 ]; then
        if [ $disk_data4_usage -ge $DATA ]; then
            echo "    "/data4分区警告:当前使用$disk_data4_usage%，大于$DATA
        else
            echo "    "当前/data4分区使用:"         "$disk_data4_usage%
        fi
    fi
    grep "data5" $disk >$NULL
    if [ $? -eq 0 ]; then
        if [ $disk_data5_usage -ge $DATA ]; then
            echo "    "/data5分区警告:当前使用$disk_data5_usage%，大于$DATA
        else
            echo "    "当前/data5分区使用:"         "$disk_data5_usage%
        fi
    fi
    echo
}

#5、连接数
monitor_connect_count() {
    tcpfile=/tmp/.tcp
    #查看并发连接数
    #描述
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

    TIME_WAITV=$(grep TIME_WAIT $tcpfile | awk '{print $2}')
    FIN_WAIT1V=$(grep FIN_WAIT1 $tcpfile | awk '{print $2}')
    FIN_WAIT2V=$(grep FIN_WAIT2 $tcpfile | awk '{print $2}')
    ESTABLISHEDV=$(grep ESTABLISHED $tcpfile | awk '{print $2}')
    SYN_RECVV=$(grep SYN_RECV $tcpfile | awk '{print $2}')
    CLOSINGV=$(grep CLOSING $tcpfile | awk '{print $2}')
    CLOSE_WAITV=$(grep CLOSE_WAIT $tcpfile | awk '{print $2}')
    LAST_ACKV=$(grep LAST_ACK $tcpfile | awk '{print $2}')
    echo -e "\033[32m###########Tcp连接数##################\033[0m"
    echo "    "当前TIME_WAIT"  " 连接数为 $TIME_WAITV 个
    echo "    "当前FIN_WAIT1"  " 连接数为 $FIN_WAIT1V 个
    echo "    "当前FIN_WAIT2"  " 连接数为 $FIN_WAIT2V 个
    echo "    "当前CLOSE_WAIT" " 连接数为 $CLOSE_WAITV 个
    echo "    "当前ESTABLISHED"" 连接数为 $ESTABLISHEDV 个
    echo "    "当前SYN_RECV"   " 连接数为 $SYN_RECVV 个
    echo "    "当前LAST_ACKV"  " 连接数为 $LAST_ACKV 个
    echo "    "当前CLOSING"    " 连接数为 $CLOSINGV 个
    echo \
    echo -e "\033[31m ----------------------------END--------------------------\033[0m"
}

get_docker_container_cpu_mem() {
    docker stats --no-stream --format "table {{.Name}}\t{{.CPUPerc}}\t{{.MemPerc}}"
}

check_result="true"
TIMESTAMP=$(date +%y%m%d_%H%M_%S)
main() {
    check_license
    check_current_user
    check_service_status
    system_info
    monitor_cpu
    monitor_mem
    monitor_disk
    monitor_connect_count
}
mkdir -p /store/"$TIMESTAMP"/
touch /store/"$TIMESTAMP"/check_result.log
main 2>&1 | tee /store/"$TIMESTAMP"/check_result.log
