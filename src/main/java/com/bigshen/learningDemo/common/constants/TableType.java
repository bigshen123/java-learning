package com.bigshen.learningDemo.common.constants;

import com.bigshen.learningDemo.common.util.HumpUtil;
import com.bigshen.learningDemo.utils.EnumUtil;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author byj
 * @date 2022/10/24
 */

@Getter
public enum TableType {

    User("用户管理/用户"),
    UserGroup("用户管理/用户组"),
    App("资源管理/资源"),
    AppGroup("资源管理/资源组"),
    AppGroupBind("资源管理/资源组绑定"),
    AppNodeBind("资源管理/资源节点绑定"),
    AppUserAutoLogin("资源管理/资源用户自动登录"),
    Term("终端管理/终端"),
    UserTermBind("终端管理/用户终端绑定"),
    Cert("证书管理/证书"),
    CertAppBind("证书管理/证书资源绑定"),
    Crl("证书管理/CRL"),
    Ocsp("证书管理/OCSP"),
    AclPolicy("策略管理/访问控制策略"),
    Admin("管理员管理/管理员"),
    Node("节点管理/节点"),
    Client("客户端管理/客户端"),
    FileMeta("文件管理/文件");

    private String businessName;

    TableType(String businessName){
        this.businessName = businessName;
    }

    @JsonValue
    public String getHumpName() {
        return HumpUtil.underlineToHump(name());
    }

    @JsonValue
    public String getName() {
        return name();
    }

    @JsonCreator
    public static TableType valueOfWithFormat(String name) {
        return EnumUtil.valueOfWithFormat(TableType.class, name);
    }

    public static TableType valueOfWithFormat(String name, TableType defaultValue) {
        return EnumUtil.valueOfWithFormat(TableType.class, name, defaultValue);
    }

    public static String getBusinessName(String serviceTypeStr) {
        if (serviceTypeStr == null) {
            return null;
        }
        TableType[] values = values();
        for (TableType value : values) {
            if (value.name().equalsIgnoreCase(serviceTypeStr)) {
                return value.getBusinessName();
            }
        }
        return null;
    }

}
