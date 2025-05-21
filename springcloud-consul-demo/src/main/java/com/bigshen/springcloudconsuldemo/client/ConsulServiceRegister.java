package com.bigshen.springcloudconsuldemo.client;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.agent.model.NewService;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author byj
 * @date 2025/5/21
 * @Description
 */
public class ConsulServiceRegister {
    public static void main(String[] args) {
        ConsulClient localhostClient = new ConsulClient("localhost");
        String serviceName = "myapp";

        // register new service
        NewService newService01 = new NewService();
        newService01.setId("myapp_01");
        newService01.setName(serviceName);
        newService01.setTags(Arrays.asList("EU-West", "EU-East"));
        newService01.setPort(8080);
        localhostClient.agentServiceRegister(newService01);

        // register new service with associated health check
        NewService newService02 = new NewService();
        newService02.setId("myapp_02");
        newService02.setName(serviceName);
        newService02.setTags(Collections.singletonList("EU-East"));
        newService02.setPort(8080);

        // 添加一个健康检查
        NewService.Check serviceCheck = new NewService.Check();
        // 该地址由consul来回调检查
        serviceCheck.setHttp("http://xxx.xxx.xxx.xxx:8080/hearth/check");
        serviceCheck.setInterval("10s");
        newService02.setCheck(serviceCheck);

        localhostClient.agentServiceRegister(newService02);

        System.out.println("注册完毕！！！");
    }
}
