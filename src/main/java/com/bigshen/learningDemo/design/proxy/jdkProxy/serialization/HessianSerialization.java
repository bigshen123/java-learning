package com.bigshen.learningDemo.design.proxy.jdkProxy.serialization;

/**
 * @author byj
 * @date 2025/3/21
 * @Description Hessian序列化与反序列化
 * 需要将请求对象和响应对象序列化成二进制字节数组，以及将获取到的二进制字节数组反序列化成请求对象和响应对象，这里使用Hessian框架来实现序列化与反序列化。
 */
//public class HessianSerialization {
//    //序列化：将对象序列化成字节数组
//    public static byte[] serialize(Object object) throws IOException {
//        //new一个字节数组输出流
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        //根据字节数组输出流new一个Hessian序列化输出流
//        HessianOutput hessianOutput = new HessianOutput(byteArrayOutputStream);
//        //用Hessian序列化输出流去写object
//        hessianOutput.writeObject(object);
//        //将Hessian序列化输出流转化为字节数组
//        byte[] bytes = byteArrayOutputStream.toByteArray();
//        return bytes;
//    }
//
//    //反序列化：将字节数组还原成对象
//    public static Object deserialize(byte[] bytes, Class clazz) throws IOException {
//        //先封装一个字节数组输入流
//        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
//        //将字节数组输入流封装到Hessian序列化输入流里去
//        HessianInput hessianInput = new HessianInput(byteArrayInputStream);
//        //从Hessian序列化输入流读出一个对象
//        Object object = hessianInput.readObject(clazz);
//        return object;
//    }
//}
