package com.bigshen.learningDemo.design.proxy.jdkProxy.serialization;

/**
 * @author byj
 * @date 2025/3/21
 * @Description
 */
/*public class HessianSerializationTest {
    public static void main(String[] args) throws Exception {
        RpcRequest rpcRequest = new RpcRequest();//先new一个RpcRequest对象
        rpcRequest.setRequestId(UUID.randomUUID().toString().replace("-", ""));
        rpcRequest.setClassName("TestClass");
        rpcRequest.setMethodName("sayHello");
        rpcRequest.setParameterClasses(new String[]{"String"});
        rpcRequest.setParameters(new Object[]{"wjunt"});
        rpcRequest.setInvokerApplicationName("RpcClient");
        rpcRequest.setInvokerIp("127.0.0.1");

        byte[] bytes = HessianSerialization.serialize(rpcRequest);//进行序列化
        System.out.println(bytes.length);

        RpcRequest deSerializedRpcRequest = (RpcRequest) HessianSerialization.deserialize(bytes, RpcRequest.class);
        System.out.println(deSerializedRpcRequest);
    }
}*/
