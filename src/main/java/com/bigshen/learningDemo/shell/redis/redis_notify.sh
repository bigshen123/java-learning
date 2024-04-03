#!/bin/bash
## 此脚本用于解散集群时，将redis从机设置为主机

LOG() {
    echo "REDIS_HA: $1"
    logger -t REDIS_HA -p user.info "$1"
}

STATE=$1 #状态
REMOTEIP=$2 #对端ip

change_master(){
	LOG "Start change role to master... " 
	LOG "Run replicaof on one"
    docker exec -i redis redis-cli -p 64000 <<EOF
auth kl123456
config set masterauth kl123456
REPLICAOF NO ONE
EOF
    if [ $? == 0 ]; then
       LOG "Change role to master success...."
    else
       LOG "Change role to master failed...."
    fi
}


change_backup(){
	LOG "Start change role to slave... " 
    LOG "Run replicaof $REMOTEIP 64000"
    docker exec -i redis redis-cli -p 64000 <<EOF
auth kl123456
config set masterauth kl123456
replicaof $REMOTEIP  64000
EOF
    if [ $? == 0 ]; then
       LOG "Change role to slave success...." 
    else
       LOG "Change role to slave failed...." 
    fi
}


if [ "$STATE" == "master" ];then
	change_master
elif [ "$STATE" == "backup" ];then
	change_backup
elif [ "$STATE" == "fault" ];then
	change_backup
fi	
