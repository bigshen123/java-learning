package com.bigshen.learningDemo.javaSE.restTemplate;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

/**
 * @author byj
 * @date 2022/10/11
 * RestTemplate 操作
 * RestTemplate提供了六种常用的HTTP方法实现远程服务调用
 * <p>
 * getForObject – 发送GET请求，将HTTP response转换成一个指定的object对象
 * postForEntity – 发送POST请求，将给定的对象封装到HTTP请求体，返回类型是一个HttpEntity对象
 * 每个HTTP方法对应的RestTemplate方法都有3种。其中2种的url参数为字符串，URI参数变量分别是Object数组和Map，第3种使用URI类型作为参数
 * exchange 和execute 方法比上面列出的其它方法（如getForObject、postForEntity等）使用范围更广，允许调用者指定HTTP请求的方法（GET、POST、PUT等），
 * 并且可以支持像HTTP PATCH（部分更新）。
 * <p>
 * 参数说明：
 * url：请求路径
 * method：请求的方法（GET、POST、PUT等）
 * requestEntity：HttpEntity对象，封装了请求头和请求体
 * responseType：返回数据类型
 * uriVariables：支持PathVariable类型的数据。
 */
public class RestTemplateDemo {

    private final static String RemoteUrl = "https://10.0.210.152:60443/api/v1/dms/sys/time";

    public static void checkSuccess(@NotNull ResponseEntity<?> response) throws IOException {
        if (200 > response.getStatusCodeValue()
                || 300 <= response.getStatusCodeValue()) {
            throw new IOException(response.toString());
        }
    }

    public static String getJsonString(String url) throws IOException {
        RestTemplate restTemplate = RestTemplateFactory.getRestTemplate();
        return getJsonString(restTemplate, url);
    }

    private static String getJsonString(RestTemplate restTemplate, String url) throws IOException {
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
        RestTemplateDemo.checkSuccess(forEntity);
        return forEntity.getBody();
    }

    public static String postJsonString(String url, String jsonString) throws IOException {
        RestTemplate restTemplate = RestTemplateFactory.getRestTemplate();
        return postJsonString(restTemplate, url, jsonString);
    }

    public static String postJsonString(RestTemplate restTemplate, String url, String jsonString) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> httpEntity = new HttpEntity<>(jsonString, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, httpEntity, String.class);
        RestTemplateDemo.checkSuccess(responseEntity);
        return responseEntity.getBody();
    }

    public static void putJsonString(String url, String jsonString) {
        RestTemplate restTemplate = RestTemplateFactory.getRestTemplate();
        putJsonString(restTemplate, url, jsonString);
    }

    private static void putJsonString(RestTemplate restTemplate, String url, String jsonString) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(jsonString, headers);
        restTemplate.put(url, httpEntity, String.class);
    }

    public static String postFileBytes(String url, String fileName, byte[] fileBytes) throws RestClientException {
        RestTemplate restTemplate = RestTemplateFactory.getRestTemplate();
        return postFileBytes(restTemplate, url, fileName, fileBytes);
    }

    private static String postFileBytes(RestTemplate restTemplate, String url, String fileName, byte[] fileBytes) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //设置请求体，注意是LinkedMultiValueMap
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("file", new ByteArrayResource(fileBytes) {
            @Override
            public String getFilename() {
                return fileName;
            }
        });
        //用HttpEntity封装整个请求报文
        HttpEntity<MultiValueMap<String, Object>> files = new HttpEntity<>(form, headers);
        return restTemplate.postForObject(url, files, String.class);
    }

    public static ResponseEntity<String> postExchange(String url, HttpEntity<Object> httpEntity) {
        RestTemplate restTemplate = RestTemplateFactory.getRestTemplate();
        return postExchange(restTemplate, url, httpEntity);

    }

    private static ResponseEntity<String> postExchange(RestTemplate restTemplate, String url, HttpEntity<Object> httpEntity) {
        return restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
    }

    @Test
    public void postExchangeTest() {
        //设置http的header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //设置访问参数
        Map<String, Object> ntpMap = new HashMap<>(3);
        ntpMap.put("model", "ntp");
        ntpMap.put("ntpServer", "ntp.g.koal.com");
        ntpMap.put("localeTime", LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
        //设置访问的Entity
        HttpEntity<Object> entity = new HttpEntity<>(ntpMap, headers);
        ResponseEntity<String> result = RestTemplateDemo.postExchange(RemoteUrl, entity);
        JSONObject data = JSONObject.parseObject(result.getBody()).getJSONObject("data");
        String status = (String) data.get("data");
        System.out.println(status);

    }
}
