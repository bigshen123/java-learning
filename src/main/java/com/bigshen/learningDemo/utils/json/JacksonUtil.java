package com.bigshen.learningDemo.utils.json;

/**
 * @author byj
 * @date 2022/7/21
 */

import com.bigshen.learningDemo.common.exception.ApiException;
import com.bigshen.learningDemo.common.spring.Spring;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.*;
@Slf4j
@Component
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
            if (Spring.isInSpring()) {
                return Spring.getBean(Jackson2ObjectMapperBuilder.class).createXmlMapper(false).build();
            } else {
                // SpringContext在初始化过程中 SpringContextHolder.getBean(Jackson2ObjectMapperBuilder.class) 会失败
                // 这是一个关键的工具，尽可能避免执行失败
                log.warn("ApplicationContext 中还未初始化 JacksonBuilder，故使用缺省值. ");
                return Jackson2ObjectMapperBuilder.json().createXmlMapper(false).build();
            }
        } catch (Exception e) {
            // SpringContext在初始化过程中 SpringContextHolder.getBean(Jackson2ObjectMapperBuilder.class) 会失败
            // 这是一个关键的工具，尽可能避免执行失败
            log.warn("ApplicationContext 中还未初始化 JacksonBuilder，故使用缺省值. " + e.getMessage());
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
            throw new ApiException(500, "序列化资源为JSON失败", e);
        }
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
            JsonNodeType nodeType = json().readTree(jsonInString).getNodeType();
            return JsonNodeType.OBJECT == nodeType;
        } catch (IOException e) {
            return false;
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
            throw new ApiException(500, "序列化资源为JSON失败", e);
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
            throw new ApiException(500, "序列化资源为JSON失败", e);
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
            throw new ApiException(500, "序列化资源为JSON失败", e);
        }
    }


    public static Map<String, Object> parseObject(String jsonString) {
        try {
            TypeReference<Map<String, Object>> type = new TypeReference<Map<String, Object>>() {
            };
            jsonString = formatEmptyJsonString(jsonString, type);
            return json().readValue(jsonString, type);
        } catch (IOException e) {
            throw new ApiException(500, "反序列JSON资源失败", e);
        }
    }

    public static <T> T parseObject(String jsonString, Class<T> valueType) {
        try {
            jsonString = formatEmptyJsonString(jsonString, valueType);
            return json().readValue(jsonString, valueType);
        } catch (Exception e) {
            throw new ApiException(500, "反序列JSON资源失败", e);
        }
    }

    public static <T> T parseObject(String jsonString, TypeReference<T> type) {
        try {
            jsonString = formatEmptyJsonString(jsonString, type);
            return json().readValue(jsonString, type);
        } catch (IOException e) {
            throw new ApiException(500, "反序列JSON资源失败", e);
        }
    }

    public static <T> T parseObject(String jsonString, Class<?> parametrized, Class<?>... parameterClasses) {
        try {
            jsonString = formatEmptyJsonString(jsonString, parametrized);
            return json().readValue(jsonString, json().getTypeFactory().constructParametricType(parametrized, parameterClasses));
        } catch (IOException e) {
            throw new ApiException(500, "反序列JSON资源失败", e);
        }
    }

    public static <T> T parseObject(Map<String, Object> map, Class<T> valueType) {
        try {
            String jsonString = toJsonString(map);
            jsonString = formatEmptyJsonString(jsonString, valueType);
            return json().readValue(jsonString, valueType);
        } catch (IOException e) {
            throw new ApiException(500, "反序列JSON资源失败", e);
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
            throw new ApiException(500, "通过表达式获取JSON中嵌套的值失败", e);
        }
        return JacksonUtil.toJsonString(jsonNode);
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
                throw new ApiException(HttpStatus.BAD_REQUEST.value(), "content must be json string!");
            }
            objectNode = JacksonUtil.json().readValue(content, ObjectNode.class);
            ObjectNode specificNode = (ObjectNode) objectNode.at(prefixExpression);
            JsonNode jsonNode = json().readTree(value);
            specificNode.set(specificKey, jsonNode);
            return json().writeValueAsString(objectNode);
        } catch (IOException e) {
            throw new ApiException(500, "通过表达式修改JSON中嵌套的值失败", e);
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
                    ((ObjectNode) mainNode).putPOJO(fieldName, value);
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
            throw new ApiException(500, "待合并的两个json字符串非JsonObject格式.");
        }
        try {
            JsonNode mainNode = merge(json().readTree(mainJsonStr), json().readTree(updateJsonStr));
            return toJsonString(mainNode);
        } catch (IOException e) {
            throw new ApiException(500, "调用readTree方法时,出现IO异常.", e);
        }
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
            throw new ApiException(500, "待合并的两个json字符串非JsonObject格式.");
        }
        try {
            JsonNode mainNode = rebase(json().readTree(mainJsonStr), json().readTree(updateJsonStr));
            return toJsonString(mainNode);
        } catch (IOException e) {
            throw new ApiException(500, "调用readTree方法时,出现IO异常.", e);
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
            } else if (jsonNode == null && mainNode instanceof ObjectNode) {
                // Overwrite field
                JsonNode value = updateNode.get(fieldName);
                ((ObjectNode) mainNode).putPOJO(fieldName, value);
            }
        }
        return mainNode;
    }

    private static boolean isArrayType(Class<?> type) {
        return type.isArray() || Collection.class.isAssignableFrom(type);
    }

    public static <T> T clone(Object from, Class<T> toClass) {
        return parseObject(toJsonString(from), toClass);
    }

    /**
     * Json中向ObjectNode中添加数据
     *
     * @param json   原json
     * @param ex     表达式
     * @param addMap map
     * @return 处理后的json
     */
    public static String addJsonObject(String json, String ex, LinkedHashMap<String, Object> addMap) {
        ObjectNode jsonNodes;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            jsonNodes = objectMapper.readValue(json, ObjectNode.class);
            if (jsonNodes.at(ex) instanceof ObjectNode) {
                ObjectNode objectNode = (ObjectNode) jsonNodes.at(ex);
                if (objectNode != null) {
                    addMap.forEach(objectNode::putPOJO);
                }
            }
            return objectMapper.writeValueAsString(jsonNodes);
        } catch (Exception e) {
            throw new ApiException(500, "add Json ObjectNode 异常", e);
        }
    }

    /**
     * Json中向指定ArrayNode的key中添加数据
     *
     * @param json   原json
     * @param ex     表达式
     * @param addMap map
     * @return 处理后的json
     */
    public static String addJsonArray(String json, String ex, String key, LinkedHashMap<String, Object> addMap) {
        ObjectNode jsonNodes;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            jsonNodes = objectMapper.readValue(json, ObjectNode.class);
            //兼容service_instances...xxx.location[0].proxy_ssl 有list数组的情况
            if (jsonNodes.at(ex) instanceof ArrayNode) {
                ArrayNode arrayNode = (ArrayNode) jsonNodes.at(ex);
                addMap.forEach((k, v) -> {
                    ObjectNode next = (ObjectNode) arrayNode.elements().next().get(key);
                    if (next != null) {
                        next.putPOJO(k, v);
                    }
                });
            }
            return objectMapper.writeValueAsString(jsonNodes);
        } catch (Exception e) {
            throw new ApiException(500, "add Json ArrayNode 异常", e);
        }
    }

    /**
     * Json中删除 ObjectNode
     *
     * @param json      原json
     * @param ex        表达式
     * @param removeKey 需要删除的key
     * @return 处理后的json
     */
    public static String removeJsonObject(String json, String ex, String removeKey) {
        ObjectNode jsonNodes;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            jsonNodes = objectMapper.readValue(json, ObjectNode.class);
            if (jsonNodes.at(ex) instanceof ObjectNode) {
                ObjectNode objectNode = (ObjectNode) jsonNodes.at(ex);
                if (objectNode != null) {
                    objectNode.remove(removeKey);
                }
            }
            return objectMapper.writeValueAsString(jsonNodes);
        } catch (Exception e) {
            throw new ApiException(500, "remove Json ObjectNode 异常", e);
        }
    }

    /**
     * Json中删除 ArrayNode指定key下面的数据
     *
     * @param json      json
     * @param ex        ex表达式
     * @param key       指定key
     * @param removeKey 删除的key
     * @return
     */
    public static String removeJsonArray(String json, String ex, String key, String removeKey) {
        ObjectNode jsonNodes;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            jsonNodes = objectMapper.readValue(json, ObjectNode.class);
            //兼容service_instances...xxx.location[0].proxy_ssl 有list数组的情况
            if (jsonNodes.at(ex) instanceof ArrayNode) {
                ArrayNode arrayNode = (ArrayNode) jsonNodes.at(ex);
                ObjectNode next;
                //删除整个key
                if (key.equals(removeKey)) {
                    next = (ObjectNode) arrayNode.elements().next();
                } else {
                    //删除key下指定字段
                    next = (ObjectNode) arrayNode.elements().next().get(key);
                }
                if (next != null) {
                    next.remove(removeKey);
                }
            }
            return objectMapper.writeValueAsString(jsonNodes);
        } catch (Exception e) {
            throw new ApiException(500, "remove Json ArrayNode 异常", e);
        }
    }

    /**
     * Json中删除数据
     *
     * @param json       原json
     * @param ex         表达式
     * @param removeKeys 需要删除的keys
     * @return 处理后的json
     */
    public static String removeJsonList(String json, String ex, List<String> removeKeys) {
        ObjectNode jsonNodes;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            jsonNodes = objectMapper.readValue(json, ObjectNode.class);
            if (jsonNodes.at(ex) instanceof ObjectNode) {
                ObjectNode objectNode = (ObjectNode) jsonNodes.at(ex);
                removeKeys.forEach(objectNode::remove);
            }
            return objectMapper.writeValueAsString(jsonNodes);
        } catch (Exception e) {
            throw new ApiException(500, "remove Json List异常", e);
        }
    }

    public static void main(String[] args) {
        String json = "{\"id\":\"4446a227-f17e-487d-bd04-f3410d7ee9c6\",\"name\":\"test1\",\"type\":\"http\",\"enabledReverseProxy\":true,\"enabledForwardProxy\":false,\"enabledTransparentProxy\":false,\"upstream\":{\"id\":\"4446a227-f17e-487d-bd04-f3410d7ee9c6\",\"name\":\"4446a227-f17e-487d-bd04-f3410d7ee9c6\",\"upstream_load_mode\":\"none\",\"keepalive_connections\":\"100\",\"server\":[{\"ip\":\"10.0.1.25\",\"port\":\"80\",\"weight\":1,\"max_fails\":\"1\",\"fail_timeout\":\"10s\"}]},\"enabledVpn\":false,\"reverseProxy\":{\"client_header_buffer_size\":\"8k\",\"client_header_timeout\":\"60s\",\"enable\":\"on\",\"event_cert_enabled\":\"off\",\"firewall_extranet_ip\":\"\",\"host_name\":\"\",\"id\":\"e2e26d61-7954-6c54-bb87-391dd72cb684\",\"ignore_invalid_headers\":\"on\",\"large_client_header_buffers\":\"8 8k\",\"listen\":{\"backlog\":\"65535\",\"ip\":\"0.0.0.0\",\"ipv6\":\"[::]\",\"port\":\"5273\"},\"location\":[{\"access_control_cache_level\":\"domain\",\"access_control_enable\":\"off\",\"advanced_configuration\":\"\",\"allow_cors\":\"on\",\"app_id\":\"4446a227-f17e-487d-bd04-f3410d7ee9c6\",\"app_key\":\"\",\"app_secret\":\"\",\"auto_login\":{\"ejs2_url1\":\"\",\"ejs2_url2\":\"\",\"ejs_url\":\"\",\"enable\":\"off\",\"form_request_method\":\"post\",\"js_string\":\"\",\"mode\":\"\",\"passwordKeyword\":\"\",\"request_method\":\"get\",\"usernameKeyword\":\"\"},\"backend_keepalive\":{\"enable\":\"on\",\"enable_websocket\":\"on\"},\"challenge\":{\"access_deny_on_verify_failed\":\"off\",\"signature_key\":\"gr_sign_data\",\"verify\":\"off\"},\"client_body_buffer_size\":\"128k\",\"client_body_temp_path\":\"/opt/TRP/data/0/temp\",\"client_body_timeout\":\"60s\",\"client_local_port\":\"\",\"client_max_body_size\":\"0\",\"client_ssl_protocol\":\"\",\"enable\":\"on\",\"enable_chunked_transfer_encoding\":\"on\",\"enable_x_forwarded_for\":\"on\",\"enable_x_forwarded_host\":\"on\",\"enable_x_forwarded_proto\":\"on\",\"enable_x_real_ip\":\"on\",\"friendly_error_prompt\":{\"enable\":\"on\",\"external_mapping\":\"\"},\"frontend_keepalive\":{\"enable\":\"on\",\"keepalive_requests\":\"100\",\"keepalive_timeout\":\"15s\"},\"gzip\":{\"enable\":\"off\",\"gzip_disable\":\"\",\"gzip_http_version\":\"1.1\",\"gzip_types\":\"text/html\"},\"host_rewrite\":{\"auto_proxy_redirect\":\"on\",\"host\":\"\",\"proxy_redirect\":[{\"origin\":\"\",\"replacement\":\"\"}]},\"http_security\":\"off\",\"id\":\"4446a227-f17e-487d-bd04-f3410d7ee9c6\",\"info_binding\":{\"bind_filter\":{\"exclude\":\"true\",\"mimes\":\"\",\"sites\":\"\",\"url_suffix\":\"\"},\"bind_mapping\":\"\",\"default_bind_charset\":\"UTF-8\",\"default_bind_mode\":\"cookie\",\"default_bind_url_encode\":\"on\",\"enable\":\"off\",\"sign_algorithm\":\"hmac-sha256\",\"sign_bind_enable\":\"off\",\"sign_bind_key\":\"CASC-DIGITALSIGNATURE\",\"sign_hmac_key\":\"54138789178694204349224596949811\"},\"limit_speed\":{\"conn_dry_run\":\"off\",\"conn_dry_run_log_level\":\"warn\",\"enable\":\"off\",\"limit_conn\":\"200\",\"limit_conn_log_sampling_rate\":\"10\",\"limit_conn_variable\":\"$binary_remote_addr\",\"limit_conn_zone\":\"3bcca0d2-e722-5315-ad50-a1de29ccc129\",\"limit_req\":\"200\",\"limit_req_burst\":\"20\",\"limit_req_delay\":\"nodelay\",\"limit_req_log_sampling_rate\":\"10\",\"limit_req_variable\":\"$binary_remote_addr\",\"limit_req_zone\":\"3ed38c54-2dc0-5adb-9877-d14c368d87ff\",\"req_dry_run\":\"off\",\"req_dry_run_log_level\":\"warn\"},\"link_track\":\"off\",\"log\":{\"access_log\":{\"write_to_file\":\"on\",\"write_to_syslog\":\"on\"},\"error_log\":{\"log_level\":\"error\",\"write_to_file\":\"on\",\"write_to_syslog\":\"on\"}},\"path\":\"/\",\"proxy_bind\":\"\",\"proxy_cache\":{\"enable\":\"off\",\"proxy_cache_file_type\":\"jpeg;jpg;png;gif;ico;swf;css;js\",\"proxy_cache_valid\":\"10m\"},\"proxy_parameter\":{\"proxy_buffer_size\":\"8k\",\"proxy_buffering\":\"off\",\"proxy_buffers\":\"32\",\"proxy_busy_buffers_size\":\"8k\",\"proxy_intercept_errors\":\"inherit\"},\"proxy_pass\":\"http://4446a227-f17e-487d-bd04-f3410d7ee9c6\",\"proxy_read_timeout\":\"1m\",\"proxy_request_buffering\":\"on\",\"proxy_send_timeout\":\"1m\",\"proxy_ssl\":{\"enable\":\"off\",\"proxy_rsa_certificate\":\"\",\"proxy_sm2_certificate_enc\":\"\",\"proxy_sm2_certificate_sig\":\"\",\"proxy_ssl_ciphers\":\"ECDHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES128-SHA256:AES128-SHA:AES256-SHA:ECDHE-RSA-AES256-SHA:ECDHE-RSA-AES128-SHA\",\"proxy_ssl_crl\":\"\",\"proxy_ssl_name\":\"www.test.com\",\"proxy_ssl_protocols\":[\"TLSv1\",\"TLSv1.1\",\"TLSv1.2\"],\"proxy_ssl_server_name\":\"off\",\"proxy_ssl_session_reuse\":\"on\",\"proxy_ssl_sm2_ciphers\":\"ECC-SM4-SM3:ECDHE-SM4-SM3:RSA-SM4-SM3\",\"proxy_ssl_trusted_certificate\":\"\",\"proxy_ssl_verify\":\"off\",\"proxy_ssl_verify_depth\":\"10\",\"gmvpn_enable\":\"off\"},\"resource_skywalking\":\"off\",\"resource_url\":\"http://10.0.1.25\",\"response_replace\":{\"enable\":\"off\",\"sub_filter_last_modified\":\"off\",\"sub_filter_once\":\"off\",\"sub_filter_types\":[\"*\"],\"sub_filters\":[{\"origin\":\"\",\"replacement\":\"\"}]},\"send_timeout\":\"120s\",\"sendfile\":\"on\",\"token_name\":\"X-Auth-Token\",\"token_priority\":\"off\",\"sso_verify\":\"off\",\"auto_session_find\":\"off\",\"tenant_id\":\"-1\",\"tenant_app_id\":\"4446a227-f17e-487d-bd04-f3410d7ee9c6\",\"proxy_id\":\"-1\",\"shared_state\":{},\"watermark_enable\":\"off\",\"name\":\"test1\"}],\"server_name\":\"\",\"ssl\":{\"client_hello_firewall\":\"off\",\"enable\":\"on\",\"md5_cert_enable\":\"off\",\"rsa_site_certificate\":\"\",\"sm2_site_certificate_enc\":\"\",\"sm2_site_certificate_sig\":\"\",\"ssl_ciphers\":\"ECC-SM4-SM3:ECDHE-SM4-SM3:ECC-ZUC-SM3:ECDHE-ZUC-SM3:TLS_AES_128_GCM_SHA256:TLS_AES_256_GCM_SHA384:TLS_CHACHA20_POLY1305_SHA256:ECDHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES128-SHA256:AES128-SHA:AES256-SHA:ECDHE-RSA-AES256-SHA:ECDHE-RSA-AES128-SHA\",\"ssl_client_certificate\":[],\"ssl_client_sigalgs_list\":\"DSA+SHA1:ECDSA+SHA1:RSA+SHA1:DSA+SHA256:ECDSA+SHA256:RSA+SHA256:RSA-PSS+SHA256\",\"ssl_close_if_nocert\":\"off\",\"ssl_ecdh_curve\":\"prime256v1:secp384r1\",\"ssl_ignore_cert_validity\":\"off\",\"ssl_prefer_server_ciphers\":\"on\",\"ssl_protocols\":[\"TLSv1\",\"TLSv1.1\",\"TLSv1.2\",\"GMVPN\",\"TLSv1.3\"],\"ssl_session_ticket_keep_session_id\":\"off\",\"ssl_session_ticket_sid\":\"1111111111111111\",\"ssl_session_tickets\":\"on\",\"ssl_session_timeout\":\"3600s\",\"ssl_stapling\":{\"enable\":\"on\",\"request_method\":\"get\",\"responder\":\"\"},\"ssl_trust_local_cert_chain\":\"off\",\"ssl_verify_client\":\"off\",\"ssl_verify_depth\":\"10\",\"ssl_close_if_verify_failed\":\"off\"},\"underscores_in_headers\":\"on\",\"virtual_hosts_mode\":\"ip_port\",\"app_id\":\"4446a227-f17e-487d-bd04-f3410d7ee9c6\"},\"forwardProxy\":{\"access_control_cache_level\":\"domain\",\"access_control_enable\":\"off\",\"advanced_configuration\":\"\",\"app_id\":\"4446a227-f17e-487d-bd04-f3410d7ee9c6\",\"auto_login\":{\"ejs2_url1\":\"\",\"ejs2_url2\":\"\",\"ejs_url\":\"\",\"enable\":\"off\",\"form_request_method\":\"post\",\"js_string\":\"\",\"mode\":\"\",\"passwordKeyword\":\"\",\"request_method\":\"get\",\"usernameKeyword\":\"\"},\"backend_keepalive\":{\"enable\":\"on\",\"enable_websocket\":\"on\"},\"client_body_buffer_size\":\"128k\",\"client_body_temp_path\":\"/opt/TRP/data/0/temp\",\"client_body_timeout\":\"60s\",\"client_local_port\":\"\",\"client_max_body_size\":\"0\",\"client_ssl_protocol\":\"\",\"enable\":\"off\",\"enable_chunked_transfer_encoding\":\"on\",\"enable_x_forwarded_for\":\"on\",\"enable_x_forwarded_host\":\"on\",\"enable_x_forwarded_proto\":\"on\",\"enable_x_real_ip\":\"on\",\"friendly_error_prompt\":{\"enable\":\"on\",\"external_mapping\":\"\"},\"frontend_keepalive\":{\"enable\":\"on\",\"keepalive_requests\":\"100\",\"keepalive_timeout\":\"15s\"},\"gzip\":{\"enable\":\"off\",\"gzip_disable\":\"\",\"gzip_http_version\":\"1.1\",\"gzip_types\":\"text/html\"},\"host_rewrite\":{\"host\":\"\",\"proxy_redirect\":[{\"origin\":\"\",\"replacement\":\"\"}]},\"http_security\":\"off\",\"id\":\"4446a227-f17e-487d-bd04-f3410d7ee9c6\",\"info_binding\":{\"bind_filter\":{\"exclude\":\"true\",\"mimes\":\"\",\"sites\":\"\",\"url_suffix\":\"\"},\"bind_mapping\":\"\",\"default_bind_charset\":\"UTF-8\",\"default_bind_mode\":\"cookie\",\"default_bind_url_encode\":\"on\",\"enable\":\"off\",\"sign_algorithm\":\"hmac-sha256\",\"sign_bind_enable\":\"off\",\"sign_bind_key\":\"CASC-DIGITALSIGNATURE\",\"sign_hmac_key\":\"54138789178694204349224596949811\"},\"limit_speed\":{\"conn_dry_run\":\"off\",\"conn_dry_run_log_level\":\"warn\",\"enable\":\"off\",\"limit_conn\":\"200\",\"limit_conn_log_sampling_rate\":\"10\",\"limit_conn_variable\":\"$binary_remote_addr\",\"limit_conn_zone\":\"d1926710-8c15-589d-91c9-980cd120f038\",\"limit_req\":\"200\",\"limit_req_burst\":\"20\",\"limit_req_delay\":\"nodelay\",\"limit_req_log_sampling_rate\":\"10\",\"limit_req_variable\":\"$binary_remote_addr\",\"limit_req_zone\":\"7cefbc5e-28bd-52d0-a44d-4142a0799350\",\"req_dry_run\":\"off\",\"req_dry_run_log_level\":\"warn\"},\"link_track\":\"off\",\"log\":{\"access_log\":{\"write_to_file\":\"on\",\"write_to_syslog\":\"on\"},\"error_log\":{\"log_level\":\"error\",\"write_to_file\":\"on\",\"write_to_syslog\":\"on\"}},\"path\":\"http://10.0.1.25\",\"proxy_bind\":\"\",\"proxy_cache\":{\"enable\":\"off\",\"proxy_cache_file_type\":\"jpeg;jpg;png;gif;ico;swf;css;js\",\"proxy_cache_valid\":\"10m\"},\"proxy_certificate\":\"\",\"proxy_parameter\":{\"proxy_buffer_size\":\"8k\",\"proxy_buffering\":\"off\",\"proxy_buffers\":\"32\",\"proxy_busy_buffers_size\":\"8k\",\"proxy_intercept_errors\":\"inherit\"},\"proxy_pass\":\"http://4446a227-f17e-487d-bd04-f3410d7ee9c6\",\"proxy_read_timeout\":\"1m\",\"proxy_request_buffering\":\"on\",\"proxy_send_timeout\":\"1m\",\"proxy_ssl\":{\"enable\":\"off\",\"proxy_rsa_certificate\":\"\",\"proxy_sm2_certificate_enc\":\"\",\"proxy_sm2_certificate_sig\":\"\",\"proxy_ssl_ciphers\":\"ECDHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES128-SHA256:AES128-SHA:AES256-SHA:ECDHE-RSA-AES256-SHA:ECDHE-RSA-AES128-SHA\",\"proxy_ssl_crl\":\"\",\"proxy_ssl_name\":\"www.test.com\",\"proxy_ssl_protocols\":[\"TLSv1\",\"TLSv1.1\",\"TLSv1.2\"],\"proxy_ssl_server_name\":\"off\",\"proxy_ssl_session_reuse\":\"on\",\"proxy_ssl_sm2_ciphers\":\"ECC-SM4-SM3:ECDHE-SM4-SM3:RSA-SM4-SM3\",\"proxy_ssl_trusted_certificate\":\"\",\"proxy_ssl_verify\":\"off\",\"proxy_ssl_verify_depth\":\"10\"},\"resource_skywalking\":\"off\",\"resource_url\":\"http://10.0.1.25\",\"response_replace\":{\"enable\":\"off\",\"sub_filter_last_modified\":\"off\",\"sub_filter_once\":\"off\",\"sub_filter_types\":[\"*\"],\"sub_filters\":[{\"origin\":\"\",\"replacement\":\"\"}]},\"send_timeout\":\"120s\",\"sendfile\":\"on\",\"ssl\":{\"ssl_session_timeout\":\"3600s\"},\"tenant_id\":\"-1\",\"tenant_app_id\":\"4446a227-f17e-487d-bd04-f3410d7ee9c6\",\"proxy_id\":\"-1\",\"name\":\"test1\"},\"transparentProxy\":{\"access_control_cache_level\":\"domain\",\"access_control_enable\":\"off\",\"advanced_configuration\":\"\",\"app_id\":\"4446a227-f17e-487d-bd04-f3410d7ee9c6\",\"auto_login\":{\"ejs2_url1\":\"\",\"ejs2_url2\":\"\",\"ejs_url\":\"\",\"enable\":\"off\",\"form_request_method\":\"post\",\"js_string\":\"\",\"mode\":\"\",\"passwordKeyword\":\"\",\"request_method\":\"get\",\"usernameKeyword\":\"\"},\"backend_keepalive\":{\"enable\":\"on\",\"enable_websocket\":\"on\"},\"client_body_buffer_size\":\"128k\",\"client_body_temp_path\":\"/opt/TRP/data/0/temp\",\"client_body_timeout\":\"60s\",\"client_local_port\":\"\",\"client_max_body_size\":\"0\",\"client_ssl_protocol\":\"\",\"enable\":\"off\",\"enable_chunked_transfer_encoding\":\"on\",\"enable_x_forwarded_for\":\"on\",\"enable_x_forwarded_host\":\"on\",\"enable_x_forwarded_proto\":\"on\",\"enable_x_real_ip\":\"on\",\"friendly_error_prompt\":{\"enable\":\"on\",\"external_mapping\":\"\"},\"frontend_keepalive\":{\"enable\":\"on\",\"keepalive_requests\":\"100\",\"keepalive_timeout\":\"15s\"},\"gzip\":{\"enable\":\"off\",\"gzip_disable\":\"\",\"gzip_http_version\":\"1.1\",\"gzip_types\":\"text/html\"},\"host_rewrite\":{\"host\":\"\",\"proxy_redirect\":[{\"origin\":\"\",\"replacement\":\"\"}]},\"http_security\":\"off\",\"id\":\"4446a227-f17e-487d-bd04-f3410d7ee9c6\",\"info_binding\":{\"bind_filter\":{\"exclude\":\"true\",\"mimes\":\"\",\"sites\":\"\",\"url_suffix\":\"\"},\"bind_mapping\":\"\",\"default_bind_charset\":\"UTF-8\",\"default_bind_mode\":\"cookie\",\"default_bind_url_encode\":\"on\",\"enable\":\"off\",\"sign_algorithm\":\"hmac-sha256\",\"sign_bind_enable\":\"off\",\"sign_bind_key\":\"CASC-DIGITALSIGNATURE\",\"sign_hmac_key\":\"54138789178694204349224596949811\"},\"limit_speed\":{\"conn_dry_run\":\"off\",\"conn_dry_run_log_level\":\"warn\",\"enable\":\"off\",\"limit_conn\":\"200\",\"limit_conn_log_sampling_rate\":\"10\",\"limit_conn_variable\":\"$binary_remote_addr\",\"limit_conn_zone\":\"31fe4129-8442-53ec-9880-914527284878\",\"limit_req\":\"200\",\"limit_req_burst\":\"20\",\"limit_req_delay\":\"nodelay\",\"limit_req_log_sampling_rate\":\"10\",\"limit_req_variable\":\"$binary_remote_addr\",\"limit_req_zone\":\"9480ce3f-612e-5bb5-bb41-b5dd418724ac\",\"req_dry_run\":\"off\",\"req_dry_run_log_level\":\"warn\"},\"link_track\":\"off\",\"log\":{\"access_log\":{\"write_to_file\":\"on\",\"write_to_syslog\":\"on\"},\"error_log\":{\"log_level\":\"error\",\"write_to_file\":\"on\",\"write_to_syslog\":\"on\"}},\"path\":\"http://10.0.1.25\",\"proxy_bind\":\"\",\"proxy_cache\":{\"enable\":\"off\",\"proxy_cache_file_type\":\"jpeg;jpg;png;gif;ico;swf;css;js\",\"proxy_cache_valid\":\"10m\"},\"proxy_certificate\":\"\",\"proxy_parameter\":{\"proxy_buffer_size\":\"8k\",\"proxy_buffering\":\"off\",\"proxy_buffers\":\"32\",\"proxy_busy_buffers_size\":\"8k\",\"proxy_intercept_errors\":\"inherit\"},\"proxy_pass\":\"http://4446a227-f17e-487d-bd04-f3410d7ee9c6\",\"proxy_read_timeout\":\"1m\",\"proxy_request_buffering\":\"on\",\"proxy_send_timeout\":\"1m\",\"resource_skywalking\":\"off\",\"resource_url\":\"http://10.0.1.25\",\"response_replace\":{\"enable\":\"off\",\"sub_filter_last_modified\":\"off\",\"sub_filter_once\":\"off\",\"sub_filter_types\":[\"*\"],\"sub_filters\":[{\"origin\":\"\",\"replacement\":\"\"}]},\"send_timeout\":\"120s\",\"sendfile\":\"on\",\"ssl\":{\"ssl_session_timeout\":\"3600s\"},\"tenant_id\":\"-1\",\"tenant_app_id\":\"4446a227-f17e-487d-bd04-f3410d7ee9c6\",\"proxy_id\":\"-1\",\"name\":\"test1\"}}\n";
        String id = JsonPath.read(json, "$.id");
        String name = JsonPath.read(json, "$.name");
        System.out.println((String) JsonPath.read(json, "$.id"));
        System.out.println((String) JsonPath.read(json, "$.name"));
        Map<String,Object> map = JacksonUtil.parseObject(json,Map.class,String.class,Object.class);
        System.out.println(map.get("id"));
        System.out.println(map.get("name"));
    }

}
