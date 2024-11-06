package com.bigshen.learningDemo.utils.dns;

import com.bigshen.learningDemo.utils.dns.model.DnsResponse;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.xbill.DNS.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author byj
 * @date 2024/9/9
 * @Description
 */
@Slf4j
@Api(value = "dns解析")
@RestController
@RequestMapping(path = DnsController.PATH, produces = APPLICATION_JSON_VALUE)
public class DnsController {
    public static final String PATH = "/coredns";

    @GetMapping("/resolve")
    public DnsResponse resolveDomain(@RequestParam("domain") String domain) {
        String ip = "";
        try {
            Lookup lookup = new Lookup(domain, Type.A);
            // 指向 CoreDNS 的 IP 地址
            SimpleResolver resolver = new SimpleResolver("127.0.0.1");
            lookup.setResolver(resolver);
            lookup.setCache(null);
            Record[] records = lookup.run();

            if (lookup.getResult() == Lookup.SUCCESSFUL && records != null) {
                for (Record record : records) {
                    ARecord a = (ARecord) record;
                    ip = a.getAddress().getHostAddress();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new DnsResponse(domain, "Error resolving domain: " + e.getMessage());
        }
        return new DnsResponse(domain, ip);
    }


    @PostMapping(value = "/resolveBinary", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] resolveDomainBinary(@RequestBody byte[] dnsQuery) {
        try {
            // 发送请求并获取响应
            byte[] response = DnsResolver.sendDnsQuery(dnsQuery);

            // 返回原始的DNS请求和响应,返回 query 和 response 的合并数据
            return DnsResolver.mergeQueryAndResponse(dnsQuery, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
