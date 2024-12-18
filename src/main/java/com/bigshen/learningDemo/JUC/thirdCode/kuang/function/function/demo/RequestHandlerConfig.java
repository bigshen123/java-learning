package com.bigshen.learningDemo.JUC.thirdCode.kuang.function.function.demo;


import com.bigshen.learningDemo.common.model.BaseResponse;
import lombok.Getter;

import java.util.function.Function;

/**
 * @author byj
 * @date 2024/12/18
 * @Description
 */
@Getter
public class RequestHandlerConfig<T, R> {
    /**
     * 请求处理函数
     */
    private final Function<T, BaseResponse<R>> handler;

    /**
     * 请求数据类型
     */
    private final Class<T> requestType;

    /**
     * 响应数据类型
     */
    private final Class<R> responseType;

    public RequestHandlerConfig(Function<T, BaseResponse<R>> handler, Class<T> requestType, Class<R> responseType) {
        this.handler = handler;
        this.requestType = requestType;
        this.responseType = responseType;
    }
}
