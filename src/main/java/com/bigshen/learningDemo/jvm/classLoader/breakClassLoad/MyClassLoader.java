package com.bigshen.learningDemo.jvm.classLoader.breakClassLoad;

import java.io.*;

/**
 * @author byj
 * @date 2025/4/10
 * @Description 打破双亲委派的demo
 */
public class MyClassLoader extends ClassLoader {

    private final String baseDir;

    public MyClassLoader(String baseDir) {
        this.baseDir = baseDir;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        // 不打破核心类加载流程（避免加载如 java.lang.String 等）
        if (name.startsWith("java.") || name.startsWith("javax.")) {
            return super.loadClass(name, resolve);
        }

        // 打破双亲委派，自己先尝试加载
        try {
            byte[] classData = loadClassData(name);
            if (classData != null) {
                Class<?> clazz = defineClass(name, classData, 0, classData.length);
                if (resolve) {
                    resolveClass(clazz);
                }
                return clazz;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 加载失败再交给父加载器
        return super.loadClass(name, resolve);
    }

    private byte[] loadClassData(String className) throws IOException {
        String fileName = baseDir + "/" + className.replace('.', '/') + ".class";
        File file = new File(fileName);
        if (!file.exists()) {
            return null;
        }

        try (InputStream is = new FileInputStream(file);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[4096];
            int len;
            while ((len = is.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        }
    }
}
