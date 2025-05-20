//package com.bigshen.springbootDemo.config;
//
//import org.redisson.Redisson;
//import org.redisson.api.RedissonClient;
//import org.redisson.config.Config;
//import org.redisson.spring.cache.CacheConfig;
//import org.redisson.spring.cache.RedissonSpringCacheManager;
//import org.redisson.spring.data.connection.RedissonConnectionFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cache.CacheManager;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @author byj
// * @date 2025/5/20
// * @Description
// */
//@Configuration
//@EnableCaching
//public class RedissonConfig {
//    @Primary
//    @Bean(name = "redissonConnectionFactory")
//    public RedissonConnectionFactory redissonConnectionFactory(RedissonClient redisson) {
//        return new RedissonConnectionFactory(redisson);
//    }
//    @Bean(destroyMethod = "shutdown")
//    @Primary
//    public RedissonClient redissonClient(@Value("${redisson.config}") String redissonConfig) throws IOException {
//        Config config = Config.fromJSON(redissonConfig);
//        return Redisson.create(config);
//    }
//    @Primary
//    @Bean
//    CacheManager cacheManager(RedissonClient redissonClient) {
//        Map<String, CacheConfig> config = new HashMap<String, CacheConfig>();
//        // 创建一个名称为"SpringCacheConfig"的缓存，过期时间ttl为24分钟，同时最长空闲时maxIdleTime为12分钟。
//        config.put("SpringCacheConfig", new CacheConfig(24*60*1000, 12*60*1000));
//        return new RedissonSpringCacheManager(redissonClient, config);
//    }
//}
