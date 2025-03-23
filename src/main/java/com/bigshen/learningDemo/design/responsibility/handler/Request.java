package com.bigshen.learningDemo.design.responsibility.handler;

/**
 * @Author BYJ
 * @Date 2025/3/23 18:59
 * @Describe
 */
public class Request {
    private RequestType type;
    private String name;

    public Request(RequestType type, String name) {
        this.type = type;
        this.name = name;
    }

    public RequestType getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
