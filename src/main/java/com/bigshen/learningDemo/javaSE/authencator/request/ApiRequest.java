package com.bigshen.learningDemo.javaSE.authencator.request;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Description:
 * @Author: BIGSHEN
 * @Date: 2019/12/21 18:38
 */
public class ApiRequest {
    private String baseUrl;
    private String appID;
    private long timeStamp;
    private String token;

    public ApiRequest(String fullUrl) {

        Map<String, String> params = parse(fullUrl);
        // TODO
        String baseUrl = "";
        String appID = "";
        long timeStamp = 0L;
        String token = "";
    }

    public static ApiRequest genFakeReq(String fullUrl) {
        return new ApiRequest("geekbang", "designpattern", new Date().getTime(),
                "IXIGIpJ9hdOBCyjStaDJ5Nom07g=");
    }

    private static Map<String, String> parse(String fullUrl) {
        // TODO
        return new TreeMap<>();
    }


    private ApiRequest(String baseUrl, String appID, long timeStamp, String token) {
        this.baseUrl = baseUrl;
        this.appID = appID;
        this.timeStamp = timeStamp;
        this.token = token;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getAppID() {
        return appID;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public String getToken() {
        return token;
    }
}
