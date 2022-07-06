package com.bigshen.chatDemoService.utils;

import cn.hutool.core.collection.CollectionUtil;
import com.bigshen.chatDemoService.common.constants.Constants;
import com.bigshen.chatDemoService.common.exception.ApiException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Objects;

/**
 * @author byj
 * @date 2022/2/11
 * 参数校验utils 配合@Validated 、BindingResult result 对传入的对象进行参数校验异常处理
 */
public class ThrowableUtils {
    public static void checkParamArgument(BindingResult result) {
        if (result != null && result.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> errors = result.getFieldErrors();
            if (CollectionUtil.isNotEmpty(errors)) {
                FieldError error = errors.get(0);
                String rejectedValue = Objects.toString(error.getRejectedValue(), "");
                String defMsg = error.getDefaultMessage();
                // 排除类上面的注解提示
                if (rejectedValue.contains(Constants.DELIMITER_TO)) {
                    // 自己去确定错误字段
                    sb.append(defMsg);
                } else {
                    if (Constants.DELIMITER_COLON.contains(defMsg)) {
                        sb.append(error.getField()).append(" ").append(defMsg);
                    } else {
                        sb.append(error.getField()).append(" ").append(defMsg);
                    }
                }
            } else {
                String msg = result.getAllErrors().get(0).getDefaultMessage();
                sb.append(msg);
            }
            throw new ApiException(sb.toString());
        }
    }
}
