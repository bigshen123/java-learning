package com.bigshen.springbootDemo.service.impl;

import com.bigshen.springbootDemo.service.RedissonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.redisson.api.*;
import org.redisson.api.geo.GeoSearchArgs;
import org.redisson.api.listener.MessageListener;
import org.redisson.client.codec.StringCodec;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author byj
 * @date 2025/5/20
 * @Description
 */

@Service
public class RedissonServiceImpl implements RedissonService {

    private final RedissonClient redissonClient;
    private final ObjectMapper objectMapper;

    public RedissonServiceImpl(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void set(String key, Object value) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        bucket.set(value, 7, TimeUnit.DAYS);
    }

    @Override
    public void set(String key, Object value, long expireSeconds) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        bucket.set(value, expireSeconds, TimeUnit.SECONDS);
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        Object val = bucket.get();
        if (val == null) {
            return null;
        }
        if (clazz.isInstance(val)) {
            return clazz.cast(val);
        }
        try {
            // 尝试序列化转换
            String json = objectMapper.writeValueAsString(val);
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException("类型转换失败", e);
        }
    }

    @Override
    public String get(String key) {
        RBucket<String> bucket = redissonClient.getBucket(key);
        return bucket.get();
    }

    @Override
    public boolean exists(String key) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        return bucket.isExists();
    }

    @Override
    public void delete(String key) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        bucket.delete();
    }

    @Override
    public Long delete(Collection<String> keys) {
        return redissonClient.getKeys().delete(keys.toArray(new String[0]));
    }

    @Override
    public void expire(String key, long seconds) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        bucket.expire(seconds, TimeUnit.SECONDS);
    }

    @Override
    public Long getExpire(String key) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        return bucket.remainTimeToLive() / 1000; // 毫秒转秒
    }

    // Hash操作示例
    @Override
    public void hset(String hashKey, String field, Object value) {
        RMap<String, Object> map = redissonClient.getMap(hashKey);
        map.put(field, value);
    }

    @Override
    public void hset(String hashKey, String field, Object value, long expireSeconds) {
        hset(hashKey, field, value);
        hexpire(hashKey, expireSeconds);
    }

    @Override
    public Object hget(String hashKey, String field) {
        RMap<String, Object> map = redissonClient.getMap(hashKey);
        return map.get(field);
    }

    @Override
    public Map<String, Object> hgetAll(String hashKey) {
        RMap<String, Object> map = redissonClient.getMap(hashKey);
        return map.readAllMap();
    }

    @Override
    public void hdel(String hashKey, String... fields) {
        RMap<String, Object> map = redissonClient.getMap(hashKey);
        map.fastRemove(fields);
    }

    @Override
    public Boolean hexpire(String hashKey, long seconds) {
        RMap<String, Object> map = redissonClient.getMap(hashKey);
        return map.expire(seconds, TimeUnit.SECONDS);
    }

    // Set操作示例
    @Override
    public boolean sadd(String key, String... members) {
        RSet<String> set = redissonClient.getSet(key);
        return set.addAll(Arrays.asList(members));
    }

    @Override
    public Set<String> smembers(String key) {
        RSet<String> set = redissonClient.getSet(key);
        return set.readAll();
    }

    @Override
    public Boolean sismember(String key, String member) {
        RSet<String> set = redissonClient.getSet(key);
        return set.contains(member);
    }

    @Override
    public Long scard(String key) {
        RSet<String> set = redissonClient.getSet(key);
        return (long) set.size();
    }

    // Sorted Set 操作示例
    @Override
    public Boolean zadd(String key, String member, double score) {
        RScoredSortedSet<String> zset = redissonClient.getScoredSortedSet(key);
        return zset.add(score, member);
    }

    @Override
    public Boolean zadd(String key, String member, double score, long expireSeconds) {
        boolean added = zadd(key, member, score);
        if (added) {
            redissonClient.getBucket(key).expire(expireSeconds, TimeUnit.SECONDS);
        }
        return added;
    }

    @Override
    public Double zscore(String key, String member) {
        RScoredSortedSet<String> zset = redissonClient.getScoredSortedSet(key);
        return zset.getScore(member);
    }

    @Override
    public boolean zrem(String key, String... members) {
        RScoredSortedSet<String> zset = redissonClient.getScoredSortedSet(key);
        return zset.removeAll(Arrays.asList(members));
    }

    @Override
    public Long zcard(String key) {
        RScoredSortedSet<String> zset = redissonClient.getScoredSortedSet(key);
        return (long) zset.size();
    }

    // List 操作示例
    @Override
    public Long lpush(String key, String value) {
        RDeque<String> deque = redissonClient.getDeque(key);
        deque.addFirst(value);
        return (long) deque.size();
    }

    @Override
    public Long rpush(String key, String value) {
        RDeque<String> deque = redissonClient.getDeque(key);
        deque.addLast(value);
        return (long) deque.size();
    }

    @Override
    public String lpop(String key) {
        RDeque<String> deque = redissonClient.getDeque(key);
        return deque.pollFirst();
    }

    @Override
    public String rpop(String key) {
        RDeque<String> deque = redissonClient.getDeque(key);
        return deque.pollLast();
    }

    @Override
    public List<String> lrange(String key, long start, long end) {
        RList<String> list = redissonClient.getList(key);
        int size = list.size();
        if (size == 0) {
            return Collections.emptyList();
        }

        int fromIndex = (int) Math.max(start, 0);
        int toIndex = (end == -1) ? size : (int) (end + 1); // 包含 end
        toIndex = Math.min(toIndex, size); // 防止越界

        if (fromIndex >= toIndex) {
            return Collections.emptyList();
        }
        return list.subList(fromIndex, toIndex);
    }

    // Scan示例（简易实现）
    @Override
    public void scan(String pattern, Consumer<String> consumer) {
        Iterable<String> keys = redissonClient.getKeys().getKeysByPattern(pattern);
        for (String key : keys) {
            consumer.accept(key);
        }
    }

    @Override
    public Boolean hSetIfPresent(String hashKey, String field, String value) {
        RMap<String, String> map = redissonClient.getMap(hashKey);
        return map.replace(field, value) != null;
    }

    @Override
    public void batchInsert(Map<String, Object> data) {
        data.forEach(this::set);
    }

    @Override
    public RLock getLock(String key) {
        return redissonClient.getLock(key);
    }

    @Override
    public void lock(String key, long leaseTime, TimeUnit unit) {
        getLock(key).lock(leaseTime, unit);
    }

    @Override
    public boolean tryLock(String key, long waitTime, long leaseTime, TimeUnit unit) {
        try {
            return getLock(key).tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    @Override
    public void unlock(String key) {
        getLock(key).unlock();
    }

    @Override
    public <T> RBloomFilter<T> initBloomFilter(String key, long expectedInsertions, double falseProbability) {
        RBloomFilter<T> bloomFilter = redissonClient.getBloomFilter(key);
        bloomFilter.tryInit(expectedInsertions, falseProbability);
        return bloomFilter;
    }

    @Override
    public <T> boolean addToBloomFilter(String key, T value) {
        RBloomFilter<T> bloomFilter = redissonClient.getBloomFilter(key);
        return bloomFilter.add(value);
    }

    @Override
    public <T> boolean mightContainInBloomFilter(String key, T value) {
        RBloomFilter<T> bloomFilter = redissonClient.getBloomFilter(key);
        return bloomFilter.contains(value);
    }


// ======================== Lua 脚本 ========================

    @Override
    public <V> V eval(String script, List<String> keys, List<Object> args) {
        RScript rScript = redissonClient.getScript(StringCodec.INSTANCE);
        return rScript.eval(
                RScript.Mode.READ_WRITE,
                script,
                RScript.ReturnType.VALUE,
                Collections.singletonList(keys),
                args.toArray()
        );
    }

    public String getFunction() {
        RFunction f = redissonClient.getFunction();
        f.load("lib", "redis.register_function('myfun', function(keys, args) return args[1] end)");
        // execute function
        return f.call(FunctionMode.READ, "myfun", FunctionResult.STRING, Collections.emptyList(), "test");
    }


// ======================== 延迟队列 ========================

    @Override
    public void addToDelayQueue(String queueName, String value, long delay, TimeUnit unit) {
        RBlockingQueue<String> blockingQueue = redissonClient.getBlockingQueue(queueName);
        RDelayedQueue<String> delayedQueue = redissonClient.getDelayedQueue(blockingQueue);
        delayedQueue.offer(value, delay, unit);
    }

    @Override
    public void listenDelayQueue(String queueName, Consumer<String> consumer) {
        RBlockingQueue<String> blockingQueue = redissonClient.getBlockingQueue(queueName);
        new Thread(() -> {
            while (true) {
                try {
                    // 阻塞等待
                    String item = blockingQueue.take();
                    consumer.accept(item);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }).start();
    }


// ======================== 限流器 ========================

    @Override
    public RRateLimiter initRateLimiter(String key, long rate, long interval, RateIntervalUnit unit) {
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);
        rateLimiter.trySetRate(RateType.OVERALL, rate, interval, unit);
        return rateLimiter;
    }

    @Override
    public boolean tryAcquire(String key, long permits) {
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);
        return rateLimiter.tryAcquire(permits);
    }

    public long publish(String topicName, String message) {
        RTopic topic = redissonClient.getTopic(topicName);
        // publish 会返回接收到该消息的订阅者数量
        return topic.publish(message);
    }

    @Override
    public void subscribe(String topicName, Consumer<String> messageHandler) {
        RTopic topic = redissonClient.getTopic(topicName);
        topic.addListener(String.class, (channel, msg) -> {
            // 传入的 messageHandler 是对消息的处理逻辑
            messageHandler.accept(msg);
        });
    }

    public <M> void addListener(String topicName, Class<M> messageType, MessageListener<M> listener) {
        RTopic topic = redissonClient.getTopic(topicName);
        topic.addListener(messageType, listener);
    }

    // --- 原子计数器 ---
    public long atomicIncrement(String atomicName) {
        RAtomicLong atomicLong = redissonClient.getAtomicLong(atomicName);
        return atomicLong.incrementAndGet();
    }

    public long atomicGet(String atomicName) {
        RAtomicLong atomicLong = redissonClient.getAtomicLong(atomicName);
        return atomicLong.get();
    }

    // --- 信号量 ---
    public boolean tryAcquireSemaphore(String semaphoreName, long waitTime, TimeUnit unit) throws InterruptedException {
        RSemaphore semaphore = redissonClient.getSemaphore(semaphoreName);
        return semaphore.tryAcquire(waitTime, unit);
    }

    public void releaseSemaphore(String semaphoreName) {
        RSemaphore semaphore = redissonClient.getSemaphore(semaphoreName);
        semaphore.release();
    }

    @Override
    public void geoAdd(String key, double longitude, double latitude, String member) {
        RGeo<String> geo = redissonClient.getGeo(key);
        geo.add(longitude, latitude, member);
    }

    @Override
    public List<String> geoRadius(String key, double longitude, double latitude, double radiusMeters) {
        RGeo<String> geo = redissonClient.getGeo(key);
        return geo.search(
                GeoSearchArgs.from(longitude, latitude)
                        .radius(radiusMeters, GeoUnit.METERS)
        );
    }

    @Override
    public void initSemaphore(String key, int permits) {
        RSemaphore semaphore = redissonClient.getSemaphore(key);
        try {
            // 删除旧值后重新设置（可选，防止重复调用时 permits 叠加）
            semaphore.delete();
        } catch (Exception ignored) {
        }
        semaphore.trySetPermits(permits);
    }

    @Override
    public boolean acquireSemaphore(String key) {
        RSemaphore semaphore = redissonClient.getSemaphore(key);
        try {
            return semaphore.tryAcquire();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void schedule(String taskName, Runnable task, long delay, TimeUnit unit) {
        RScheduledExecutorService scheduler = getOrCreateScheduler(taskName);
        scheduler.schedule(wrapRunnable(task), delay, unit);
    }

    @Override
    public void scheduleAtFixedRate(String taskName, Runnable task, long initialDelay, long period, TimeUnit unit) {
        RScheduledExecutorService scheduler = getOrCreateScheduler(taskName);
        scheduler.scheduleAtFixedRate(wrapRunnable(task), initialDelay, period, unit);
    }

    /**
     * 获取或创建分布式定时调度器
     */
    private RScheduledExecutorService getOrCreateScheduler(String taskName) {
        RScheduledExecutorService executor = redissonClient.getExecutorService(taskName);
        if (!executor.isShutdown() && !executor.isTerminated()) {
            return executor;
        }
        return redissonClient.getExecutorService(taskName);
    }

    /**
     * 将任务包装为远程可执行对象
     */
    private Runnable wrapRunnable(Runnable task) {
        return new SerializableRunnable(task);
    }

    /**
     * 将 Runnable 包装为可序列化形式（Redisson 要求）
     */
    private static class SerializableRunnable implements Runnable, Serializable {
        private final Runnable delegate;

        public SerializableRunnable(Runnable delegate) {
            this.delegate = delegate;
        }

        @Override
        public void run() {
            if (delegate != null) {
                delegate.run();
            }
        }
    }

}
