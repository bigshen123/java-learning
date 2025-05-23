package com.bigshen.springbootDemo.mq.rocketmq.constant;

import java.io.Serializable;

/**
 * @author byj
 * @date 2025/5/22
 * @Description 消息内容的封装
 *
 * 在RocketMQ中有四种可选格式：
 * 1、发送Json对象
 * 2、发送转Json后的String对象
 * 3、根据业务封装对应实体类
 * 4、直接使用原生MessageExt接收。
 */
public class MessageContent implements Serializable {

    public final String topic;
    public final String tag;
    public final String keys;
    public final Object body;

    public MessageContent(String topic, String tag, String keys, Object body) {
        this.topic = topic;
        this.tag = tag;
        this.keys = keys;
        this.body = body;
    }

    public MessageContent(String topic, String keys, Object body) {
        this(topic, MQConstant.OCSS_DEFAULT_TAG, keys, body);
    }

    @Override
    public String toString() {
        return "MessageContent{" + "topic='" + topic + '\'' + ", tag='" + tag + '\'' + ", keys='" + keys + '\''
                + ", body=" + body + '}';
    }
}
