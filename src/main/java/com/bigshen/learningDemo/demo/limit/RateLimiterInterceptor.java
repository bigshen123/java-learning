package com.bigshen.learningDemo.demo.limit;

import org.apache.curator.shaded.com.google.common.util.concurrent.RateLimiter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author byj
 * @date 2024/1/5
 * @Description
 */
public class RateLimiterInterceptor extends HandlerInterceptorAdapter {
    private final RateLimiter rateLimiter = RateLimiter.create(10);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (rateLimiter.tryAcquire()) {
            return true;
        } else {
            response.getWriter().write("Rate limit exceeded");
            return false;
        }
    }
}
