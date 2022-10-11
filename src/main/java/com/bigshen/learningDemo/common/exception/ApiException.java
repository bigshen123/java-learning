package com.bigshen.learningDemo.common.exception;

import com.bigshen.learningDemo.common.model.RequestUUID;
import com.bigshen.learningDemo.common.spring.Spring;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

/**
 * 自定义API异常
 */
@Getter
public class ApiException extends RuntimeException {

    private static final long serialVersionUID = -5648781561904624095L;

    /**
     * 响应的状态码
     */
    protected int httpStatus = 500;
    /**
     * 友好错误描述
     */
    protected String message;
    /**
     * 错误的唯一标识（如异常类名称、错误宏/常量定义）
     */
    protected String reason;
    /**
     * 错误恢复建议。
     */
    protected String recoveryOptions;

    /**
     * 调试信息。 比如： 上下文信息， 局部变量信息
     */
    protected Map<String, Object> diagnoseInfo;

    public ApiException(int httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Deprecated
    public ApiException(int httpStatus, String message, String reason) {
        super(message);
        this.httpStatus = httpStatus;
        this.message = message;
        this.reason = reason;
    }

    public ApiException(int httpStatus, String message, Throwable classReason) {
        super(message, classReason);
        this.httpStatus = httpStatus;
        this.message = message;
        this.reason = classReason != null ? classReason.getClass().getSimpleName() : null;
    }

    @Deprecated
    public ApiException(int httpStatus, String message, String reason, Throwable classReason) {
        super(message, classReason);
        this.httpStatus = httpStatus;
        this.message = message;
        this.reason = reason;
    }


    @Override
    public String getMessage() {
        return newError().toString();
    }

    public ApiException withReason(String reason) {
        this.reason = reason;
        return this;
    }

    public ApiException withRecoveryOptions(String recoveryOptions) {
        this.recoveryOptions = recoveryOptions;
        return this;
    }

    public ApiException withDiagnoseInfo(Map<String, Object> diagnoseInfo) {
        this.diagnoseInfo = diagnoseInfo;
        return this;
    }

    public ApiException appendDiagnoseInfo(String key, Object value) {
        if (diagnoseInfo == null) {
            diagnoseInfo = new HashMap<>();
        }
        diagnoseInfo.put(key, value);
        return this;
    }

    public String searchDomain() {
        String domain;
        if ((domain = searchDomain(getStackTrace())) != null) {
            return domain;
        }
        if (getCause() != null) {
            if ((domain = searchDomain(getCause().getStackTrace())) != null) {
                return domain;
            }
        }
        if (Spring.getCtx() != null) {
            return Spring.getProperty("spring.application.name");
        }
        return "Unknown";
    }

    private String searchDomain(StackTraceElement[] stackTraceElements) {

        return Stream.of(stackTraceElements)
                .filter(stackTraceElement -> {
                    try {
                        return null != Class.forName(stackTraceElement.getClassName()).getAnnotation(Service.class);
                    } catch (ClassNotFoundException ignored) {
                        return false;
                    }
                })
                .findFirst()
                .map(StackTraceElement::getFileName)
                .map(domain -> domain.substring(0, domain.indexOf(".java")))
                .orElse(null);
    }

    public Failure newError() {

        Failure failure = new Failure();

        failure.setDomain(searchDomain());
        failure.setMessage(message);
        failure.setReason(reason);
        failure.setRecoveryOptions(recoveryOptions);
        appendDiagnoseInfo("exceptionId", getRequestIdOrNew());
        failure.setDiagnoseInfo(diagnoseInfo);

        return failure;
    }

    public List<Failure> newErrors() {
        List<Failure> failures = new ArrayList<>();
        addError(failures, this);
        return failures;
    }

    private void addError(List<Failure> failures, Throwable throwable) {
        if (throwable instanceof ApiException) {
            failures.add(((ApiException) throwable).newError());
        } else {
            Failure failure = new Failure();
            failure.setMessage(throwable.getMessage());
            failure.setReason(throwable.getCause() != null ? throwable.getCause().getClass().getSimpleName() : null);
            failures.add(failure);
        }

        if (throwable.getCause() != null) {
            addError(failures, throwable.getCause());
        }
    }

    /**
     * @return {@link #message}(友好错误描述) 成员变量值
     */
    public String getMessageValue() {
        return this.message;
    }

    /**
     * 获取请求的ID,没有则随机
     *
     * @return 请求ID
     */
    public static String getRequestIdOrNew() {
        String requestId;
        if (Spring.isInWebRequest()) {
            try {
                requestId = Spring.getBean(RequestUUID.class).getUuid().toString();
            } catch (Exception e) {
                requestId = UUID.randomUUID().toString();
            }
        } else {
            requestId = UUID.randomUUID().toString();
        }
        return requestId;
    }
}
