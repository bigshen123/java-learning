package com.bigshen.learningDemo.mq.kafka;

import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CountDownLatch;

public class Consumer extends Thread{
    private static final Logger log = LoggerFactory.getLogger(Consumer.class);
    private final KafkaConsumer<String,String> consumer;
    private final String topic;
    private final String groupId;
    private int messageRemaining;
    private Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();

    public Consumer(final String topic,
                    final String groupId,
                    final boolean readCommitted
                    ) {

        this.groupId = groupId;
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaProperties.BOOTSTRAP_SERVERS_CONFIG);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaProperties.CONSUME_GROUP_ID);
//        instanceId.ifPresent(id -> props.put(ConsumerConfig.GROUP_INSTANCE_ID_CONFIG, id));
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        if (readCommitted){
            props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG,"read_committed");
        }
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumer = new KafkaConsumer<String, String>(props);
        this.topic = topic;
    }

    private class HandleRebalance implements ConsumerRebalanceListener{
        @Override
        public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
            System.out.println("Lost partitions in rebalance.Committing current offsets:" + currentOffsets);
            consumer.commitSync(currentOffsets);
        }
        @Override
        public void onPartitionsAssigned(Collection<TopicPartition> partitions) {

        }
    }
    KafkaConsumer<String, String> get() {
        return consumer;
    }

    @Override
    public void run() {
        consumeRecord();
    }

    public void consumeRecord(){
        try{
            consumer.subscribe(Collections.singletonList(topic),new HandleRebalance());
            while (true){
                ConsumerRecords<String, String> records = consumer.poll(100);
                for(ConsumerRecord<String,String> record:records) {
                    System.out.println("topic = " + record.topic());
                    System.out.println("partition = " + record.partition());
                    System.out.println("offset = " + record.offset());
                    System.out.println("customer = " + record.key());
                    System.out.println("country = " + record.value());

                    processRecord(record);
                    storeRecordInDB(record);
                    currentOffsets.put(new TopicPartition(record.topic(),record.partition()),new OffsetAndMetadata(record.offset() + 1,"no metadata"));
                }
                consumer.commitAsync(currentOffsets,null);
            }
        }catch (WakeupException e){
            log.error("wakeup exception",e);
        } catch (Exception e){
            log.error("Unexcepted error",e);
        }finally {
            try{
                consumer.commitSync(currentOffsets);
            }finally {
                consumer.close();
                System.out.println("Closed consumer and we are done");
            }
        }
    }

    private void storeRecordInDB(ConsumerRecord<String, String> record) {
        //TODO
    }

    private void processRecord(ConsumerRecord<String, String> record) {
        //当业务上运行到某条件需要停止kafka的consumer循环时，可以调用consumer.wakeup来退出循环
//        String value = record.value();
//        JSONObject parse = (JSONObject) JSONObject.parse(value);
//        if(!parse.containsKey("police")){
//            consumer.wakeup();
//        }
    }

}
