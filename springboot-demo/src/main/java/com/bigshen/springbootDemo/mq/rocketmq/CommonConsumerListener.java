package com.bigshen.springbootDemo.mq.rocketmq;

import com.bigshen.springbootDemo.mq.rocketmq.constant.MQConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.stereotype.Service;

/**
 * @author byj
 * @date 2025/5/22
 * @Description
 */
@Service
@Slf4j
@RocketMQMessageListener(topic = MQConstant.COMMON_TOPIC_POST_REQUEST, consumerGroup = MQConstant.COMMON_CONSUMER_GROUP)
public class CommonConsumerListener implements RocketMQListener<String>, RocketMQPushConsumerLifecycleListener {


    @Override
    public void onMessage(String s) {
        // TODO 业务逻辑
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer defaultMQPushConsumer) {
        defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        defaultMQPushConsumer.setMessageModel(MessageModel.BROADCASTING);
    }
}
