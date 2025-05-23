package com.bigshen.springbootDemo.controller;

import com.bigshen.springbootDemo.mq.rocketmq.MQProducerService;
import com.bigshen.springbootDemo.mq.rocketmq.User;
import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController; /**
 * @author byj
 * @date 2025/5/22
 * @Description
 */
@RestController
@RequestMapping("/rocketmq")
public class RocketMQController {

    @Autowired
    private MQProducerService mqProducerService;

    @GetMapping("/send")
    public void send() {
        User user = new User();
        mqProducerService.send(user);
    }

    @GetMapping("/sendTag")
    public SendResult sendTag() {
        return mqProducerService.sendTagMsg("带有tag的字符消息");
    }

}
