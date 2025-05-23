package com.bigshen.springbootDemo.mq.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class Producer {
    private final KafkaProducer<String, String> producer;
    private final String topic;
    private final Boolean isAsync;


    public Producer(final String topic,
                    final Boolean isAsync,
                    final String transactionalId,
                    final boolean enableIdempotency,
                    final int transactionTimeoutMs) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaProperties.BOOTSTRAP_SERVERS_CONFIG);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "DemoProducer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        if (transactionTimeoutMs > 0) {
            props.put(ProducerConfig.TRANSACTION_TIMEOUT_CONFIG, transactionTimeoutMs);
        }
        if (transactionalId != null) {
            props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, transactionalId);
        }
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, enableIdempotency);

        producer = new KafkaProducer<>(props);
        this.topic = topic;
        this.isAsync = isAsync;
    }

    KafkaProducer<String, String> get() {
        return producer;
    }

    /**
     * 异步发送消息，发送完每个信息后都等待响应；
     * 尽管Kafka会把目标主题、分区信息和消息的偏移量发送回来，
     * 但对于发送端的应用程序来说不是必须的。
     * 不过在遇到消息发送失败时，我们需要抛出异常、记录错误日志，或者把消息写入“错误消息”文件以便日后分析
     * 为了在异步发送消息的同时能够对异常情况进行处理，生产者提供了回调支持
     * @param messageKey
     * @param messageStr
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void sendRecord(String messageKey, String messageStr) throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();
        if (isAsync) { // Send asynchronously
            producer.send(new ProducerRecord<>(topic,
                    messageKey,
                    messageStr), new DemoCallBack(startTime, messageKey, messageStr)).get();
        } else { // Send synchronously
            try {
                producer.send(new ProducerRecord<>(topic,
                        messageKey,
                        messageStr)).get();
                System.out.println("Sent message: (" + messageKey + ", " + messageStr + ")");
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}

class DemoCallBack implements Callback {

    private final long startTime;
    private final String key;
    private final String message;

    public DemoCallBack(long startTime, String key, String message) {
        this.startTime = startTime;
        this.key = key;
        this.message = message;
    }

    /**
     * A callback method the user can implement to provide asynchronous handling of request completion. This method will
     * be called when the record sent to the server has been acknowledged. When exception is not null in the callback,
     * metadata will contain the special -1 value for all fields except for topicPartition, which will be valid.
     *
     * @param metadata  The metadata for the record that was sent (i.e. the partition and offset). An empty metadata
     *                  with -1 value for all fields except for topicPartition will be returned if an error occurred.
     * @param exception The exception thrown during processing of this record. Null if no error occurred.
     */
    public void onCompletion(RecordMetadata metadata, Exception exception) {
        long elapsedTime = System.currentTimeMillis() - startTime;
        if (metadata != null) {
            System.out.println(
                    "message(" + key + ", " + message + ") sent to partition(" + metadata.partition() +
                            "), " +
                            "offset(" + metadata.offset() + ") in " + elapsedTime + " ms");
        } else {
            exception.printStackTrace();
        }
    }
}
