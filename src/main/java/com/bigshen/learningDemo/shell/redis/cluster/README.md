## redis Cluster 集群

- 使用了 6 台机器进行 Redis Cluster 集群搭建
- 3 主 3 从

## 修改的配置文件项解释
```shell
port 6379    ## 内部端口
bind 0.0.0.0  ## 允许所有机器访问
appendonly no  ## 不开启 aof 
cluster-enabled yes ##  开启集群
cluster-config-file nodes.conf  ## 容器启动后生成的集群配置文件
cluster-node-timeout 15000      ## 集群之间的
cluster-announce-ip ${HOST_IP}  # 指明IP 这里是我本地IP地址
cluster-announce-port 46379     ## 外部端口
cluster-announce-bus-port 56379  # 指明对外总线端口，默认都是 内部 port+10000, 所以内部的总线端口为 16379
```

## 搭建集群

```shell
## 1、清空环境
bash clear.sh
## 2、复制文件，执行脚本，需要修改 ${HOST_IP} 主机 IP 和 密码 ${REQUIREPASS}
bash create_node_conf.sh
## 3、修改 docker-compose.yml 的文件路径
## 4、启动
docker-compose up -d
## 5、组建集群(随便进入一台机器后执行), --cluster-replicas 1 意思是每台主机要有一台从机
redis-cli --cluster create  ${HOST_IP}:46381 ${HOST_IP}:46382 ${HOST_IP}:46383 ${HOST_IP}:46384 ${HOST_IP}:46385 ${HOST_IP}:46386 --cluster-replicas 1 -a ${REQUIREPASS}
```

## 常用命令

```shell
## https://github.com/menglikaibin/redis-cluster
## 查看帮助
cluster help
## 查看节点属性
cluster info
## 查看节点信息
cluster nodes
```



<!-- redis-cli --cluster create  10.0.200.135:46381 10.0.200.135:46382 10.0.200.135:46383 10.0.200.135:46384 10.0.200.135:46385 10.0.200.135:46386 --cluster-replicas 1 -a Wl123456 -->



