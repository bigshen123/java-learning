## redis 主从搭建

- 主从切换的脚本，实现主从自动切换（依赖的keepalived 检测）


[C10redis_ha_check.sh](C10redis_ha_check.sh) 为检测脚本，检测redis的监控状态

[N10redis_ha_notify.sh](N10redis_ha_notify.sh) 为主从切换脚本，根据keepalived 检测脚本来确定切master还是backup




