package com.bigshen.learningDemo.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class HttpClientUtil {

	private static final Log logger = LogFactory.getLog(HttpClientUtil.class);
	
	// 默认字符集
    private static String encoding = "utf-8";

    /**
     * @Description: TODO(发送post请求)
     * @param url      请求地址
     * @param headers  请求头
     * @return String
     * @throws
     */
    public static String sendPost(String url, Map<String, String> headers, String jsonData) {
        // 创建Client
        CloseableHttpClient client = HttpClients.createDefault();
        // 创建HttpPost对象
        HttpPost httpPost = new HttpPost();
        try {
            // 设置请求地址
            httpPost.setURI(new URI(url));
            // 设置请求头
            if (headers != null) {
                Header[] allHeader = new BasicHeader[headers.size()];
                int i = 0;
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    allHeader[i] = new BasicHeader(entry.getKey(), entry.getValue());
                    i++;
                }
                httpPost.setHeaders(allHeader);
            }
            // 设置实体
            httpPost.setEntity(new StringEntity(jsonData, encoding));
            // 发送请求,返回响应对象
            CloseableHttpResponse response = client.execute(httpPost);
            return parseData(response);

        } catch (Exception e) {
        	if (logger.isErrorEnabled()) {
        		logger.error("发送post请求失败", e);
        	}
        } finally {
            httpPost.releaseConnection();
        }
        return null;
    }

    /**
     * @Description: TODO(发送post请求 ， 请求数据默认使用json格式 ， 默认使用UTF - 8编码)
     * @param url  请求地址
     * @param jsonData 请求实体
     * @return String
     * @throws
     */
    public static String sendPost(String url, String jsonData) {
        // 设置默认请求头
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");

        return sendPost(url, headers, jsonData);
    }

    /**
     * @Description: TODO(发送post请求 ， 请求数据默认使用json格式 ， 默认使用UTF - 8编码)
     * @param url    请求地址
     * @param params 请求实体
     * @return String
     * @throws
     */
    public static String sendPost(String url, Map<String, Object> params) {
        // 设置默认请求头
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        // 将map转成json
        String jsonData = JSON.toJSONString(params);
        return sendPost(url, headers, jsonData);
    }

    /**
     * @Description: TODO(发送get请求)
     * @param url      请求地址
     * @param params   请求参数
     * @return String
     * @throws
     */
    public static String sendGet(String url, Map<String, Object> params) {
        // 创建client
        CloseableHttpClient client = HttpClients.createDefault();
        // 创建HttpGet
        HttpGet httpGet = new HttpGet();
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            // 封装参数
            if (params != null) {
                for (String key : params.keySet()) {
                    builder.addParameter(key, params.get(key).toString());
                }
            }
            URI uri = builder.build();
            // 设置请求地址
            httpGet.setURI(uri);
            // 发送请求，返回响应对象
            CloseableHttpResponse response = client.execute(httpGet);
            return parseData(response);
        } catch (Exception e) {
        	if (logger.isErrorEnabled()) {
        		logger.error("发送get请求失败", e);
        	}
        } finally {
            httpGet.releaseConnection();
        }
        return null;
    }

    /**
     * @Description: TODO(发送get请求)
     * @param url 请求地址
     * @return String
     * @throws
     */
    public static String sendGet(String url) {
        return sendGet(url, null);
    }

    /**
     * 解析response
     * @param response
     * @return
     * @throws Exception
     */
    public static String parseData(CloseableHttpResponse response) throws Exception {
        // 获取响应状态
        int status = response.getStatusLine().getStatusCode();
        if (status == HttpStatus.SC_OK) {
            // 获取响应数据
            return EntityUtils.toString(response.getEntity(), encoding);
        } else {
        	if (logger.isErrorEnabled()) {
        		logger.error("响应失败，状态码：" + status);
        	}
        }
        return null;
    }
	
}
