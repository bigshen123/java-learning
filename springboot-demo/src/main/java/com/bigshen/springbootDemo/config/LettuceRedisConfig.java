package com.bigshen.springbootDemo.config;

import io.lettuce.core.resource.DefaultClientResources;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * @author byj
 * @date 2025/5/27
 * @Description
 */
@Configuration
public class LettuceRedisConfig {

    @Bean
    public RedisConnectionFactory redisConnectionFactory(RedisProperties redisProperties) {
        return LettuceConnectionFactoryConfig.createRedisConnectionFactory(DefaultClientResources.create(), redisProperties);
    }

}
