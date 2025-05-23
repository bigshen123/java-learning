package com.bigshen.springbootDemo.util;

import com.bigshen.springbootDemo.service.RedissonService;
import com.bigshen.springbootDemo.service.impl.RedissonServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.HashMap;
import java.util.Map;

/**
 * @author byj
 * @date 2025/5/20
 * @Description
 */
public class RedissonDemo {

    // 模拟注入 RedissonDemoService（你实际应从 Spring 或构造方法获取）
    private static RedissonService redissonService = null;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        // 1. 配置 Redisson
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        // 2. 创建 RedissonClient
        RedissonClient redissonClient = Redisson.create(config);
        try {
            redissonService = new RedissonServiceImpl(redissonClient);
            System.out.println("Redis ping: " + redissonClient.getKeys().count());

            testBasicKV();
            testHash();
            testSet();
            testSortedSet();
            testList();
            testScan();
            testBatchInsert();
        } finally {
            // 关闭 RedissonClient
            redissonClient.shutdown();
        }


    }

    private static void testBasicKV() {
        System.out.println("======= 基本 KV 测试 =======");
        redissonService.set("test:key", "Hello Redis", 60);
        String value = redissonService.get("test:key");
        System.out.println("获取值：" + value);
        System.out.println("是否存在：" + redissonService.exists("test:key"));
        System.out.println("过期时间：" + redissonService.getExpire("test:key") + "秒");
        redissonService.delete("test:key");
    }

    private static void testHash() {
        System.out.println("\n======= Hash 测试 =======");
        redissonService.hset("test:hash", "field1", "value1", 120);
        redissonService.hset("test:hash", "field2", 12345);
        System.out.println("field1: " + redissonService.hget("test:hash", "field1"));
        System.out.println("全部字段：" + redissonService.hgetAll("test:hash"));
        redissonService.hdel("test:hash", "field1");
    }

    private static void testSet() {
        System.out.println("\n======= Set 测试 =======");
        redissonService.sadd("test:set", "a", "b", "c");
        System.out.println("所有成员：" + redissonService.smembers("test:set"));
        System.out.println("是否包含 'a'：" + redissonService.sismember("test:set", "a"));
        System.out.println("集合大小：" + redissonService.scard("test:set"));
    }

    private static void testSortedSet() {
        System.out.println("\n======= Sorted Set 测试 =======");
        redissonService.zadd("test:zset", "user1", 10);
        redissonService.zadd("test:zset", "user2", 20, 300);
        System.out.println("user1 分数：" + redissonService.zscore("test:zset", "user1"));
        System.out.println("成员数：" + redissonService.zcard("test:zset"));
        redissonService.zrem("test:zset", "user1");
    }

    private static void testList() {
        System.out.println("\n======= List 测试 =======");
        redissonService.lpush("test:list", "a");
        redissonService.rpush("test:list", "b");
        System.out.println("左弹出：" + redissonService.lpop("test:list"));
        System.out.println("右弹出：" + redissonService.rpop("test:list"));
        redissonService.rpush("test:list", "x");
        redissonService.rpush("test:list", "y");
        redissonService.rpush("test:list", "z");
        System.out.println("范围获取：" + redissonService.lrange("test:list", 0, -1));
    }

    private static void testScan() {
        System.out.println("\n======= Scan 测试 =======");
        redissonService.scan("test:*", key -> System.out.println("扫描到 Key: " + key));
    }

    private static void testBatchInsert() {
        System.out.println("\n======= 批量插入测试 =======");
        Map<String, Object> batchData = new HashMap<>();
        batchData.put("batch:key1", "val1");
        batchData.put("batch:key2", 999);
        batchData.put("batch:key3", true);
        redissonService.batchInsert(batchData);

        System.out.println("获取 batch:key1：" + redissonService.get("batch:key1"));
    }
}
