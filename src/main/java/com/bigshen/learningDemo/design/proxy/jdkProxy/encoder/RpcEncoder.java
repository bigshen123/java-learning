package com.bigshen.learningDemo.design.proxy.jdkProxy.encoder;

import com.bigshen.learningDemo.design.proxy.jdkProxy.HessianSerialization;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author byj
 * @date 2025/3/21
 * @Description
 * 编码可以理解为进行序列化操作，解码可以理解为进行反序列化操作。
 * <p>
 * 编码器RpcEncoder需要继承Netty的MessageToByteEncoder类，解码器RpcDecoder需要继承Netty的ByteToMessageDecoder类。
 * <p>
 * 反序列化的逻辑需要根据序列化时数据的封装逻辑来进行处理，比如下面编码后的一条数据是由字节数组长度 + 字节数组组成的，因此反序列化需要根据此来写对应的逻辑。
 */
public class RpcEncoder extends MessageToByteEncoder {
    //要进行序列化的目标类
    private Class<?> targetClass;

    public RpcEncoder(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        //传入的对象o是否是Encoder所指定的类的实例对象
        if (targetClass.isInstance(o)) {
            byte[] bytes = HessianSerialization.serialize(o);

            //将序列化好的字节数组写到byteBuf里去
            //先写数据长度到byteBuf，这个长度就是4个字节的bytes的length
            byteBuf.writeInt(bytes.length);
            //然后再写完整的bytes数组到byteBuf
            byteBuf.writeBytes(bytes);
        }
    }
}
