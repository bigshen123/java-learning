package com.bigshen.learningDemo.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author byj
 * @date 2022/10/11
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> extends BaseJsonModel {
    private String id;
    private String apiVersion;
    private T data;
}
