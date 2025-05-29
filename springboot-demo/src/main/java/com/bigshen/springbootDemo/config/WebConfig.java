package com.bigshen.springbootDemo.config;

import com.bigshen.springbootDemo.config.mdc.TraceInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author byj
 * @date 2025/5/29
 * @Description
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private TraceInterceptor traceInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(traceInterceptor).addPathPatterns("/**");
    }
}
