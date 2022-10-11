package com.bigshen.learningDemo.common.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author byj
 * @date 2022/10/11
 */
@Setter
@Getter
public abstract class BaseResponse extends BaseJsonModel {

    private static final long serialVersionUID = 943059230470133936L;

    private String id;
    private String apiVersion;
}
