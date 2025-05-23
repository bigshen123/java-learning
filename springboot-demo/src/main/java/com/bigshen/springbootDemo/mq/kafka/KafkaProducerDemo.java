package com.bigshen.springbootDemo.mq.kafka;

import com.alibaba.fastjson.JSONObject;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class KafkaProducerDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Producer producer = new Producer(KafkaProperties.TOPIC,true,null,false,30);
        JSONObject message = new JSONObject();
        message.put("policeNumer","9002");
        message.put("policeState","值班");
        String sendMessage = message.toJSONString();
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        //发送数据
        producer.sendRecord(uuid,sendMessage);
    }
}
