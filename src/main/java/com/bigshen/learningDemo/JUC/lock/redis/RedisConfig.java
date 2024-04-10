package com.bigshen.learningDemo.JUC.lock.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author byj
 */
@Configuration
public class RedisConfig {


    private static final String HOST ="127.0.0.1";
    private static final int PORT =6379;
    private static final int TIMEOUT =30000;
    private static final int MAX_IDLE =10;
    private static final int MAX_WAIT_MILLIS =1500;

    private Boolean blockWhenExhausted;

    @Bean
    public JedisPool jedisPoolFactory() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(MAX_IDLE);
        jedisPoolConfig.setMaxWaitMillis(MAX_WAIT_MILLIS);
        // 连接耗尽时是否阻塞, false报异常,true阻塞直到超时, 默认true
        jedisPoolConfig.setBlockWhenExhausted(blockWhenExhausted);
        return new JedisPool(jedisPoolConfig, HOST, PORT, TIMEOUT);
    }
}
