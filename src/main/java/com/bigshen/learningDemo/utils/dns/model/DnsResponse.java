package com.bigshen.learningDemo.utils.dns.model;

import java.util.List;

/**
 * @author byj
 * @date 2024/9/9
 * @Description
 */
public class DnsResponse {
    private String domain;
    private String resolvedIp;

    public DnsResponse(String domain, String resolvedIp) {
        this.domain = domain;
        this.resolvedIp = resolvedIp;
    }

    public String getDomain() {
        return domain;
    }

    public String getResolvedIp() {
        return resolvedIp;
    }
}
