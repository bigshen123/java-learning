package com.bigshen.learningDemo.demo.copy.deapCopy.gson;

import com.bigshen.learningDemo.demo.copy.deapCopy.Address;
import com.bigshen.learningDemo.demo.copy.deapCopy.User;
import com.google.gson.Gson;

/**
 * @author byj
 * @date 2022/10/13
 */
public class GsonCopy {
    public static void main(String[] args) {
        Address address = new Address("杭州", "中国");
        User user = new User("大山", address);

        // 使用Gson序列化进行深拷贝
        Gson gson = new Gson();
        User copyUser = gson.fromJson(gson.toJson(user), User.class);

        // 修改源对象的值
        user.getAddress().setCity("深圳");

        // 检查两个对象的值不同
        System.out.println(user.getAddress().getCity().equals(copyUser.getAddress().getCity()));
    }
}
