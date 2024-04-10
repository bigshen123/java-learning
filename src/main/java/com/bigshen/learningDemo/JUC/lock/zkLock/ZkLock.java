package com.bigshen.learningDemo.JUC.lock.zkLock;

/**
 * @ClassName ZKLock
 * @Description:TODO
 * @Author: byj
 * @Date: 2020/12/11
 */
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 分布式锁Zookeeper实现
 *
 */
@Slf4j
@Component
public class ZkLock  {
    private String zkAddress = "zk_adress";
    private static final String root = "package root";
    private CuratorFramework zkClient;

    private final String LOCK_PREFIX = "/lock_";

    @Bean
    public void initZkLock() {
        if (StringUtils.isBlank(root)) {
            throw new RuntimeException("zookeeper 'root' can't be null");
        }
        zkClient = CuratorFrameworkFactory
                .builder()
                .connectString(zkAddress)
                .retryPolicy(new RetryNTimes(2000, 20000))
                .namespace(root)
                .build();
        zkClient.start();
    }

    public boolean tryLock(String lockName) {
        lockName = LOCK_PREFIX+lockName;
        boolean locked = true;
        try {
            Stat stat = zkClient.checkExists().forPath(lockName);
            if (stat == null) {
                log.info("tryLock:{}", lockName);
                stat = zkClient.checkExists().forPath(lockName);
                if (stat == null) {
                    zkClient
                            .create()
                            .creatingParentsIfNeeded()
                            .withMode(CreateMode.EPHEMERAL)
                            .forPath(lockName, "1".getBytes());
                } else {
                    log.warn("double-check stat.version:{}", stat.getAversion());
                    locked = false;
                }
            } else {
                log.warn("check stat.version:{}", stat.getAversion());
                locked = false;
            }
        } catch (Exception e) {
            locked = false;
        }
        return locked;
    }

    public boolean tryLock(String key, long timeout) {
        return false;
    }

    public void release(String lockName) {
        lockName = LOCK_PREFIX+lockName;
        try {
            zkClient
                    .delete()
                    .guaranteed()
                    .deletingChildrenIfNeeded()
                    .forPath(lockName);
            log.info("release:{}", lockName);
        } catch (Exception e) {
            log.error("删除", e);
        }
    }

    public void setZkAddress(String zkAddress) {
        this.zkAddress = zkAddress;
    }
}