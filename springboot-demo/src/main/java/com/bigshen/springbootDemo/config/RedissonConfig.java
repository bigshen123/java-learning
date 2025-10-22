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
//import org.springframework.core.io.Resource;
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
//    /**
//     * Redisson 用于整合 Spring Data Redis 的连接工厂
//     * 底层由 Redisson 驱动实现 从而支持更多高级特性，如自动重连、异步操作、分布式锁等
//     *
//     * @param redisson
//     * @return
//     */
//    @Primary
//    @Bean(name = "redissonConnectionFactory")
//    public RedissonConnectionFactory redissonConnectionFactory(RedissonClient redisson) {
//        return new RedissonConnectionFactory(redisson);
//    }
//
//    /**
//     * redisson 配置文件
//     * 就不需要application.yml中增加 spring.redis.redisson.file = classpath:redisson.yml 配置
//     *
//     * @param configFile redisson配置文件
//     * @return
//     * @throws IOException
//     */
//    @Bean(destroyMethod = "shutdown")
//    @Primary
//    public RedissonClient redissonClient(@Value("classpath:/redisson.yml") Resource configFile) throws IOException {
//        Config config = Config.fromYAML(configFile.getInputStream());
//        return Redisson.create(config);
//    }
//
//    /**
//     * Spring 用来管理缓存的配置
//     * 使用 Redisson 的方式将 Spring Cache 操作映射为 Redis 缓存操作，完成分布式缓存功能。
//     *
//     * @param redissonClient redisson客户端
//     * @return
//     */
//    @Primary
//    @Bean
//    CacheManager cacheManager(RedissonClient redissonClient) {
//        Map<String, CacheConfig> config = new HashMap<String, CacheConfig>();
//        // 创建一个名称为"SpringCacheConfig"的缓存，过期时间ttl为24分钟，同时最长空闲时maxIdleTime为12分钟。
//        // 后续可以在 @Cacheable(cacheNames = "SpringCacheConfig") 中使用
//        // 所有使用 @Cacheable、@CachePut、@CacheEvict 注解的地方，都会委托给这个 CacheManager 管理。
//        config.put("SpringCacheConfig", new CacheConfig(24 * 60 * 1000, 12 * 60 * 1000));
//        return new RedissonSpringCacheManager(redissonClient, config);
//    }
//}
