package com.bigshen.learningDemo.demo.jvm.classLoader;

import java.io.IOException;
import java.io.InputStream;

/**
 * @ClassName ClassLoaderTest
 * @Description: TODO
 * @Author BYJ
 * @Date 2020/6/27
 * @Version V1.0
 **/
public class ClassLoaderTest {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        ClassLoader classLoader=new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {

                try {
                    String fileName = name.substring(name.lastIndexOf(".") + 1) + ".class";
                    InputStream is = getClass().getResourceAsStream(fileName);
                    if (is==null){
                        return super.loadClass(name);
                    }
                    byte[] b=new byte[is.available()];
                    is.read(b);
                    return defineClass(name,b,0,b.length);
                } catch (IOException e) {
                    throw new  ClassNotFoundException(name);
                }

            }
        };
        Object obj = classLoader.loadClass("com.bigshen.learningDemo.demo.jvm.classLoader.ClassLoaderTest").newInstance();
        System.out.println(obj.getClass());
       // System.out.println(obj instanceof com.bigshen.chatDemoService.demo.jvm.classLoader.ClassLoaderTest);
    }
}
