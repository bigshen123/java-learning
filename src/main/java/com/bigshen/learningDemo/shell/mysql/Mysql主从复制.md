# M-S主从架构模拟（传统方式部署）

## Mysql 主从复制原理
复制过程：
1、开启binlog日志，通过把主库的binlog传送到从库，从新解析应用到从库。
2、复制需要3个线程（dump、io、sql）完成，5.6从库多个sql。
3、复制是异步的过程。主从复制是异步的逻辑的SQL语句级的复制。

复制前提：
1、主服务器一定要打开二进制日志（查看binlog日志 show variables like 'log_%';  ON开启状态，OFF关闭状态）
2、必须两台服务器（或者是多个实例起两个端口）
3、从服务器需要一次数据初始化（表结构需要一致）
3.1如果主从服务器都是新搭建的话，可以不做初始化
3.2如果主服务器已经运行了很长时间了，可以通过物理备份将主库数据恢复到从库。
4、主库必须要有对从库复制请求的用户。
5、从库需要有relay-log设置，存放从主库传送过来的二进制日志 show variables  like '%relay%';
6、在第一次的时候，从库需要change master to 去连接主库。
7、change master信息需要存放到master.info中  show variables  like '%master_info%';
8、从库怎么知道，主库发生了新的变化?通过relay-log.info记录的已经应用过的relay-log信息。
9、在复制过程中涉及到的线程
从库会开启一个IO thread(线程)，负责连接主库，请求binlog，接收binlog并写入relay-log。
从库会开启一个SQL thread(线程)，负责执行relay-log中的事件。
主库会开启一个dump thrad(线程)，负责响应从IO thread的请求。


## 主库上的操作流程：
1、开启log-bin日志，设置servier-id=1,重启数据库服务
2、主库上创建复制用户：grant replication
3、初始化数据库，将主库数据迁移到备库

[root@master1 ~]# grep -Ev '^#|^$' /etc/my.cnf
[mysqld]
datadir=/var/lib/mysql
socket=/var/lib/mysql/mysql.sock
symbolic-links=0
log-bin=/var/log/mariadb/mariadb-bin    # 启用二进制日志，并设置日志位置（自己的位置）
server-id=1                            # 主备server-id 不能相同,只要主从不一样就行
[mysqld_safe]
log-error=/var/log/mariadb/mariadb.log
pid-file=/var/run/mariadb/mariadb.pid
!includedir /etc/my.cnf.d

## 主库上创建复制用户：
```mariadb
-- 给一个 MySQL 用户（mslave）授予在所有数据库上进行复制和复制客户端操作的权限，
-- 并且限制该用户只能从 IP 地址为 192.168.3.89 的主机连接，并且设置了密码为 '123qwe'
grant replication slave,replication client on *.* to mslave@'192.168.3.89' identified by '123qwe';
-- 确保权限更改立即生效。
flush privileges;
```


## 测试备库是否可以使用远程用户（注意关闭防火墙）
[root@mysql-slave1 mysql]# mysql -hmaster -umslave -p'123qwe'
mysql> show grants;
+------------------------------------------------------------------------------------------------------------------------------------+
| Grants for rep@192.168.3.%                                                                                                                   |
+------------------------------------------------------------------------------------------------------------------------------------+
| GRANT REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'rep'@'%' IDENTIFIED BY PASSWORD '*E56A114692FE0DE073F9A1DD68A00EEB9703F3F1' |
+------------------------------------------------------------------------------------------------------------------------------------+

## 备库相关操作
备库上进行的操作
1、配置my.cnf中server-id=2
2、初始化数据库，将主库迁移的数据导入从库
3、mysql > change master to master_host='master', master_user='mslave', master_password='123qwe', master_log_file='xxx', master_log_pos=xxx;
4、mysql > start slave; # 启动slave角色
5、查看从库状态 show slave status;
# 1、配置备库my.cnf
[root@mysql-slave1 mysql]# grep -Ev '^$|^#' /etc/my.cnf
[mysqld]
datadir=/var/lib/mysql
socket=/var/lib/mysql/mysql.sock
log-bin=/var/log/mariadb/mariadb-bin    # 启用二进制日志   
server-id=2                             # 主备server-id 不能相同即可
symbolic-links=0
log-error=/var/log/mariadb/mysqld.log
pid-file=/var/run/mysqld/mysqld.pid
[root@mysql-slave1 mysql]#

# 2、初始化数据库，将主库迁移的数据导入从库
具体看怎么备份的数据库
例：
[root@master1 ~]# mysqldump  -p123qwe -A -R -F --single-transaction --triggers --master-data=1 > `date +%F`-mysql-full.sql

备库同步数据：mysql> set sql_log_bin=0;
mysql> source 2020-04-01-mysql-full.sql
》注：这种方法同步的数据可以不用下下面的maser-log-file,master-log-pos，
前提是用source方式导入且进行逻辑备份时指定了master-data=1

# 3、从库设置同步日志位置
mysql> CHANGE MASTER TO master_host='master',master_user='mslave',
> master_password='Edmund@110',
> master_port=3306,
> master_log_file='mariadb-bin.000001',
> master_log_pos=478;

注：如果数据数通过mysqldump的且利用source xxx.sql读取备份的话可以不写master_log_file与master_log_pos，其他情况都要写

# 4、从库上启动同步，并且可以查看同步状态
mysql> start slave;         
mysql> show slave status\G;
...         Slave_IO_Running: Yes
Slave_SQL_Running: Yes
...
- 可以看到：Slave_IO_Running | Slave_SQL_Running两个值都是YES，说明配置成功了。


## 测试主备同步
参考文档中的用例[Mysql主从高可用配置.doc](Mysql%E4%B8%BB%E4%BB%8E%E9%AB%98%E5%8F%AF%E7%94%A8%E9%85%8D%E7%BD%AE.doc)











