package com.bigshen.learningDemo.concurrent.thread.threadLocal;

/**
 * @Description:
 * @Author: BIGSHEN
 * @Date: 2019/12/22 10:51
 */
public class ThreadContext {
    private String userId;
    private Long transactionId;
    private static ThreadLocal threadLocal = new ThreadLocal(){
        @Override
        protected ThreadContext initialValue() {
            return new ThreadContext();
        }
    };
    public static ThreadContext get() {
        return (ThreadContext) threadLocal.get();
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public Long getTransactionId() {
        return transactionId;
    }
    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }
}
