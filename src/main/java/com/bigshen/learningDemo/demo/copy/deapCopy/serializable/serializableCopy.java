package com.bigshen.learningDemo.demo.copy.deapCopy.serializable;

import com.bigshen.learningDemo.demo.copy.deapCopy.Address;
import com.bigshen.learningDemo.demo.copy.deapCopy.User;
import org.apache.commons.lang.SerializationUtils;

/**
 * @author byj
 * @date 2022/10/13
 */
public class serializableCopy {
    public static void main(String[] args) {
        Address address = new Address("杭州", "中国");
        User user = new User("大山", address);

        // 使用Apache Commons Lang序列化进行深拷贝
        User copyUser = (User) SerializationUtils.clone(user);

        // 修改源对象的值
        user.getAddress().setCity("深圳");

        // 检查两个对象的值不同
        boolean equals = user.getAddress().getCity().equals(copyUser.getAddress().getCity());
        System.out.println(equals);
    }
}
