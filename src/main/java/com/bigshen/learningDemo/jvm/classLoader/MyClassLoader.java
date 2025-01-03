package com.bigshen.learningDemo.jvm.classLoader;

import java.io.*;

/**
 * @Description:自定义类加载器
 * @Author: BIGSHEN
 * @Date: 2019/12/12 21:06
 */
public class MyClassLoader extends ClassLoader{
    private String root;

    /**
     * 重写findClass方法
     * @param name
     * @return
     * @throws ClassNotFoundException
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException{
        //根据类名加载类数据
        byte[] classData=loadClassData(name);
        if (classData==null){
            throw new ClassNotFoundException();
        }else {
            return defineClass(name,classData,0,classData.length);
        }
    }

    /**
     * 根据类名加载类数据
     * @param name 类名
     * @return
     */
    private byte[] loadClassData(String name) {
        String fileName=root+ File.separatorChar +
                name.replace('.',File.separatorChar)+".class";
        try {
            InputStream inputStream=new FileInputStream(fileName);
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            int bufferSize=1024;
            byte[] buffer=new byte[bufferSize];
            int length=0;
            while ((length=inputStream.read(buffer))!=-1){
                baos.write(buffer,0,length);
            }
            return baos.toByteArray();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public static void main(String[] args) {
        MyClassLoader classLoader=new MyClassLoader();
        classLoader.setRoot("D:\\IdeaProjects\\chat_demo\\src\\main\\java\\com\\bigshen\\chatDemoService");
        Class<?> testClass=null;
        try {
            testClass = classLoader.loadClass("com.bigshen.learningDemo.demo.jvm.classLoader.ClassLoaderTest");
            Object object = testClass.newInstance();
            System.out.println(object.getClass().getClassLoader());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
