package com.bigshen.springbootDemo.mq.rocketmq;

import com.bigshen.springbootDemo.mq.rocketmq.constant.MessageContent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * @author byj
 * @date 2025/5/22
 * @Description
 */
@Component
@Slf4j
public class CommonProducer {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 发送普通队列通用方法
     *
     * @param messageContent
     * @return
     * @throws MQException
     */
    public boolean messageSend(MessageContent messageContent) throws MQException {
        log.debug("普通队列mq消息发送, messageContent is: {}", messageContent);
        try {
            SendResult sendResult;
            if (StringUtils.isNotEmpty(messageContent.keys)) {
                Message<?> message = MessageBuilder.withPayload(messageContent.body)
                        .setHeader(MessageConst.PROPERTY_KEYS, messageContent.keys).build();
                sendResult = rocketMQTemplate.syncSend(messageContent.topic + ":" + messageContent.tag, message);
            } else {
                sendResult = rocketMQTemplate.syncSend(messageContent.topic + ":" + messageContent.tag,
                        messageContent.body);
            }
            return sendResult != null && sendResult.getSendStatus() == SendStatus.SEND_OK;
        } catch (Exception e) {
            log.error("通用发消息异常", e);
            throw new MQException("mq send error", e);
		}
	}
}
