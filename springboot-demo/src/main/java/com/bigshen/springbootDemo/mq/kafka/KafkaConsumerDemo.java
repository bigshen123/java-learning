package com.bigshen.springbootDemo.mq.kafka;

/**
 * kafka消费者样例
 */

import org.apache.kafka.clients.consumer.KafkaConsumer;
public class KafkaConsumerDemo {

    public static void main(String[] args){
        Consumer consumerThread = new Consumer(KafkaProperties.TOPIC,"kafkademo",false);
        //如果需要主动订阅数据，可以开启下面注解，作用是使kafka的consumer优雅的退出并提交偏移量
//        Runtime.getRuntime().addShutdownHook(new Thread(){
//            @Override
//            public void run() {
//                System.out.println("Starting exit...");
//                KafkaConsumer<String, String> consumer = consumerThread.get();
//                consumer.wakeup();
//                try{
//                    consumerThread.join();
//                }catch (InterruptedException e){
//                    e.printStackTrace();
//                }
//            }
//        });
        consumerThread.start();
    }
}
