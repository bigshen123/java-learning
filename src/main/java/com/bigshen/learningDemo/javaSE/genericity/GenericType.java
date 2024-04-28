package com.bigshen.learningDemo.javaSE.genericity;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @Author BYJ
 * @Date 2024/2/3 15:13
 * @Describe
 */
public class GenericType<T> {
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static void main(String[] args) {
        GenericType<String> genericType = new GenericType<String>() {};
        Type superclass = genericType.getClass().getGenericSuperclass();
        //getActualTypeArguments 返回确切的泛型参数, 如Map<String, Integer>返回[String, Integer]
        Type type = ((ParameterizedType) superclass).getActualTypeArguments()[0];
        System.out.println(type);//class java.lang.String
    }
}
