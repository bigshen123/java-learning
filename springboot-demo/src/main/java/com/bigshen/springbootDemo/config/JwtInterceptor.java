package com.bigshen.springbootDemo.config;

import com.bigshen.springbootDemo.annotation.PassToken;
import com.bigshen.springbootDemo.util.JWTUtils;
import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.HandlerMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Enumeration;

/**
 * @author byj
 * @date 2025/5/28
 * @Description 使用拦截器，通过反射，可以将请求缩小到方法上。实现
 * @PassToken注解标注的方法可以不需要进行token登录验证。
 */
@Configuration
public class JwtInterceptor extends HandlerInterceptorAdapter {



    /**
     * 读取jwt.enable配置信息
     */
    @Value("${jwt.enabled}")
    private String jwt_enabled;

    /**
     * 在业务处理器处理请求之前执行
     * @param request 请求
     * @param response 响应
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if(jwt_enabled.equals("false")){
            //jwt token验证服务未开启时，返回true
            return true;
        }else{
            // 请求资源没有映射到方法上时直接通过
            if (!(handler instanceof HandlerMethod)) {
                return true;
            }
            //判断方法上是否有存在@PassToken注解
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            PassToken annotation = method.getAnnotation(PassToken.class);
            if(annotation!=null&&annotation.canPass()){


                return true;
            }else{


                //所有的请求头信息
                Enumeration<String> headerNames = request.getHeaderNames();

                while ( headerNames.hasMoreElements()){


                    System.out.println(headerNames.nextElement());
                }
                //解析请求中的token信息
                String token=request.getHeader("Authorization");
                //解析请求中的
                String empno=request.getHeader("empno");
                if(StringUtils.isEmpty(token)){


                    throw new LoginException("请求头中的token为空，token验证失败，请重新登录");
                }else{


                    JWTUtils jwtUtil=new JWTUtils(); //JwtUti工具类见上一章节介绍
                    DefaultClaims object = (DefaultClaims)jwtUtil.decodeJWTrHS256(token, "123456");
                    //自定义token playload载体的信息
                    String emp=object.get("emp",String.class);
                    if(!emp.equals(empno)){
                        //自定义LoginException，当校验token失败后，抛出该异常，交由全局异常处理
                        throw new LoginException("token验证用户工号失败，无法请求服务");
                    }else {
                        //判断token是否过期
                        Date expireDate =object.getExpiration();
                        if(expireDate.before(new Date())){
                            throw new LoginException("token已过期，请重新登录获得token");
                        }
                    }
                }
            }
        }

        return true;
    }
    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse,
                           Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {
    }

}
