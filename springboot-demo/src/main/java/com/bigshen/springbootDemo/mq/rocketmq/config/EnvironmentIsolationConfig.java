package com.bigshen.springbootDemo.mq.rocketmq.config;

import org.apache.rocketmq.spring.support.DefaultRocketMQListenerContainer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * @author byj
 * @date 2025/5/23
 * @Description RockeMQ环境隔离  避免测试环境的消息被开发环境消费
 * 在实例化消息监听者之前把topic修改掉
 * 实现BeanPostProcessor接口，在监听器实例初始化前修改对应的topic。
 */
@Configuration
public class EnvironmentIsolationConfig implements BeanPostProcessor {
    @Value("${rocketmq.enhance.enabledIsolation:true}")
    private boolean enabledIsolation;
    @Value("${rocketmq.enhance.environment:''}")
    private String environmentName;

    private RocketEnhanceProperties rocketEnhanceProperties;

    public EnvironmentIsolationConfig(RocketEnhanceProperties rocketEnhanceProperties) {
        this.rocketEnhanceProperties = rocketEnhanceProperties;
    }

    /**
     * 在装载Bean之前实现参数修改
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof DefaultRocketMQListenerContainer){

            DefaultRocketMQListenerContainer container = (DefaultRocketMQListenerContainer) bean;

            if(rocketEnhanceProperties.isEnabledIsolation() && StringUtils.hasText(rocketEnhanceProperties.getEnvironment())){
                container.setTopic(String.join("_", container.getTopic(),rocketEnhanceProperties.getEnvironment()));
            }
            return container;
        }
        return bean;
    }
}
