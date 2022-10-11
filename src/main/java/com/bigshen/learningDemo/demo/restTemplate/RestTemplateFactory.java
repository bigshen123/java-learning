package com.bigshen.learningDemo.demo.restTemplate;

import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import com.bigshen.learningDemo.common.spring.Spring;

/**
 * @author byj
 * @date 2022/10/11
 */
public class RestTemplateFactory {
    /**
     * 默认不校验ssl
     */
    @Primary
    @Bean("restTemplate")
    public RestTemplate restTemplate(CloseableHttpClient closeableHttpClient) {
        return new RestTemplate(new HttpComponentsClientHttpRequestFactory(closeableHttpClient));
    }

    /**
     * 根据参数直接返回 使用连接池 对象(优先从bean中获取，没有则返回新对象)
     *
     * @return 使用连接池 对象(优先从bean中获取，没有则返回新对象)
     */
    public static RestTemplate getRestTemplate() {

        Spring.assertIsInSpring();

        return Spring.getBean(RestTemplate.class);
    }


    /**
     * 根据参数直接返回 使用连接池 对象
     *
     * @return 新 使用连接池 对象
     */
    public static RestTemplate newRestTemplate() {

        Spring.assertIsInSpring();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(Spring.getBean(CloseableHttpClient.class));

        return new RestTemplate(factory);
    }
}
