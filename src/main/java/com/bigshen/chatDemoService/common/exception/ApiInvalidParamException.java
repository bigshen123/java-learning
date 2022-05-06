package com.bigshen.chatDemoService.common.exception;


public class ApiInvalidParamException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ApiInvalidParamException() {
        super();
    }

    public ApiInvalidParamException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiInvalidParamException(String message) {
        super(message);
    }

    public ApiInvalidParamException(Throwable cause) {
        super(cause);
    }
}