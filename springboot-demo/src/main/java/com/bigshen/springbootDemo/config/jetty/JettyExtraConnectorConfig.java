package com.bigshen.springbootDemo.config.jetty;

import org.eclipse.jetty.server.ServerConnector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

/**
 * @author byj
 * @date 2025/5/28
 * @Description  新启动一个端口
 * 使用jetty：轻量、灵活、适合现代微服务
 * 追求 轻量级、启动快
 * 嵌入式 Web 应用（如 Spring Boot 微服务）
 * 高并发、高连接数，如消息推送、WebSocket
 * 资源受限的部署环境（如容器、IoT）
 *
 * Tomcat：稳重、兼容性强、企业级项目首选。
 */
@Component
public class JettyExtraConnectorConfig implements WebServerFactoryCustomizer<JettyServletWebServerFactory> {

    @Value("${server.additional-port:28081}")
    private int additionalPort;

    @Value("${server.additional-address:0.0.0.0}")
    private String additionalAddress;

    @Override
    public void customize(JettyServletWebServerFactory factory) {
        factory.addServerCustomizers(server -> {
            ServerConnector extraConnector = new ServerConnector(server);
            extraConnector.setPort(additionalPort);
            extraConnector.setHost(additionalAddress); // 可选：设置监听地址
            server.addConnector(extraConnector);
        });
    }
}
