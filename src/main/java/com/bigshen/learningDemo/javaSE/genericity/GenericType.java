package com.bigshen.learningDemo.javaSE.genericity;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @Author BYJ
 * @Date 2024/2/3 15:13
 * @Describe 自定义泛型工具类
 */
@Setter
@Getter
public class GenericType<T> {
    private final Class<T> type;

    @SuppressWarnings("unchecked")
    public GenericType() {
        Type superClass = getClass().getGenericSuperclass();
        if (superClass instanceof ParameterizedType) {
            //getActualTypeArguments 返回确切的泛型参数, 如Map<String, Integer>返回[String, Integer]
            Type actualType = ((ParameterizedType) superClass).getActualTypeArguments()[0];
            this.type = (Class<T>) actualType;
        } else {
            throw new RuntimeException("泛型类型无法解析");
        }
    }

    static class StringType extends GenericType<String> {}
    static class IntegerType extends GenericType<Integer> {}

    public static void main(String[] args) {
        StringType stringType = new StringType();
        System.out.println("泛型类型是: " + stringType.getType().getName());
        IntegerType integerType = new IntegerType();
        System.out.println("泛型类型是: " + integerType.getType().getName());
    }
}
