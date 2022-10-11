package com.bigshen.learningDemo.common.exception;

import com.bigshen.learningDemo.common.model.BaseJsonModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author byj
 * @date 2022/10/11
 */
@ApiModel
@Setter
@Getter
public class Failure extends BaseJsonModel {

    private static final long serialVersionUID = 8275795944642510343L;

    @ApiModelProperty(required = true, value = "错误类型（一般是产生错误的服务或业务的名称）")
    private String domain;
    @ApiModelProperty(required = true, value = "友好错误描述")
    private String message;
    @ApiModelProperty(required = true, value = "错误的唯一标识（如异常类名称、错误宏/常量定义）")
    private String reason;
    @ApiModelProperty(value = "错误恢复建议。也可以使用google建议的`extendedHelper`给出URI")
    private String recoveryOptions;
    @ApiModelProperty(value = "诊断信息（错误日志或抛出的异常信息）")
    private Map<String, Object> diagnoseInfo;
    @ApiModelProperty(value = "帮助信息URI")
    private String extendedHelper;
    @ApiModelProperty(value = "错误信息收集地址")
    private String sendReport;

}
