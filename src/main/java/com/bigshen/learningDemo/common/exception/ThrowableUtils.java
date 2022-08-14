package com.bigshen.learningDemo.common.exception;

import cn.hutool.core.collection.CollectionUtil;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Objects;

/**
 * 抛出异常工具类
 */
public class ThrowableUtils {

    //异常类型
    public static final String DELIMITER_TO = "@";
    public static final String DELIMITER_COLON = ":";
    /**
     * 校验参数正确,拼装字段名和值到错误信息
     *
     * @param result
     */
    public static void checkParamArgument(BindingResult result) {
        if (result != null && result.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> errors = result.getFieldErrors();
            if (CollectionUtil.isNotEmpty(errors)) {
                FieldError error = errors.get(0);
                String rejectedValue = Objects.toString(error.getRejectedValue(), "");
                String defMsg = error.getDefaultMessage();
                // 排除类上面的注解提示
                if (rejectedValue.contains(DELIMITER_TO)) {
                    // 自己去确定错误字段
                    sb.append(defMsg);
                } else {
                    if (DELIMITER_COLON.contains(defMsg)) {
                        sb.append(error.getField()).append(" ").append(defMsg);
                    } else {
                        sb.append(error.getField()).append(" ").append(defMsg);
                    }
                }
            } else {
                String msg = result.getAllErrors().get(0).getDefaultMessage();
                sb.append(msg);
            }
            throw new ApiInvalidParamException(sb.toString());
        }

    }
}
