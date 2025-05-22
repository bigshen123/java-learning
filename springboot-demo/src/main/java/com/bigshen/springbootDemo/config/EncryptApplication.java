package com.bigshen.springbootDemo.config;

import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author byj
 * @date 2025/5/22
 * @Description jasypt 加密解密工具类
 */
@Component
public class EncryptApplication implements CommandLineRunner {

    @Autowired
    private ApplicationContext appCtx;

    @Autowired
    private StringEncryptor codeSheepEncryptorBean;

    @Override
    public void run(String... args) {
        Environment environment = appCtx.getBean(Environment.class);

        // 首先获取配置文件里的原始明文信息
        String mysqlOriginPswd = environment.getProperty("spring.redis.password");
        // 加密
        String mysqlEncryptedPswd = encrypt(mysqlOriginPswd);

        // 打印加密前后的结果对比
        // 配置中加上ENC() 函数包裹 获取时可以直接获取到解密后的明文信息
        System.out.println("MySQL原始明文密码为：" + mysqlOriginPswd);
        System.out.println("====================================");
        System.out.println("MySQL原始明文密码加密后的结果为：" + mysqlEncryptedPswd);
    }

    private String encrypt(String originPassword) {
        return codeSheepEncryptorBean.encrypt(originPassword);
    }

    private String decrypt(String encryptedPassword) {
        return codeSheepEncryptorBean.decrypt(encryptedPassword);
    }
}
