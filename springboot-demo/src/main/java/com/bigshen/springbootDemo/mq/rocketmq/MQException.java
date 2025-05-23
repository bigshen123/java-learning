package com.bigshen.springbootDemo.mq.rocketmq;

/**
 * @author byj
 * @date 2025/5/22
 * @Description
 */
public class MQException extends Exception {

    private static final long serialVersionUID = 1L;

    public MQException() {
        super();
    }

    public MQException(String message, Throwable cause) {
        super(message, cause);
    }
}