package com.bigshen.learningDemo.design.proxy.jdkProxy.encoder;

import com.bigshen.learningDemo.design.proxy.jdkProxy.HessianSerialization;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author byj
 * @date 2025/3/21
 * @Description
 */
public class RpcDecoder extends ByteToMessageDecoder {
    private static final int MESSAGE_LENGTH_BYTES = 4;
    private static final int MESSAGE_LENGTH_VALID_MINIMUM_VALUE = 0;

    private Class<?> targetClass;
    public RpcDecoder(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        //1.消息长度校验
        //首先校验消息长度的字节数，也就是byteBuf当前可读的字节数，必须达到4个字节，此时才可以继续往下走
        if (byteBuf.readableBytes() < MESSAGE_LENGTH_BYTES) {
            return;
        }

        //2.读索引标记
        //对于byteBuf当前可以读的readerIndex，进行mark标记，也就是进行读索引标记
        //后续可以通过这个mark标记，找回来重新发起read读取之前的一个readerIndex位置
        byteBuf.markReaderIndex();

        //3.读取消息长度
        //读取4个字节的int，int代表了消息bytes的长度
        int messageLength = byteBuf.readInt();

        //4.消息长度负值校验
        //如果此时消息长度是小于0，说明此时通信已经出现了故障
        if (messageLength < MESSAGE_LENGTH_VALID_MINIMUM_VALUE) {
            channelHandlerContext.close();
        }

        //5.拆包校验
        //判断可读字节数是否小于消息长度，若是则出现了拆包，需要对byteBuf的读索引进行复位，下次再读
        //byteBuf.readableBytes()读完4个字节后继续读byteBuf.readableBytes()
        //如果此时消息字节数据没有接收完整，那么可以读的字节数是比消息字节长度小的，这就是检查经典的拆包问题
        //这时需要进行读索引进行复位，本次不再进行数据处理
        if (byteBuf.readableBytes() < messageLength) {
            byteBuf.resetReaderIndex();
            //出现拆包后，等待下次数据输入时再进行分析
            //EventLoop里有个for循环会不断监听Channel的读事件；
            //当数据还在传输时，由于传输是一个持续的过程，所以在传输数据过程中，Channel会一直产生读事件；
            //这个过程中，只要循环回来执行判断，就肯定满足监听到Channel的读事件；
            //因此在数据还没传输完成时，for循环执行到去判断是否有Channel的读事件，就会出现这种拆包问题；
            //所以只要返回不处理并且复位读索引，那么下次for循环到来又可重新处理该Channel的读事件了；
            return;
        }

        //6.将字节数组反序列化为指定类
        byte[] bytes = new byte[messageLength];
        byteBuf.readBytes(bytes);
        Object object = HessianSerialization.deserialize(bytes, targetClass);
        list.add(object);
    }
}