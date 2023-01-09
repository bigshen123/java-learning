#!/bin/bash
#计算CPU利用率的时间间隔。
CPUTIME=3   # 单位 s
#CPU限制，当系统使用CPU达到下面值时报警。
CPULIMIT=60 #单位 %
#内存限制，当系统使用内存达到下面值时报警。
MEMLIMIT=80 #单位 %
#磁盘监控。

ROOT=80   # / 分区使用限制。单位 %
VAR=90    # var 分区使用限制。 单位 %
HOME=90   # home 分区使用限制。单位 %
BOOT=80   # boot 分区使用限制。 单位 %
DATA=90   # data1 分区使用限制。单位 %
#计算网卡流量时间间隔
NETTIME=3 #单位 s

CORE_SERVICES="rms pps trp vpn"
SERVICES="redis influxdb telegraf kms_test kms console ocsp kong psql"
