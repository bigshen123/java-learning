package com.bigshen.learningDemo.demo.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

/**
 * @Description:将对象序列化
 * @Author: BIGSHEN
 * @Date: 2019/12/16 21:22
 */
public class SerializableDemo {
    public static void main(String[] args) throws Exception {
        //对象输出流
        ObjectInputStream oos=new ObjectInputStream(new FileInputStream
                (new File("D://obj")));
        User user1 = (User)oos.readObject();
        System.out.println(user1);
        oos.close();

        //对象输入流
        ObjectInputStream objectInputStream =
                new ObjectInputStream(new FileInputStream(new File("D://obj")));
         User user2 = (User)objectInputStream.readObject();
         System.out.println(user2);
         objectInputStream.close();
    }
}
