package com.bigshen.springbootDemo.mq.rocketmq.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author byj
 * @date 2025/5/23
 * @Description
 */
@ConfigurationProperties(prefix = "rocketmq.enhance")
@Data
public class RocketEnhanceProperties {

    private boolean enabledIsolation;

    private String environment;
}