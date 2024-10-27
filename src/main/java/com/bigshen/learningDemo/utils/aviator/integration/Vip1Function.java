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
 * @Date 2024/5/21 21:52
 * @Describe
 */
public class Vip1Function extends AbstractFunction {
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
        // 获取对象参数
        BigDecimal bigDecimal = (BigDecimal) FunctionUtils.getJavaObject(arg1, env);
        Integer value = (Integer) FunctionUtils.getNumberValue(arg2, env);
        String type = FunctionUtils.getStringValue(arg3, env);

        if (StringUtils.equals("V1", type)) {
            if (Objects.isNull(bigDecimal)) {
                bigDecimal = BigDecimal.ONE;
            }
            bigDecimal = bigDecimal.multiply(new BigDecimal(value)).multiply(BigDecimal.TEN);
            return new AviatorDecimal(bigDecimal);
        }
        return null;

    }

    /**
     * @return {@link String}
     */
    @Override
    public String getName() {
        return "vip1";
    }
}
