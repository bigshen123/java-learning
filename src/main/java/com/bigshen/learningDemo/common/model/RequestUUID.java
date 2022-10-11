package com.bigshen.learningDemo.common.model;

import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

/**
 * @author byj
 * @date 2022/10/11
 * 设置一个request级别的uuid
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@Getter
public class RequestUUID {

    private UUID uuid = UUID.randomUUID();

}
