package com.bigshen.learningDemo.demo.copy.deapCopy.jackson;

import com.bigshen.learningDemo.demo.copy.deapCopy.Address;
import com.bigshen.learningDemo.demo.copy.deapCopy.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author byj
 * @date 2022/10/13
 * 深拷贝方法
 * 优点
 * 缺点
 * 重写clone()方法
 * 1. 底层实现较简单
 * 2. 不需要引入第三方包
 * 3. 系统开销小
 * 1. 可用性较差，每次新增成员变量可能需要修改clone()方法
 * 2. 拷贝类（包括其成员变量）需要实现Cloneable接口
 * Apache.Commons.Lang序列化
 * 1. 可用性强，新增成员变量不需要修改拷贝方法
 * 1. 底层实现较复杂
 * 2. 需要引入Apache Commons Lang第三方JAR包
 * 3. 拷贝类（包括其成员变量）需要实现Serializable接口
 * 4. 序列化与反序列化存在一定的系统开销
 * Gson序列化
 * 1. 可用性强，新增成员变量不需要修改拷贝方法
 * 2. 对拷贝类没有要求，不需要实现额外接口和方法
 * 1. 底层实现复杂
 * 2. 需要引入Gson第三方JAR包
 * 3. 序列化与反序列化存在一定的系统开销
 * Jackson序列化
 * 1. 可用性强，新增成员变量不需要修改拷贝方法
 * 1. 底层实现复杂
 * 2. 需要引入Jackson第三方JAR包
 * 3. 拷贝类（包括其成员变量）需要实现默认的无参构造函数
 * 4. 序列化与反序列化存在一定的系统开销
 */
public class JacksonCopy {
    public static void main(String[] args) throws JsonProcessingException {
        Address address = new Address("杭州", "中国");
        User user = new User("大山", address);

        // 使用Jackson序列化进行深拷贝
        ObjectMapper objectMapper = new ObjectMapper();
        User copyUser = objectMapper.readValue(objectMapper.writeValueAsString(user), User.class);

        // 修改源对象的值
        user.getAddress().setCity("深圳");

        // 检查两个对象的值不同
        System.out.println(user.getAddress().getCity().equals(copyUser.getAddress().getCity()));
    }
}
