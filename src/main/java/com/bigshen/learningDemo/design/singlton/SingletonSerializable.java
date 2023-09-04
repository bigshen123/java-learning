package com.bigshen.learningDemo.design.singlton;


import java.io.*;

/**
 * @Author BYJ
 * @Date 2022/12/4 17:11
 * @Describe
 */
public class SingletonSerializable implements Serializable {
    //私有构造方法
    private SingletonSerializable() {
    }

    private static class SingletonHolder {
        private static final SingletonSerializable INSTANCE = new SingletonSerializable();
    }

    /**
     对外提供静态方法获取该对象
     */
    public static SingletonSerializable getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public static void main(String[] args) throws Exception {
        //往文件中写对象
        //writeObject2File();
        //从文件中读取对象
        SingletonSerializable s1 = readObjectFromFile();
        SingletonSerializable s2 = readObjectFromFile();

        //判断两个反序列化后的对象是否是同一个对象
        System.out.println(s1 == s2);
    }

    private static SingletonSerializable readObjectFromFile() throws Exception {
        //创建对象输入流对象
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("C:\\Users\\Think\\Desktop\\a.txt"));
        //第一个读取Singleton对象
        return (SingletonSerializable) ois.readObject();
    }

    public static void writeObject2File() throws Exception {
        //获取Singleton类的对象
        SingletonSerializable instance = SingletonSerializable.getInstance();
        //创建对象输出流
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("C:\\Users\\Think\\Desktop\\a.txt"));
        //将instance对象写出到文件中
        oos.writeObject(instance);
    }
}
