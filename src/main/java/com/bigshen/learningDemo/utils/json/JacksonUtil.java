package com.bigshen.learningDemo.utils.json;

/**
 * @author byj
 * @date 2022/7/21
 */

import com.bigshen.learningDemo.common.exception.ApiException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * @author gaodq on 2019/1/23
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class JacksonUtil implements ApplicationContextAware {

    private static ObjectMapper objectMapper;

    private JacksonUtil() {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        objectMapper = applicationContext.getBean(ObjectMapper.class);
    }

    /**
     * @return 返回系统级通用json ObjectMapper对象，请勿随意设置，如需设置且不要求性能，请使用 {@link #newJson()} 方法获取新的
     */
    public static ObjectMapper json() {
        if (objectMapper != null) {
            return objectMapper;
        }
        return newJson();
    }

    public static ObjectMapper newJson() {

        try {
            // SpringContext在初始化过程中 SpringContextHolder.getBean(Jackson2ObjectMapperBuilder.class) 会失败
            // 这是一个关键的工具，尽可能避免执行失败
            log.warn("ApplicationContext is not initialized yet, roll back to to the default Settings.");
            return Jackson2ObjectMapperBuilder.json().createXmlMapper(false).build();

        } catch (Exception e) {
            // SpringContext在初始化过程中 SpringContextHolder.getBean(Jackson2ObjectMapperBuilder.class) 会失败
            // 这是一个关键的工具，尽可能避免执行失败
            log.warn("ApplicationContext is not initialized yet, roll back to to the default Settings. " + e.getMessage());
            return Jackson2ObjectMapperBuilder.json().createXmlMapper(false).build();
        }
    }

    /**
     * 把JavaBean转换为json字符串
     *
     * @param object object
     * @return jsonString
     */
    public static String toJsonString(Object object) {
        try {
            return json().writeValueAsString(object);
        } catch (Exception e) {
            throw new ApiException(e);
        }
    }

    /**
     * 把JavaBean转换为json字符串
     *
     * @param object    object
     * @param nullValue defaultValue
     * @return jsonString
     */
    public static String toJsonString(Object object, String nullValue) {
        try {
            if (object == null) {
                return nullValue;
            }
            return json().writeValueAsString(object);
        } catch (Exception e) {
            throw new ApiException(e);
        }
    }

    /**
     * enum 序列化成json字符串
     *
     * @param enumItem Enum
     * @param <E>      Enum Type
     * @return jsonString
     */
    public static <E extends Enum<E>> String toJsonString(Enum<E> enumItem) {
        try {
            for (Method method : enumItem.getClass().getDeclaredMethods()) {
                JsonValue methodAnnotation = method.getAnnotation(JsonValue.class);
                if (methodAnnotation != null) {
                    return (String) method.invoke(enumItem);
                }
            }
        } catch (Exception ignored) {
        }
        return enumItem.name();
    }

    /**
     * 把JavaBean转换为json字符串
     * <p>
     * 会显示的将 null 输出
     *
     * @param object object
     * @return jsonString
     */
    public static String toJsonStringWithIncludeDefault(Object object) {
        try {
            return newJson().setSerializationInclusion(JsonInclude.Include.USE_DEFAULTS).writeValueAsString(object);
        } catch (Exception e) {
            throw new ApiException(e);
        }
    }

    /**
     * 美化输出，通常用于控制台展示
     *
     * @param object object
     * @return pretty jsonString
     */
    public static String toPrettyJsonString(Object object) {
        try {
            return json().writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (Exception e) {
            throw new ApiException(e);
        }
    }

    public static Map<String, Object> parseObject(String jsonString) {
        try {
            TypeReference<Map<String, Object>> type = new TypeReference<Map<String, Object>>() {
            };
            jsonString = formatEmptyJsonString(jsonString, type);
            return json().readValue(jsonString, type);
        } catch (IOException e) {
            throw new ApiException(e);
        }
    }

    public static <T> T parseObject(String jsonString, Class<T> valueType) {
        try {
            jsonString = formatEmptyJsonString(jsonString, valueType);
            return json().readValue(jsonString, valueType);
        } catch (Exception e) {
            throw new ApiException(e);
        }
    }

    public static <T> T parseObject(String jsonString, TypeReference<T> type) {
        try {
            jsonString = formatEmptyJsonString(jsonString, type);
            return json().readValue(jsonString, type);
        } catch (IOException e) {
            throw new ApiException(e);
        }
    }

    public static <T> T parseObject(String jsonString, Class<?> parametrized, Class<?>... parameterClasses) {
        try {
            jsonString = formatEmptyJsonString(jsonString, parametrized);
            return json().readValue(jsonString, json().getTypeFactory().constructParametricType(parametrized, parameterClasses));
        } catch (IOException e) {
            throw new ApiException(e);
        }
    }

    public static <T> T parseObject(Map<String, Object> map, Class<T> valueType) {
        try {
            String jsonString = toJsonString(map);
            jsonString = formatEmptyJsonString(jsonString, valueType);
            return json().readValue(jsonString, valueType);
        } catch (IOException e) {
            throw new ApiException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <E extends Enum<E>> E parseEnum(Class<E> type, String s) {
        try {
            for (Method method : type.getDeclaredMethods()) {
                JsonCreator methodAnnotation = method.getAnnotation(JsonCreator.class);
                if (methodAnnotation != null) {
                    return (E) method.invoke(null, s);
                }
            }
        } catch (Exception ignored) {
        }
        return Enum.valueOf(type, s);
    }

    /**
     * 通过表达式获取JSON中嵌套的值
     *
     * @param content    JSON content to parse to build the JSON tree.
     * @param expression 表达式 例如： ”/admin/auth“
     * @return 嵌套的值
     */
    public static String getByPath(String content, String expression) {
        JsonNode jsonNode;
        try {
            jsonNode = JacksonUtil.json().readTree(content).at(expression);
        } catch (IOException e) {
            log.error("json:{} , expression:{}", content, expression);
            throw new ApiException("通过表达式获取JSON中嵌套的值失败");
        }
        return JacksonUtil.toJsonString(jsonNode);
    }

    /**
     * 通过表达式获取JSON中嵌套的文本值
     *
     * @param content    JSON content to parse to build the JSON tree.
     * @param expression 表达式 例如： ”/admin/auth“
     * @return 嵌套的值
     */
    public static String getTextByPath(String content, String expression) {
        JsonNode jsonNode;
        try {
            jsonNode = JacksonUtil.json().readTree(content).at(expression);
        } catch (IOException e) {
            log.error("json:{} , expression:{}", content, expression);
            throw new ApiException("通过表达式获取JSON中嵌套的值失败");
        }
        return jsonNode.asText();
    }

    public static JsonNode getJsonNodeByPath(String content, String expression) {
        JsonNode jsonNode;
        try {
            jsonNode = JacksonUtil.json().readTree(content).at(expression);
        } catch (IOException e) {
            log.error("json:{} , expression:{}", content, expression);
            throw new ApiException("通过表达式获取JSON中嵌套的值失败");
        }
        return jsonNode;
    }

    /**
     * 通过表达式获取JSON中嵌套的文本值
     *
     * @param content    JSON content to parse to build the JSON tree.
     * @param expression 表达式 例如： ”/admin/auth“
     * @return Jsonnode
     */
    public static JsonNode getJsonByPath(String content, String expression) {
        JsonNode jsonNode;
        try {
            jsonNode = JacksonUtil.json().readTree(content).at(expression);
        } catch (IOException e) {
            log.error("json:{} , expression:{}", content, expression);
            throw new ApiException("通过表达式获取JSON中嵌套的值失败");
        }
        return jsonNode;
    }

    /**
     * 通过表达式修改嵌套JSON中的值
     * <p>
     * 注意：不支持表达式 /info/app[]
     *
     * <p>
     * json格式
     * <pre>
     * {
     *   "info": {
     *     "app": {
     *       "version": "1.0"
     *     }
     * }
     * @param content            json
     * @param prefixExpression   前缀路径 例如：”/info/app“
     * @param specificKey        具体的key 例如：“version”
     * @param value              要更新的值
     * @return 新的json
     */
    public static String putByPath(String content, String prefixExpression, String specificKey, String value) {
        ObjectNode objectNode;
        try {
            if (!isJSONObjectValid(content)) {
                throw new ApiException("content must be json string!");
            }
            objectNode = JacksonUtil.json().readValue(content, ObjectNode.class);
            ObjectNode specificNode = (ObjectNode) objectNode.at(prefixExpression);
            JsonNode jsonNode = json().readTree(value);
            specificNode.set(specificKey, jsonNode);
            return json().writeValueAsString(objectNode);
        } catch (IOException e) {
            throw new ApiException("通过表达式修改JSON中嵌套的值失败");
        }
    }

    /**
     * 通过表达式获取JSON中嵌套的值(Map)
     *
     * @param content    JSON content to parse to build the JSON tree.
     * @param expression 表达式 例如： ”/admin/auth“
     * @return 嵌套的值(Map格式)
     */
    public static LinkedHashMap<String, Object> getLinkedHashMapByPath(String content, String expression) {
        return parseObject(getByPath(content, expression), LinkedHashMap.class, String.class, Object.class);
    }

    private static String formatEmptyJsonString(String jsonString, TypeReference<?> type) {
        if (type.getType() instanceof Class) {
            return formatEmptyJsonString(jsonString, (Class<?>) type.getType());
        } else {
            return formatEmptyJsonString(jsonString, (Class<?>) ((ParameterizedType) type.getType()).getRawType());
        }
    }

    private static String formatEmptyJsonString(String jsonString, Class<?> type) {
        if (StringUtils.isBlank(jsonString) || "null".equals(jsonString)) {
            if (isArrayType(type)) {
                return "[]";
            } else {
                return "{}";
            }
        }
        return jsonString;
    }

    private static boolean isArrayType(Class<?> type) {
        return type.isArray() || Collection.class.isAssignableFrom(type);
    }

    public static <T> T clone(Object from, Class<T> toClass) {
        return parseObject(toJsonString(from), toClass);
    }

    /**
     * 判断String字符串是否为json格式
     *
     * @param jsonInString json字符串
     * @return 校验结果
     */
    public static boolean isJSONObjectValid(String jsonInString) {
        if (StringUtils.isEmpty(jsonInString)) {
            return false;
        }
        try {
            JsonNode jsonNode = json().readTree(jsonInString);
            JsonNodeType nodeType = jsonNode.getNodeType();
            return JsonNodeType.OBJECT.equals(nodeType);
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 通过递归遍历进行替换来实现合并的功能
     *
     * @param mainNode   主jsonNode
     * @param updateNode 将要更新的jsonNode
     * @return 返回一个合并后的jsonNode
     */
    private static JsonNode merge(JsonNode mainNode, JsonNode updateNode) {

        Iterator<String> fieldNames = updateNode.fieldNames();
        while (fieldNames.hasNext()) {

            String fieldName = fieldNames.next();
            JsonNode jsonNode = mainNode.get(fieldName);
            // if field exists and is an embedded object
            if (jsonNode != null && jsonNode.isObject()) {
                merge(jsonNode, updateNode.get(fieldName));
            } else {
                if (mainNode instanceof ObjectNode) {
                    // Overwrite field
                    JsonNode value = updateNode.get(fieldName);
                    ((ObjectNode) mainNode).put(fieldName, value);
                }
            }

        }
        return mainNode;
    }

    /**
     * 合并两个json object字符串
     *
     * @param mainJsonStr   主json
     * @param updateJsonStr 将要更新的json
     * @return 返回一个合并后的json
     */
    public static String mergeJsonStr(String mainJsonStr, String updateJsonStr) {
        //当主json为空时,进行初始化
        if (StringUtils.isEmpty(mainJsonStr)) {
            mainJsonStr = "{}";
        }
        if (!isJSONObjectValid(mainJsonStr) || !isJSONObjectValid(updateJsonStr)) {
            throw new ApiException("待合并的两个json字符串非JsonObject格式.");
        }
        try {
            JsonNode mainNode = merge(json().readTree(mainJsonStr), json().readTree(updateJsonStr));
            return toJsonString(mainNode);
        } catch (IOException e) {
            throw new ApiException("调用readTree方法时,出现IO异常.", e);
        }
    }

    public static void add(String json,Map<String,Object> addMap) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode jsonNodes = objectMapper.readValue(json, ObjectNode.class);

        addMap.forEach((k,v)->{
            jsonNodes.put(k, "male");
        });

        String newJson = objectMapper.writeValueAsString(jsonNodes);

        System.out.println(newJson);
    }

    public static void remove(String json,Map<String,Object> removeMap) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonNodes = objectMapper.readValue(json, ObjectNode.class);
        removeMap.forEach((k,v)-> jsonNodes.remove(k));
        String newJson = objectMapper.writeValueAsString(jsonNodes);
        System.out.println(newJson);
    }

    public static void main(String[] args) throws JsonProcessingException {
        /*ObjectMapper objectMapper = new ObjectMapper();
        String mainJson = "{\"enable\":\"on\",\"event_cert_enabled\":\"off\",\"firewall_extranet_ip\":\"\",\"host_name\":\"\",\"id\":\"fcb0c804-9651-6edc-05c8-d48cfbb75660\",\"listen\":{\"backlog\":\"65535\",\"ip\":\"0.0.0.0\",\"ipv6\":\"\",\"port\":\"9889\"},\"location\":[{\"access_control_cache_level\":\"domain\",\"access_control_enable\":\"off\",\"advanced_configuration\":\"\",\"allow_cors\":\"on\",\"app_id\":\"a294bb77-b4cd-4285-b110-9e4aac3a4652\",\"app_key\":\"\",\"app_secret\":\"\",\"auto_login\":{\"ejs2_url1\":\"\",\"ejs2_url2\":\"\",\"ejs_url\":\"\",\"enable\":\"off\",\"form_request_method\":\"post\",\"js_string\":\"\",\"mode\":\"\",\"passwordKeyword\":\"\",\"request_method\":\"get\",\"usernameKeyword\":\"\"},\"backend_keepalive\":{\"enable\":\"on\",\"enable_websocket\":\"on\"},\"client_body_buffer_size\":\"128k\",\"client_body_temp_path\":\"/opt/TRP/data/0/temp\",\"client_body_timeout\":\"60s\",\"client_local_port\":\"\",\"client_max_body_size\":\"0\",\"client_ssl_protocol\":\"\",\"enable\":\"on\",\"enable_chunked_transfer_encoding\":\"on\",\"enable_x_forwarded_for\":\"on\",\"enable_x_forwarded_host\":\"on\",\"enable_x_forwarded_proto\":\"on\",\"enable_x_real_ip\":\"on\",\"friendly_error_prompt\":{\"enable\":\"on\",\"external_mapping\":\"\"},\"frontend_keepalive\":{\"enable\":\"on\",\"keepalive_requests\":\"100\",\"keepalive_timeout\":\"15s\"},\"gzip\":{\"enable\":\"off\",\"gzip_disable\":\"\",\"gzip_http_version\":\"1.1\",\"gzip_types\":\"text/html\"},\"host_rewrite\":{\"auto_proxy_redirect\":\"on\",\"host\":\"\",\"proxy_redirect\":[{\"origin\":\"\",\"replacement\":\"\"}]},\"http_security\":\"off\",\"id\":\"a294bb77-b4cd-4285-b110-9e4aac3a4652\",\"info_binding\":{\"bind_filter\":{\"exclude\":\"true\",\"mimes\":\"\",\"sites\":\"\",\"url_suffix\":\"\"},\"bind_mapping\":\"\",\"default_bind_charset\":\"UTF-8\",\"default_bind_mode\":\"cookie\",\"default_bind_url_encode\":\"on\",\"enable\":\"off\",\"sign_algorithm\":\"hmac-sha256\",\"sign_bind_enable\":\"off\",\"sign_bind_key\":\"CASC-DIGITALSIGNATURE\",\"sign_hmac_key\":\"54138789178694204349224596949811\"},\"limit_speed\":{\"conn_dry_run\":\"off\",\"conn_dry_run_log_level\":\"warn\",\"enable\":\"off\",\"limit_conn\":\"200\",\"limit_conn_log_sampling_rate\":\"10\",\"limit_conn_variable\":\"$binary_remote_addr\",\"limit_conn_zone\":\"e6a8f15b-806b-5d5d-b329-3760c5c559f0\",\"limit_req\":\"200\",\"limit_req_burst\":\"20\",\"limit_req_delay\":\"nodelay\",\"limit_req_log_sampling_rate\":\"10\",\"limit_req_variable\":\"$binary_remote_addr\",\"limit_req_zone\":\"db714dbf-bf8c-5253-98b0-61637dfe6a4a\",\"req_dry_run\":\"off\",\"req_dry_run_log_level\":\"warn\"},\"link_track\":\"on\",\"log\":{\"access_log\":{\"write_to_file\":\"on\",\"write_to_syslog\":\"on\"},\"error_log\":{\"log_level\":\"error\",\"write_to_file\":\"on\",\"write_to_syslog\":\"on\"}},\"path\":\"/\",\"proxy_bind\":\"\",\"proxy_cache\":{\"enable\":\"off\",\"proxy_cache_file_type\":\"jpeg;jpg;png;gif;ico;swf;css;js\",\"proxy_cache_valid\":\"10m\"},\"proxy_parameter\":{\"proxy_buffer_size\":\"8k\",\"proxy_buffering\":\"off\",\"proxy_buffers\":\"32\",\"proxy_busy_buffers_size\":\"8k\",\"proxy_intercept_errors\":\"inherit\"},\"proxy_pass\":\"http://a294bb77-b4cd-4285-b110-9e4aac3a4652\",\"proxy_read_timeout\":\"1m\",\"proxy_request_buffering\":\"on\",\"proxy_send_timeout\":\"1m\",\"proxy_ssl\":{\"enable\":\"off\",\"proxy_ssl_certificate\":\"\",\"proxy_ssl_ciphers\":\"ECDHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES128-SHA256:AES128-SHA:AES256-SHA:ECDHE-RSA-AES256-SHA:ECDHE-RSA-AES128-SHA:ECC-SM4-SM3:ECDHE-SM4-SM3:RSA-SM4-SM3\",\"proxy_ssl_name\":\"www.test.com\",\"proxy_ssl_protocols\":[\"TLSv1\",\"TLSv1.1\",\"TLSv1.2\"],\"proxy_ssl_server_name\":\"off\",\"proxy_ssl_session_reuse\":\"on\",\"proxy_ssl_verify\":\"off\",\"proxy_ssl_verify_depth\":\"3\"},\"resource_skywalking\":\"off\",\"resource_url\":\"http://www.baidu.com\",\"response_replace\":{\"enable\":\"off\",\"sub_filter_last_modified\":\"off\",\"sub_filter_once\":\"off\",\"sub_filter_types\":[\"*\"],\"sub_filters\":[{\"origin\":\"\",\"replacement\":\"\"}]},\"send_timeout\":\"120s\",\"sendfile\":\"on\",\"token_name\":\"X-Auth-Token\",\"token_priority\":\"off\",\"sso_verify\":\"off\",\"auto_session_find\":\"off\",\"tenant_id\":\"-1\",\"tenant_app_id\":\"a294bb77-b4cd-4285-b110-9e4aac3a4652\",\"proxy_id\":\"-1\",\"shared_state\":{},\"watermark_enable\":\"off\"}],\"server_name\":\"\",\"ssl\":{\"challenge\":{\"access_deny_on_verify_failed\":\"off\",\"signature_key\":\"gr_sign_data\",\"verify\":\"off\"},\"enable\":\"on\",\"md5_cert_enable\":\"off\",\"rsa_site_certificate\":\"\",\"sm2_site_certificate_enc\":\"\",\"sm2_site_certificate_sig\":\"\",\"ssl_ciphers\":\"ECC-SM4-SM3:ECDHE-SM4-SM3:ECC-ZUC-SM3:ECDHE-ZUC-SM3:ECDHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES128-SHA256:AES128-SHA:AES256-SHA:ECDHE-RSA-AES256-SHA:ECDHE-RSA-AES128-SHA:RSA-SM4-SM3\",\"ssl_client_sigalgs_list\":\"DSA+SHA1:ECDSA+SHA1:RSA+SHA1:DSA+SHA256:ECDSA+SHA256:RSA+SHA256:RSA-PSS+SHA256\",\"ssl_close_if_nocert\":\"off\",\"ssl_ignore_cert_validity\":\"off\",\"ssl_prefer_server_ciphers\":\"on\",\"ssl_protocols\":[\"TLSv1\",\"TLSv1.1\",\"TLSv1.2\"],\"ssl_session_ticket_keep_session_id\":\"off\",\"ssl_session_timeout\":\"1800s\",\"ssl_stapling\":{\"enable\":\"on\",\"request_method\":\"get\",\"responder\":\"\"},\"ssl_trust_local_cert_chain\":\"off\",\"ssl_verify_client\":\"off\",\"ssl_client_certificate\":[]},\"virtual_hosts_mode\":\"ip_port\",\"app_id\":\"a294bb77-b4cd-4285-b110-9e4aac3a4652\"}\n";
        String updateJson = "{\"enable\":\"on\",\"id\":\"pps\",\"listen\":{\"ip\":\"0.0.0.0\",\"ipv6\":\"[::]\",\"port\":\"443\",\"backlog\":\"65535\"},\"virtual_hosts_mode\":\"multi_location\",\"event_cert_enabled\":\"off\",\"pps_proxy_addr\":\"127.0.0.1:60500\",\"ssl\":{\"enable\":\"on\",\"rsa_site_certificate\":\"\",\"sm2_site_certificate_enc\":\"\",\"sm2_site_certificate_sig\":\"\",\"ssl_protocols\":[\"TLSv1\",\"TLSv1.1\",\"TLSv1.2\"],\"ssl_verify_client\":\"off\",\"ssl_trust_local_cert_chain\":\"off\",\"ssl_close_if_nocert\":\"off\",\"ssl_prefer_server_ciphers\":\"on\",\"ssl_ignore_cert_validity\":\"off\",\"ssl_ciphers\":\"ECDHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES128-SHA256:ECC-SM4-SM3:ECDHE-SM4-SM3:RSA-SM4-SM3\",\"ssl_client_sigalgs_list\":\"DSA+SHA1:ECDSA+SHA1:RSA+SHA1:DSA+SHA256:ECDSA+SHA256:RSA+SHA256:RSA-PSS+SHA256\",\"ssl_session_timeout\":\"3600s\",\"ssl_session_ticket_keep_session_id\":\"off\",\"md5_cert_enable\":\"off\",\"ssl_client_certificate\":[],\"ssl_verify_depth\":\"10\",\"ssl_ecdh_curve\":\"prime256v1:secp384r1\",\"ssl_session_tickets\":\"on\",\"ssl_session_ticket_sid\":\"1111111111111111\"},\"location\":[],\"ignore_invalid_headers\":\"on\",\"underscores_in_headers\":\"on\",\"client_header_timeout\":\"60s\",\"client_header_buffer_size\":\"8k\",\"large_client_header_buffers\":\"4 8k\"}\n";
        String rebaseJson = rebaseJsonStr(mainJson, updateJson);
        System.out.println("rebase:"+rebaseJson);*/
        LinkedHashMap<String,Object> rootMap = new LinkedHashMap<>();
        LinkedHashMap<String,Object> root1Map = new LinkedHashMap<>();
        ArrayList<Object> arrayList = new ArrayList<>();
        rootMap.put("test",arrayList);
        arrayList.add(root1Map);
        root1Map.put("test1","asda");
        System.out.println(rootMap);
        root1Map.put("test2",112);
        System.out.println(rootMap);

    }

    /**
     * rebase两个json object字符串
     *
     * @param mainJsonStr   主json
     * @param updateJsonStr 将要更新的json
     * @return 返回一个合并后的json
     */
    public static String rebaseJsonStr(String mainJsonStr, String updateJsonStr) {
        //当主json为空时,进行初始化
        if (StringUtils.isEmpty(mainJsonStr)) {
            mainJsonStr = "{}";
        }
        if (!isJSONObjectValid(mainJsonStr) || !isJSONObjectValid(updateJsonStr)) {
            throw new ApiException("待合并的两个json字符串非JsonObject格式.");
        }
        try {
            JsonNode mainNode = rebase(json().readTree(mainJsonStr), json().readTree(updateJsonStr));
            return toJsonString(mainNode);
        } catch (IOException e) {
            throw new ApiException("调用readTree方法时,出现IO异常.", e);
        }
    }

    /**
     * 通过递归遍历进行替换来实现合并的功能(mainNode只添加updateNode中mainNode没有的值，存在的值以mainNode为主)
     *
     * @param mainNode   主jsonNode
     * @param updateNode 将要更新的jsonNode
     * @return 返回一个合并后的jsonNode
     */
    private static JsonNode rebase(JsonNode mainNode, JsonNode updateNode) {

        Iterator<String> fieldNames = updateNode.fieldNames();
        while (fieldNames.hasNext()) {

            String fieldName = fieldNames.next();
            JsonNode jsonNode = mainNode.get(fieldName);
            // if field exists and is an embedded object
            if (jsonNode != null && jsonNode.isObject()) {
                rebase(jsonNode, updateNode.get(fieldName));
            } else if (jsonNode == null && mainNode instanceof ObjectNode){
                // Overwrite field
                JsonNode value = updateNode.get(fieldName);
                ((ObjectNode) mainNode).put(fieldName, value);
            }
        }
        return mainNode;
    }

}

