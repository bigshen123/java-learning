package com.bigshen.springbootDemo.service;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author byj
 * @date 2025/5/20
 * @Description
 */
public interface RedissonDemoService {

    // ======================== 基本KV操作 ========================

    /**
     * 设置指定 key 对应的值（不过期）。
     */
    void set(String key, Object value);

    /**
     * 设置指定 key 对应的值，并设置过期时间（单位：秒）。
     */
    void set(String key, Object value, long expireSeconds);

    /**
     * 获取指定 key 的值，并反序列化为指定类型。
     */
    <T> T get(String key, Class<T> clazz);

    /**
     * 获取指定 key 的字符串值。
     */
    String get(String key);

    /**
     * 判断指定 key 是否存在。
     */
    boolean exists(String key);

    /**
     * 删除指定 key。
     */
    void delete(String key);

    /**
     * 批量删除多个 key。
     */
    Long delete(Collection<String> keys);

    /**
     * 设置指定 key 的过期时间（单位：秒）。
     */
    void expire(String key, long seconds);

    /**
     * 获取指定 key 剩余的过期时间（单位：秒）。
     */
    Long getExpire(String key);


    // ======================== Hash 操作 ========================

    /**
     * 向 Hash 中设置字段值（不过期）。
     */
    void hset(String hashKey, String field, Object value);

    /**
     * 向 Hash 中设置字段值，并设置过期时间（单位：秒）。
     */
    void hset(String hashKey, String field, Object value, long expireSeconds);

    /**
     * 获取 Hash 中指定字段的值。
     */
    Object hget(String hashKey, String field);

    /**
     * 获取 Hash 中所有字段和值。
     */
    Map<String, Object> hgetAll(String hashKey);

    /**
     * 删除 Hash 中的一个或多个字段。
     */
    void hdel(String hashKey, String... fields);

    /**
     * 设置 Hash 的过期时间（单位：秒）。
     */
    Boolean hexpire(String hashKey, long seconds);


    // ======================== Set 操作 ========================

    /**
     * 向 Set 添加一个或多个成员。
     */
    boolean sadd(String key, String... members);

    /**
     * 获取 Set 中的所有成员。
     */
    Set<String> smembers(String key);

    /**
     * 判断 Set 中是否包含指定成员。
     */
    Boolean sismember(String key, String member);

    /**
     * 获取 Set 中成员的数量。
     */
    Long scard(String key);


    // ======================== Sorted Set 操作 ========================

    /**
     * 向有序集合添加一个成员及其分数（不过期）。
     */
    Boolean zadd(String key, String member, double score);

    /**
     * 向有序集合添加一个成员及其分数，并设置过期时间（单位：秒）。
     */
    Boolean zadd(String key, String member, double score, long expireSeconds);

    /**
     * 获取指定成员的分数。
     */
    Double zscore(String key, String member);

    /**
     * 移除一个或多个成员。
     */
    boolean zrem(String key, String... members);

    /**
     * 获取有序集合的成员数量。
     */
    Long zcard(String key);


    // ======================== List 操作 ========================

    /**
     * 从左侧推入一个元素到列表中。
     */
    Long lpush(String key, String value);

    /**
     * 从右侧推入一个元素到列表中。
     */
    Long rpush(String key, String value);

    /**
     * 从左侧弹出一个元素。
     */
    String lpop(String key);

    /**
     * 从右侧弹出一个元素。
     */
    String rpop(String key);

    /**
     * 获取列表指定范围的元素。
     */
    List<String> lrange(String key, long start, long end);


    // ======================== 扫描操作 ========================

    /**
     * 使用 pattern 扫描匹配的 key，并对每个 key 执行给定的操作。
     */
    void scan(String pattern, Consumer<String> consumer);


    // ======================== 其他扩展方法 ========================

    /**
     * 仅在字段已存在时，更新 Hash 中的字段值。
     */
    Boolean hSetIfPresent(String hashKey, String field, String value);

    /**
     * 批量插入多个 key-value 键值对（简化版本）。
     */
    void batchInsert(Map<String, Object> data);


    // ======================== 分布式锁 ========================
    /**
     * 获取一个分布式锁对象
     */
    RLock getLock(String lockKey);

    /**
     * 获取锁，并在超时后释放（阻塞）
     */
    void lock(String lockKey, long leaseTime, TimeUnit unit);

    /**
     * 尝试获取锁（非阻塞）
     */
    boolean tryLock(String lockKey, long waitTime, long leaseTime, TimeUnit unit);

    /**
     * 解锁
     */
    void unlock(String lockKey);


    // ======================== 布隆过滤器（防止缓存穿透） ========================
    /**
     * 初始化布隆过滤器（预计插入量、误判率）
     */
    <T> RBloomFilter<T> initBloomFilter(String name, long expectedInsertions, double falseProbability);

    /**
     * 添加元素
     */
    <T> boolean addToBloomFilter(String name, T value);

    /**
     * 判断元素是否存在
     */
    <T> boolean mightContainInBloomFilter(String name, T value);


    // ======================== 限流器（令牌桶、固定窗口） ========================
    /**
     * 初始化限流器（令牌桶）
     */
    RRateLimiter initRateLimiter(String key, long rate, long interval, RateIntervalUnit unit);

    /**
     * 尝试获取 token
     */
    boolean tryAcquire(String key, long permits);


    // ======================== 消息发布订阅（Pub/Sub）========================
    /**
     * 发布消息
     */
    long publish(String topic, String message);

    /**
     * 订阅消息（异步处理）
     */
    void subscribe(String topic, Consumer<String> messageHandler);


    // ======================== 脚本执行（你提到的 getScript）========================
    /**
     * RScript script = redisson.getScript(StringCodec.INSTANCE);
     * 执行 Lua 脚本
     */
    <V> V eval(String script, List<String> keys, List<Object> args);



    /**
     * 向延迟队列添加一个元素
     */
    void addToDelayQueue(String queueName, String value, long delay, TimeUnit timeUnit);

    /**
     * 从延迟队列消费
     */
    void listenDelayQueue(String queueName, Consumer<String> messageHandler);


    // ======================== 地理位置 Geo（如附近门店）========================
    /**
     * 添加地理位置
     */
    void geoAdd(String key, double longitude, double latitude, String member);

    /**
     * 获取附近的元素
     */
    List<String> geoRadius(String key, double longitude, double latitude, double radiusMeters);


    // ======================== 可重入信号量 / 闭锁（用于分布式同步控制）========================

    /**
     * 初始化信号量
     */
    void initSemaphore(String key, int permits);

    /**
     * 获取许可
     */
    boolean acquireSemaphore(String key);

    /**
     * 释放许可
     */
    void releaseSemaphore(String key);

    // ======================== 可调任务调度（类似延时任务/CRON）========================
    /**
     * 设置一次性定时任务
     */
    void schedule(String taskName, Runnable task, long delay, TimeUnit unit);

    /**
     * 设置周期性任务
     */
    void scheduleAtFixedRate(String taskName, Runnable task, long initialDelay, long period, TimeUnit unit);

}

