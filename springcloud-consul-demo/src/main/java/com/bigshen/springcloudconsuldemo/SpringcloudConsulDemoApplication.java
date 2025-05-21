package com.bigshen.springcloudconsuldemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 详细springCloud demo 参考SpringCloudLearning仓库
 */
@SpringBootApplication
@EnableDiscoveryClient
public class SpringcloudConsulDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringcloudConsulDemoApplication.class, args);
    }

}
