package com.bigshen.springbootDemo.mq.kafka;
/**
 * 创建3个分区的kafka的topic
 */

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class KafkaCreateTopicDemo {
    public static void main(String[] args){
        try {
            /**
             * 需要新建的topic
             */
            List<String> topics = new ArrayList<>();
            topics.add("input-topic");
            topics.add("output-topic");
            CreateTopic.recreateTopics(topics,3);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
