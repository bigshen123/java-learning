package com.bigshen.learningDemo.utils.aviator.integration;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorDecimal;
import com.googlecode.aviator.runtime.type.AviatorObject;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

/**
 * @Author BYJ
 * @Date 2024/5/21 21:54
 * @Describe
 */
public class NormalFunction  extends AbstractFunction {
    private static final long serialVersionUID = -5862571263644261319L;


    /**
     * 返回结果
     * @param env 参数map
     * @param arg1  map里的key名
     * @param arg2 map里的key名
     * @param arg3 map里的key名
     * @return {@link AviatorObject}
     */
    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2, AviatorObject arg3) {
        BigDecimal bigDecimal = (BigDecimal) FunctionUtils.getJavaObject(arg1, env);
        Integer year = (Integer) FunctionUtils.getNumberValue(arg2, env);
        String type = FunctionUtils.getStringValue(arg3, env);
        if (StringUtils.equals("normal", type)) {
            if (Objects.isNull(bigDecimal)) {
                bigDecimal = BigDecimal.ONE;
            }
            bigDecimal = bigDecimal.multiply(new BigDecimal(year));
            return new AviatorDecimal(bigDecimal);
        }
        return null;
    }

    @Override
    public String getName() {
        return "normal";
    }
}
