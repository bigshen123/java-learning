package com.bigshen.learningDemo.jni.sdfDemo.exception;

/**
 * @Author BYJ
 * @Date 2023/11/16 22:12
 * @Describe sdf 异常
 */
public class SdfException extends RuntimeException{
    private static final long serialVersionUID = -5438419127181131148L;

    /**
     * Constructs an instance of this class.
     *
     * @param   msg
     *          the detail message
     */
    public SdfException(String msg) {
        super(msg);
    }

    /**
     * Constructs an instance of this class.
     *
     * @param   msg
     *          the detail message
     */
    public SdfException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
