package com.bigshen.springbootDemo.encrypt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.swing.*;
import java.util.List;

/**
 * @author byj
 * @date 2024/10/22
 * @Description 策略模式 demo
 */
@Slf4j
@Configuration("externalApiAuthenticationConfiguration")
public class ExternalApiAuthenticationConfiguration {


    /**
     * Spring Bean 的自动装配EncryptStrategy接口的实现类,使用 List 进行自动装配。（实现类需要@Component注解）
     */
    private final List<? extends EncryptStrategy> encryptStrategies;

    /**
     构造函数注入时，Spring 会执行类似以下的逻辑:
     List<EncryptStrategy> encryptStrategies = context.getBeansOfType(EncryptStrategy.class).values().stream().collect(Collectors.toList());
     * @param encryptStrategies
     */
    public ExternalApiAuthenticationConfiguration(List<? extends EncryptStrategy> encryptStrategies) {
        this.encryptStrategies = encryptStrategies;
    }

    @ConditionalOnMissingBean(name = "externalApiAuthenticationHandler")
    @Bean(name = "externalApiAuthenticationHandler")
    public ExternalApiAuthenticationHandler externalApiAuthenticationHandler() {
        try {
            return new ExternalApiAuthenticationHandler(encryptStrategies);
        } catch (Exception e) {
            log.error("外部接口认证插件启动失败", e);
        }
        return null;
    }

}
