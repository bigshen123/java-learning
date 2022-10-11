package com.bigshen.learningDemo.common.spring;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * @author byj
 * @date 2022/10/11
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class Spring implements ApplicationContextAware {

    private static ConfigurableApplicationContext ctx;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext ctx) throws BeansException {
        if (ctx.getParent() == null) {
//          Bootstrap 的 ApplicationContext 会提前释放，如果注入，在 /actuator/refresh 阶段可能引起失败
            throw new RuntimeException("不能注入 Bootstrap 的 ApplicationContext");
        }
        Spring.ctx = (ConfigurableApplicationContext) ctx;
    }

    public static boolean isInSpring() {
        return ctx != null;
    }

    public static void assertIsInSpring() {
        if (ctx == null) {
            throw new RuntimeException("必须先初始化 ApplicationContext， 请通过 '@Import' '@DependsOn' 等方式显示声明依赖的Bean");
        }
    }

    @Nullable
    public static ConfigurableApplicationContext getCtx() {
        return ctx;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String id) {
        return (T) ctx.getBean(id);
    }

    public static <T> T getBean(String id, Class<T> claz) {
        return ctx.getBean(id, claz);
    }

    public static <T> T getBean(Class<T> claz) {
        return ctx.getBean(claz);
    }

    public static boolean isInWebRequest() {
        return RequestContextHolder.getRequestAttributes() != null;
    }

    @Nullable
    public static HttpServletRequest getRequest() {
        return Optional.ofNullable((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .map(ServletRequestAttributes::getRequest)
                .orElse(null);
    }

    public static String getClientRealIp() {
        if (!isInSpring()) {
            return null;
        }
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }
        return getClientRealIp(request);
    }

    public static String getClientRealIp(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        String ip = request.getHeader("X-Real-IP");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        return request.getRemoteAddr();
    }


    public static String getOriginalRequestHostWithoutPort() {
        if (!isInSpring()) {
            return null;
        }
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }
        String host = request.getHeader("X-Forwarded-Host");
        if (StringUtils.isBlank(host)) {
            host = request.getHeader("Host").split(":")[0];
        }
        return host;
    }

    @Nullable
    public static HttpServletResponse getResponse() {
        return Optional.ofNullable(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()))
                .map(ServletRequestAttributes::getResponse)
                .orElse(null);
    }

    @Nullable
    public static String getProperty(String id) {
        return ctx.getEnvironment().getProperty(id);
    }
}
