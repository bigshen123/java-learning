package com.bigshen.learningDemo.mq.kafka;

public class KafkaProperties {
    public static final String TOPIC = "bigshen-topic";
    public static final String BOOTSTRAP_SERVERS_CONFIG = "192.168.2.61:9092";
    public static final int KAFKA_SERVER_PORT = 9092;
    public static final String CONSUME_GROUP_ID = "kafkademo";
    private KafkaProperties() {}
}