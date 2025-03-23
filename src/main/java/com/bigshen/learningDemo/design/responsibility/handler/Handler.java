package com.bigshen.learningDemo.design.responsibility.handler;

/**
 * @Author BYJ
 * @Date 2025/3/23 18:57
 * @Describe
 */
abstract class Handler {
    protected Handler successor;

    public Handler(Handler successor) {
        this.successor = successor;
    }

    protected abstract void handleRequest(Request request);
}
