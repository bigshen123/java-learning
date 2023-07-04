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
import com.google.common.collect.Lists;
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
            if (object == null){
                // "null"
                return "";
            }
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
        /*String json = "{\"logrotate\":{\"rotate\":\"9\",\"size\":\"10M\"},\"modelVersion\":\"2.0.0\",\"service_instances\":{\"access_control\":{\"degrade\":\"off\",\"mode\":\"default\",\"protocol\":\"pes\",\"service_url\":\"\",\"signal_degrade_interval\":\"100\"},\"backend_keepalive\":{\"enable\":\"off\",\"enable_websocket\":\"on\"},\"debug_connection\":[\"\"],\"default_portal\":{\"mode\":\"internal\",\"url\":\"\"},\"default_type\":\"text/plain\",\"enable_cookies_to_append_for_response\":\"on\",\"enable_mobile_set_cookie_expire\":\"off\",\"enable_ssl_data_fragment\":\"on\",\"event_cert_skey\":\"qKM7k9pIenlmgzk81rl+1Q==\",\"fastcgi_temp_path\":\"/opt/TRP/data/0/temp/fastcgi_temp\",\"friendly_error_prompt\":{\"enable\":\"on\",\"external_mapping\":[{\"error_code\":\"403\",\"error_page_url\":\"/error_pages/kl_403.shtml\"},{\"error_code\":\"404\",\"error_page_url\":\"/error_pages/kl_404.shtml\"}]},\"frontend_keepalive\":{\"enable\":\"on\",\"keepalive_requests\":\"100\",\"keepalive_timeout\":\"15s\"},\"hmac_secret\":\"abcdefg\",\"http_forward\":{\"server\":{\"client_header_buffer_size\":\"8k\",\"client_header_timeout\":\"60s\",\"enable\":\"on\",\"id\":\"10086\",\"ignore_invalid_headers\":\"on\",\"large_client_header_buffers\":\"8 8k\",\"listen\":{\"backlog\":\"65535\",\"ip\":\"0.0.0.0\",\"port\":\"34401\"},\"location\":\"\",\"ssl\":{\"enable\":\"on\",\"rsa_site_certificate\":\"\",\"sm2_site_certificate_enc\":\"\",\"sm2_site_certificate_sig\":\"\",\"ssl_ciphers\":\"ECC-SM4-SM3:ECDHE-SM4-SM3:ECC-ZUC-SM3:ECDHE-ZUC-SM3:ECDHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES128-SHA256:AES128-SHA:AES256-SHA:ECDHE-RSA-AES256-SHA:ECDHE-RSA-AES128-SHA:RSA-SM4-SM3\",\"ssl_client_certificate\":\"\",\"ssl_client_sigalgs_list\":\"DSA+SHA1:ECDSA+SHA1:RSA+SHA1:DSA+SHA256:ECDSA+SHA256:RSA+SHA256:RSA-PSS+SHA256\",\"ssl_close_if_nocert\":\"off\",\"ssl_close_if_verify_failed\":\"off\",\"ssl_ecdh_curve\":\"prime256v1:secp384r1\",\"ssl_ignore_cert_validity\":\"off\",\"ssl_ignore_cert_validity_time\":\"10h\",\"ssl_prefer_server_ciphers\":\"on\",\"ssl_protocols\":[\"TLSv1\",\"TLSv1.1\",\"TLSv1.2\",\"GMVPN\"],\"ssl_session_ticket_keep_session_id\":\"off\",\"ssl_session_ticket_sid\":\"1111111111111111\",\"ssl_session_tickets\":\"on\",\"ssl_session_timeout\":\"3600s\",\"ssl_trust_local_cert_chain\":\"off\",\"ssl_verify_client\":\"on\",\"ssl_verify_depth\":\"10\"},\"underscores_in_headers\":\"on\"}},\"http_reverse\":{\"server\":{\"client_header_buffer_size\":\"8k\",\"client_header_timeout\":\"60s\",\"enable\":\"on\",\"event_cert_enabled\":\"off\",\"id\":\"pps\",\"ignore_invalid_headers\":\"on\",\"large_client_header_buffers\":\"8 8k\",\"listen\":{\"backlog\":\"65535\",\"ip\":\"0.0.0.0\",\"ipv6\":\"[::]\",\"port\":\"443\"},\"location\":\"\",\"pps_proxy_addr\":\"127.0.0.1:60500\",\"ssl\":{\"enable\":\"on\",\"md5_cert_enable\":\"off\",\"rsa_site_certificate\":\"\",\"sm2_site_certificate_enc\":\"\",\"sm2_site_certificate_sig\":\"\",\"ssl_ciphers\":\"ECC-SM4-SM3:ECDHE-SM4-SM3:ECC-ZUC-SM3:ECDHE-ZUC-SM3:ECDHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES128-SHA256:AES128-SHA:AES256-SHA:ECDHE-RSA-AES256-SHA:ECDHE-RSA-AES128-SHA:RSA-SM4-SM3\",\"ssl_client_certificate\":\"\",\"ssl_client_sigalgs_list\":\"DSA+SHA1:ECDSA+SHA1:RSA+SHA1:DSA+SHA256:ECDSA+SHA256:RSA+SHA256:RSA-PSS+SHA256\",\"ssl_close_if_nocert\":\"off\",\"ssl_close_if_verify_failed\":\"off\",\"ssl_ecdh_curve\":\"prime256v1:secp384r1\",\"ssl_ignore_cert_validity\":\"off\",\"ssl_ignore_cert_validity_time\":\"10h\",\"ssl_prefer_server_ciphers\":\"on\",\"ssl_protocols\":[\"TLSv1\",\"TLSv1.1\",\"TLSv1.2\",\"GMVPN\"],\"ssl_session_ticket_keep_session_id\":\"off\",\"ssl_session_ticket_sid\":\"1111111111111111\",\"ssl_session_tickets\":\"on\",\"ssl_session_timeout\":\"3600s\",\"ssl_trust_local_cert_chain\":\"off\",\"ssl_verify_client\":\"off\",\"ssl_verify_depth\":\"10\"},\"underscores_in_headers\":\"on\",\"virtual_hosts_mode\":\"multi_location\"},\"server_pps_ssl\":{\"client_header_buffer_size\":\"8k\",\"client_header_timeout\":\"60s\",\"enable\":\"on\",\"id\":\"pps-ssl\",\"ignore_invalid_headers\":\"on\",\"large_client_header_buffers\":\"8 8k\",\"listen\":{\"backlog\":\"65535\",\"ip\":\"0.0.0.0\",\"ipv6\":\"[::]\",\"port\":\"34400\"},\"location\":\"\",\"pps_proxy_cert_verify_addr\":\"127.0.0.1:60502\",\"ssl\":{\"enable\":\"on\",\"md5_cert_enable\":\"off\",\"rsa_site_certificate\":\"\",\"sm2_site_certificate_enc\":\"\",\"sm2_site_certificate_sig\":\"\",\"ssl_ciphers\":\"ECC-SM4-SM3:ECDHE-SM4-SM3:ECC-ZUC-SM3:ECDHE-ZUC-SM3:ECDHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES128-SHA256:AES128-SHA:AES256-SHA:ECDHE-RSA-AES256-SHA:ECDHE-RSA-AES128-SHA:RSA-SM4-SM3\",\"ssl_client_certificate\":\"\",\"ssl_client_sigalgs_list\":\"DSA+SHA1:ECDSA+SHA1:RSA+SHA1:DSA+SHA256:ECDSA+SHA256:RSA+SHA256:RSA-PSS+SHA256\",\"ssl_close_if_nocert\":\"off\",\"ssl_close_if_verify_failed\":\"off\",\"ssl_ecdh_curve\":\"prime256v1:secp384r1\",\"ssl_ignore_cert_validity\":\"off\",\"ssl_ignore_cert_validity_time\":\"10h\",\"ssl_prefer_server_ciphers\":\"on\",\"ssl_protocols\":[\"TLSv1\",\"TLSv1.1\",\"TLSv1.2\",\"GMVPN\"],\"ssl_session_ticket_keep_session_id\":\"off\",\"ssl_session_ticket_sid\":\"1111111111111111\",\"ssl_session_tickets\":\"on\",\"ssl_session_timeout\":\"3600s\",\"ssl_trust_local_cert_chain\":\"off\",\"ssl_verify_client\":\"optional\",\"ssl_verify_depth\":\"10\"},\"underscores_in_headers\":\"on\",\"virtual_hosts_mode\":\"multi_location\"}},\"http_transparent\":{\"server\":{\"client_header_buffer_size\":\"8k\",\"client_header_timeout\":\"60s\",\"enable\":\"on\",\"id\":\"10088\",\"ignore_invalid_headers\":\"on\",\"large_client_header_buffers\":\"8 8k\",\"listen\":{\"backlog\":\"65535\",\"ip\":\"127.0.0.1\",\"port\":\"34403\"},\"location\":\"\",\"underscores_in_headers\":\"on\"}},\"httpclient\":{\"http_request_timeout\":\"5000\"},\"id\":\"1\",\"ignore_invalid_headers\":\"on\",\"include\":\"mime.types\",\"license_device\":\"\",\"limit_speed\":{\"cpu\":[{\"cpu_name\":\"J1900\",\"default_limit_req\":\"1000\"},{\"cpu_name\":\"i3-7\",\"default_limit_req\":\"20000\"},{\"cpu_name\":\"i5-7\",\"default_limit_req\":\"50000\"}],\"enable\":\"on\",\"limit_conn_http_zone\":\"\",\"limit_conn_tcp_zone\":\"\",\"limit_req\":\"\",\"limit_req_burst\":\"50\",\"limit_req_delay\":\"nodelay\",\"limit_req_factor\":\"0.9\",\"limit_req_log_sampling_rate\":\"10\",\"limit_req_variable\":\"global\",\"limit_req_zone\":\"global-req\",\"req_dry_run\":\"off\",\"req_dry_run_log_level\":\"warn\"},\"location_template\":{\"http_forward\":{\"modelVersion\":\"2.0.0\",\"server\":{\"enable\":\"on\",\"id\":\"10086\",\"listen\":{\"backlog\":\"65535\",\"ip\":\"0.0.0.0\",\"port\":\"34401\"},\"location\":[{\"access_control_cache_level\":\"domain\",\"access_control_enable\":\"off\",\"advanced_configuration\":\"\",\"app_id\":\"\",\"auto_login\":{\"ejs2_url1\":\"\",\"ejs2_url2\":\"\",\"ejs_url\":\"\",\"enable\":\"off\",\"form_request_method\":\"post\",\"js_string\":\"\",\"mode\":\"\",\"passwordKeyword\":\"\",\"request_method\":\"get\",\"usernameKeyword\":\"\"},\"backend_keepalive\":{\"enable\":\"off\",\"enable_websocket\":\"on\"},\"client_body_buffer_size\":\"128k\",\"client_body_temp_path\":\"/opt/TRP/data/0/temp\",\"client_body_timeout\":\"60s\",\"client_local_port\":\"\",\"client_max_body_size\":\"0\",\"client_ssl_protocol\":\"\",\"enable\":\"on\",\"enable_chunked_transfer_encoding\":\"on\",\"enable_x_forwarded_for\":\"on\",\"enable_x_forwarded_host\":\"on\",\"enable_x_forwarded_proto\":\"on\",\"enable_x_real_ip\":\"on\",\"friendly_error_prompt\":{\"enable\":\"on\",\"external_mapping\":\"\"},\"frontend_keepalive\":{\"enable\":\"on\",\"keepalive_requests\":\"100\",\"keepalive_timeout\":\"15s\"},\"gzip\":{\"enable\":\"off\",\"gzip_disable\":\"\",\"gzip_http_version\":\"1.1\",\"gzip_types\":\"text/html\"},\"host_rewrite\":{\"host\":\"\",\"proxy_redirect\":[{\"origin\":\"\",\"replacement\":\"\"}]},\"http_security\":\"off\",\"id\":\"\",\"info_binding\":{\"bind_filter\":{\"exclude\":\"true\",\"mimes\":\"\",\"sites\":\"\",\"url_suffix\":\"\"},\"bind_mapping\":\"\",\"default_bind_charset\":\"UTF-8\",\"default_bind_mode\":\"cookie\",\"default_bind_url_encode\":\"on\",\"enable\":\"off\",\"sign_algorithm\":\"hmac-sha256\",\"sign_bind_enable\":\"off\",\"sign_bind_key\":\"CASC-DIGITALSIGNATURE\",\"sign_hmac_key\":\"54138789178694204349224596949811\"},\"limit_speed\":{\"conn_dry_run\":\"off\",\"conn_dry_run_log_level\":\"warn\",\"enable\":\"off\",\"limit_conn\":\"200\",\"limit_conn_log_sampling_rate\":\"10\",\"limit_conn_variable\":\"$binary_remote_addr\",\"limit_conn_zone\":\"\",\"limit_req\":\"200\",\"limit_req_burst\":\"20\",\"limit_req_delay\":\"nodelay\",\"limit_req_log_sampling_rate\":\"10\",\"limit_req_variable\":\"$binary_remote_addr\",\"limit_req_zone\":\"\",\"req_dry_run\":\"off\",\"req_dry_run_log_level\":\"warn\"},\"link_track\":\"off\",\"log\":{\"access_log\":{\"write_to_file\":\"off\",\"write_to_syslog\":\"on\"},\"error_log\":{\"lapse_time_stamp\":\"\",\"log_level\":\"error\",\"previse_log_level\":\"\",\"write_to_file\":\"off\",\"write_to_syslog\":\"on\"}},\"path\":\"\",\"proxy_bind\":\"\",\"proxy_cache\":{\"enable\":\"off\",\"proxy_cache_file_type\":\"jpeg;jpg;png;gif;ico;swf;css;js\",\"proxy_cache_valid\":\"10m\"},\"proxy_certificate\":\"\",\"proxy_connect_timeout\":\"1m\",\"proxy_parameter\":{\"proxy_buffer_size\":\"8k\",\"proxy_buffering\":\"off\",\"proxy_buffers\":\"32\",\"proxy_busy_buffers_size\":\"8k\",\"proxy_intercept_errors\":\"inherit\"},\"proxy_pass\":\"\",\"proxy_read_timeout\":\"1m\",\"proxy_request_buffering\":\"off\",\"proxy_send_timeout\":\"1m\",\"proxy_ssl\":{\"enable\":\"off\",\"proxy_rsa_certificate\":\"\",\"proxy_sm2_certificate_enc\":\"\",\"proxy_sm2_certificate_sig\":\"\",\"proxy_ssl_ciphers\":\"ECDHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES128-SHA256:AES128-SHA:AES256-SHA:ECDHE-RSA-AES256-SHA:ECDHE-RSA-AES128-SHA\",\"proxy_ssl_crl\":\"\",\"proxy_ssl_name\":\"www.test.com\",\"proxy_ssl_protocols\":[\"TLSv1\",\"TLSv1.1\",\"TLSv1.2\"],\"proxy_ssl_server_name\":\"off\",\"proxy_ssl_session_reuse\":\"on\",\"proxy_ssl_sm2_ciphers\":\"ECC-SM4-SM3:ECDHE-SM4-SM3:RSA-SM4-SM3\",\"proxy_ssl_trusted_certificate\":\"\",\"proxy_ssl_verify\":\"off\",\"proxy_ssl_verify_depth\":\"10\"},\"resource_skywalking\":\"off\",\"resource_url\":\"\",\"response_replace\":{\"enable\":\"off\",\"sub_filter_last_modified\":\"off\",\"sub_filter_once\":\"off\",\"sub_filter_types\":[\"*\"],\"sub_filters\":[{\"origin\":\"\",\"replacement\":\"\"}]},\"send_timeout\":\"120s\",\"sendfile\":\"on\",\"ssl\":{\"ssl_session_timeout\":\"3600s\"}}]}},\"http_reverse\":{\"modelVersion\":\"2.0.0\",\"server\":{\"client_header_buffer_size\":\"8k\",\"client_header_timeout\":\"60s\",\"enable\":\"on\",\"event_cert_enabled\":\"off\",\"firewall_extranet_ip\":\"\",\"host_name\":\"\",\"id\":\"10086\",\"ignore_invalid_headers\":\"on\",\"large_client_header_buffers\":\"8 8k\",\"listen\":{\"backlog\":\"65535\",\"ip\":\"0.0.0.0\",\"ipv6\":\"[::]\",\"port\":\"443\"},\"location\":[{\"access_control_cache_level\":\"domain\",\"access_control_enable\":\"off\",\"advanced_configuration\":\"\",\"allow_cors\":\"on\",\"app_id\":\"\",\"app_key\":\"\",\"app_secret\":\"\",\"auto_login\":{\"ejs2_url1\":\"\",\"ejs2_url2\":\"\",\"ejs_url\":\"\",\"enable\":\"off\",\"form_request_method\":\"post\",\"js_string\":\"\",\"mode\":\"\",\"passwordKeyword\":\"\",\"request_method\":\"get\",\"usernameKeyword\":\"\"},\"backend_keepalive\":{\"enable\":\"off\",\"enable_websocket\":\"on\"},\"challenge\":{\"access_deny_on_verify_failed\":\"off\",\"signature_key\":\"gr_sign_data\",\"verify\":\"off\"},\"client_body_buffer_size\":\"128k\",\"client_body_temp_path\":\"/opt/TRP/data/0/temp\",\"client_body_timeout\":\"60s\",\"client_local_port\":\"\",\"client_max_body_size\":\"0\",\"client_ssl_protocol\":\"\",\"enable\":\"on\",\"enable_chunked_transfer_encoding\":\"on\",\"enable_x_forwarded_for\":\"on\",\"enable_x_forwarded_host\":\"on\",\"enable_x_forwarded_proto\":\"on\",\"enable_x_real_ip\":\"on\",\"friendly_error_prompt\":{\"enable\":\"on\",\"external_mapping\":\"\"},\"frontend_keepalive\":{\"enable\":\"on\",\"keepalive_requests\":\"100\",\"keepalive_timeout\":\"15s\"},\"gzip\":{\"enable\":\"off\",\"gzip_disable\":\"\",\"gzip_http_version\":\"1.1\",\"gzip_types\":\"text/html\"},\"host_rewrite\":{\"auto_proxy_redirect\":\"on\",\"host\":\"\",\"proxy_redirect\":[{\"origin\":\"\",\"replacement\":\"\"}]},\"http_security\":\"off\",\"id\":\"\",\"info_binding\":{\"bind_filter\":{\"exclude\":\"true\",\"mimes\":\"\",\"sites\":\"\",\"url_suffix\":\"\"},\"bind_mapping\":\"\",\"default_bind_charset\":\"UTF-8\",\"default_bind_mode\":\"cookie\",\"default_bind_url_encode\":\"on\",\"enable\":\"off\",\"sign_algorithm\":\"hmac-sha256\",\"sign_bind_enable\":\"off\",\"sign_bind_key\":\"CASC-DIGITALSIGNATURE\",\"sign_hmac_key\":\"54138789178694204349224596949811\"},\"limit_speed\":{\"conn_dry_run\":\"off\",\"conn_dry_run_log_level\":\"warn\",\"enable\":\"off\",\"limit_conn\":\"200\",\"limit_conn_log_sampling_rate\":\"10\",\"limit_conn_variable\":\"$binary_remote_addr\",\"limit_conn_zone\":\"\",\"limit_req\":\"200\",\"limit_req_burst\":\"20\",\"limit_req_delay\":\"nodelay\",\"limit_req_log_sampling_rate\":\"10\",\"limit_req_variable\":\"$binary_remote_addr\",\"limit_req_zone\":\"\",\"req_dry_run\":\"off\",\"req_dry_run_log_level\":\"warn\"},\"link_track\":\"off\",\"log\":{\"access_log\":{\"write_to_file\":\"off\",\"write_to_syslog\":\"on\"},\"error_log\":{\"lapse_time_stamp\":\"\",\"log_level\":\"error\",\"previse_log_level\":\"\",\"write_to_file\":\"off\",\"write_to_syslog\":\"on\"}},\"oauth_path\":\"/nsag-oauth-callback\",\"path\":\"\",\"proxy_bind\":\"\",\"proxy_cache\":{\"enable\":\"off\",\"proxy_cache_file_type\":\"jpeg;jpg;png;gif;ico;swf;css;js\",\"proxy_cache_valid\":\"10m\"},\"proxy_connect_timeout\":\"1m\",\"proxy_parameter\":{\"proxy_buffer_size\":\"8k\",\"proxy_buffering\":\"off\",\"proxy_buffers\":\"32\",\"proxy_busy_buffers_size\":\"8k\",\"proxy_intercept_errors\":\"inherit\"},\"proxy_pass\":\"\",\"proxy_read_timeout\":\"1m\",\"proxy_request_buffering\":\"off\",\"proxy_send_timeout\":\"1m\",\"proxy_ssl\":{\"enable\":\"off\",\"proxy_rsa_certificate\":\"\",\"proxy_sm2_certificate_enc\":\"\",\"proxy_sm2_certificate_sig\":\"\",\"proxy_ssl_ciphers\":\"ECDHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES128-SHA256:AES128-SHA:AES256-SHA:ECDHE-RSA-AES256-SHA:ECDHE-RSA-AES128-SHA\",\"proxy_ssl_crl\":\"\",\"proxy_ssl_name\":\"www.test.com\",\"proxy_ssl_protocols\":[\"TLSv1\",\"TLSv1.1\",\"TLSv1.2\"],\"proxy_ssl_server_name\":\"off\",\"proxy_ssl_session_reuse\":\"on\",\"proxy_ssl_sm2_ciphers\":\"ECC-SM4-SM3:ECDHE-SM4-SM3:RSA-SM4-SM3\",\"proxy_ssl_trusted_certificate\":\"\",\"proxy_ssl_verify\":\"off\",\"proxy_ssl_verify_depth\":\"10\"},\"resource_skywalking\":\"off\",\"resource_url\":\"\",\"response_replace\":{\"enable\":\"off\",\"sub_filter_last_modified\":\"off\",\"sub_filter_once\":\"off\",\"sub_filter_types\":[\"*\"],\"sub_filters\":[{\"origin\":\"\",\"replacement\":\"\"}]},\"send_timeout\":\"120s\",\"sendfile\":\"on\",\"token_name\":\"X-Auth-Token\",\"token_priority\":\"off\"}],\"server_name\":\"\",\"ssl\":{\"client_hello_firewall\":\"off\",\"enable\":\"on\",\"md5_cert_enable\":\"off\",\"rsa_site_certificate\":\"\",\"sm2_site_certificate_enc\":\"\",\"sm2_site_certificate_sig\":\"\",\"ssl_ciphers\":\"ECC-SM4-SM3:ECDHE-SM4-SM3:ECC-ZUC-SM3:ECDHE-ZUC-SM3:ECDHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES128-SHA256:AES128-SHA:AES256-SHA:ECDHE-RSA-AES256-SHA:ECDHE-RSA-AES128-SHA:RSA-SM4-SM3\",\"ssl_client_certificate\":\"\",\"ssl_client_sigalgs_list\":\"DSA+SHA1:ECDSA+SHA1:RSA+SHA1:DSA+SHA256:ECDSA+SHA256:RSA+SHA256:RSA-PSS+SHA256\",\"ssl_close_if_nocert\":\"off\",\"ssl_close_if_verify_failed\":\"off\",\"ssl_ecdh_curve\":\"prime256v1:secp384r1\",\"ssl_ignore_cert_validity\":\"off\",\"ssl_ignore_cert_validity_time\":\"10h\",\"ssl_prefer_server_ciphers\":\"on\",\"ssl_protocols\":[\"TLSv1\",\"TLSv1.1\",\"TLSv1.2\",\"GMVPN\"],\"ssl_session_ticket_keep_session_id\":\"off\",\"ssl_session_ticket_sid\":\"1111111111111111\",\"ssl_session_tickets\":\"on\",\"ssl_session_timeout\":\"3600s\",\"ssl_stapling\":{\"enable\":\"off\",\"request_method\":\"get\",\"responder\":\"\"},\"ssl_trust_local_cert_chain\":\"off\",\"ssl_verify_client\":\"off\",\"ssl_verify_depth\":\"10\"},\"underscores_in_headers\":\"on\",\"virtual_hosts_mode\":\"\"}},\"http_transparent\":{\"modelVersion\":\"2.0.0\",\"server\":{\"enable\":\"on\",\"id\":\"34403\",\"listen\":{\"backlog\":\"65535\",\"ip\":\"127.0.0.1\",\"port\":\"34403\"},\"location\":[{\"access_control_cache_level\":\"domain\",\"access_control_enable\":\"off\",\"advanced_configuration\":\"\",\"app_id\":\"\",\"auto_login\":{\"ejs2_url1\":\"\",\"ejs2_url2\":\"\",\"ejs_url\":\"\",\"enable\":\"off\",\"form_request_method\":\"post\",\"js_string\":\"\",\"mode\":\"\",\"passwordKeyword\":\"\",\"request_method\":\"get\",\"usernameKeyword\":\"\"},\"backend_keepalive\":{\"enable\":\"off\",\"enable_websocket\":\"on\"},\"client_body_buffer_size\":\"128k\",\"client_body_temp_path\":\"/opt/TRP/data/0/temp\",\"client_body_timeout\":\"60s\",\"client_local_port\":\"\",\"client_max_body_size\":\"0\",\"client_ssl_protocol\":\"\",\"enable\":\"on\",\"enable_chunked_transfer_encoding\":\"on\",\"enable_x_forwarded_for\":\"on\",\"enable_x_forwarded_host\":\"on\",\"enable_x_forwarded_proto\":\"on\",\"enable_x_real_ip\":\"on\",\"friendly_error_prompt\":{\"enable\":\"on\",\"external_mapping\":\"\"},\"frontend_keepalive\":{\"enable\":\"on\",\"keepalive_requests\":\"100\",\"keepalive_timeout\":\"15s\"},\"gzip\":{\"enable\":\"off\",\"gzip_disable\":\"\",\"gzip_http_version\":\"1.1\",\"gzip_types\":\"text/html\"},\"host_rewrite\":{\"host\":\"\",\"proxy_redirect\":[{\"origin\":\"\",\"replacement\":\"\"}]},\"http_security\":\"off\",\"id\":\"\",\"info_binding\":{\"bind_filter\":{\"exclude\":\"true\",\"mimes\":\"\",\"sites\":\"\",\"url_suffix\":\"\"},\"bind_mapping\":\"\",\"default_bind_charset\":\"UTF-8\",\"default_bind_mode\":\"cookie\",\"default_bind_url_encode\":\"on\",\"enable\":\"off\",\"sign_algorithm\":\"hmac-sha256\",\"sign_bind_enable\":\"off\",\"sign_bind_key\":\"CASC-DIGITALSIGNATURE\",\"sign_hmac_key\":\"54138789178694204349224596949811\"},\"limit_speed\":{\"conn_dry_run\":\"off\",\"conn_dry_run_log_level\":\"warn\",\"enable\":\"off\",\"limit_conn\":\"200\",\"limit_conn_log_sampling_rate\":\"10\",\"limit_conn_variable\":\"$binary_remote_addr\",\"limit_conn_zone\":\"\",\"limit_req\":\"200\",\"limit_req_burst\":\"20\",\"limit_req_delay\":\"nodelay\",\"limit_req_log_sampling_rate\":\"10\",\"limit_req_variable\":\"$binary_remote_addr\",\"limit_req_zone\":\"\",\"req_dry_run\":\"off\",\"req_dry_run_log_level\":\"warn\"},\"link_track\":\"off\",\"log\":{\"access_log\":{\"write_to_file\":\"off\",\"write_to_syslog\":\"on\"},\"error_log\":{\"lapse_time_stamp\":\"\",\"log_level\":\"error\",\"previse_log_level\":\"\",\"write_to_file\":\"off\",\"write_to_syslog\":\"on\"}},\"path\":\"\",\"proxy_bind\":\"\",\"proxy_cache\":{\"enable\":\"off\",\"proxy_cache_file_type\":\"jpeg;jpg;png;gif;ico;swf;css;js\",\"proxy_cache_valid\":\"10m\"},\"proxy_certificate\":\"\",\"proxy_connect_timeout\":\"1m\",\"proxy_parameter\":{\"proxy_buffer_size\":\"8k\",\"proxy_buffering\":\"off\",\"proxy_buffers\":\"32\",\"proxy_busy_buffers_size\":\"8k\",\"proxy_intercept_errors\":\"inherit\"},\"proxy_pass\":\"\",\"proxy_read_timeout\":\"1m\",\"proxy_request_buffering\":\"off\",\"proxy_send_timeout\":\"1m\",\"resource_skywalking\":\"off\",\"resource_url\":\"\",\"response_replace\":{\"enable\":\"off\",\"sub_filter_last_modified\":\"off\",\"sub_filter_once\":\"off\",\"sub_filter_types\":[\"*\"],\"sub_filters\":[{\"origin\":\"\",\"replacement\":\"\"}]},\"send_timeout\":\"120s\",\"sendfile\":\"on\",\"ssl\":{\"ssl_session_timeout\":\"3600s\"}}]}},\"tcp_forward\":{\"modelVersion\":\"2.0.0\",\"server\":{\"advanced_configuration\":\"\",\"enable\":\"on\",\"id\":\"\",\"listen\":{\"backlog\":\"\",\"ip\":\"\",\"port\":\"\"},\"resource_info\":[{\"access_control_enable\":\"off\",\"client_local_port\":\"\",\"client_ssl_protocol\":\"GMVPN\",\"enable\":\"on\",\"proxy_pass\":\"\",\"proxy_protocol\":\"off\",\"resource_id\":\"\",\"resource_url\":\"\"}]}},\"tcp_reverse\":{\"modelVersion\":\"2.0.0\",\"server\":{\"access_control_enable\":\"off\",\"advanced_configuration\":\"\",\"enable\":\"on\",\"id\":\"\",\"limit_speed\":{\"conn_dry_run\":\"off\",\"conn_dry_run_log_level\":\"warn\",\"enable\":\"off\",\"limit_conn\":\"200\",\"limit_conn_log_sampling_rate\":\"10\",\"limit_conn_variable\":\"$binary_remote_addr\",\"limit_conn_zone\":\"\"},\"listen\":{\"backlog\":\"65535\",\"ip\":\"\",\"port\":\"\"},\"log\":{\"access_log\":{\"write_to_file\":\"off\",\"write_to_syslog\":\"on\"},\"error_log\":{\"lapse_time_stamp\":\"\",\"log_level\":\"error\",\"previse_log_level\":\"\",\"write_to_file\":\"off\",\"write_to_syslog\":\"on\"}},\"proxy_buffer_size\":\"16k\",\"proxy_connect_timeout\":\"1m\",\"proxy_pass\":\"\",\"proxy_protocol\":\"off\",\"proxy_ssl\":{\"enable\":\"off\",\"proxy_rsa_certificate\":\"\",\"proxy_sm2_certificate_enc\":\"\",\"proxy_sm2_certificate_sig\":\"\",\"proxy_ssl_ciphers\":\"ECDHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES128-SHA256:AES128-SHA:AES256-SHA:ECDHE-RSA-AES256-SHA:ECDHE-RSA-AES128-SHA\",\"proxy_ssl_crl\":\"\",\"proxy_ssl_name\":\"www.test.com\",\"proxy_ssl_protocols\":[\"TLSv1\",\"TLSv1.1\",\"TLSv1.2\"],\"proxy_ssl_server_name\":\"off\",\"proxy_ssl_session_reuse\":\"on\",\"proxy_ssl_sm2_ciphers\":\"ECC-SM4-SM3:ECDHE-SM4-SM3:RSA-SM4-SM3\",\"proxy_ssl_trusted_certificate\":\"\",\"proxy_ssl_verify\":\"off\",\"proxy_ssl_verify_depth\":\"10\"},\"resource_url\":\"\",\"ssl\":{\"enable\":\"off\",\"rsa_site_certificate\":\"\",\"sm2_site_certificate_enc\":\"\",\"sm2_site_certificate_sig\":\"\",\"ssl_ciphers\":\"ECC-SM4-SM3:ECDHE-SM4-SM3:ECC-ZUC-SM3:ECDHE-ZUC-SM3:ECDHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES128-SHA256:AES128-SHA:AES256-SHA:ECDHE-RSA-AES256-SHA:ECDHE-RSA-AES128-SHA:RSA-SM4-SM3\",\"ssl_client_certificate\":\"\",\"ssl_client_sigalgs_list\":\"DSA+SHA1:ECDSA+SHA1:RSA+SHA1:DSA+SHA256:ECDSA+SHA256:RSA+SHA256:RSA-PSS+SHA256\",\"ssl_close_if_nocert\":\"off\",\"ssl_close_if_verify_failed\":\"off\",\"ssl_ecdh_curve\":\"prime256v1:secp384r1\",\"ssl_ignore_cert_validity\":\"off\",\"ssl_ignore_cert_validity_time\":\"10h\",\"ssl_prefer_server_ciphers\":\"on\",\"ssl_protocols\":[\"TLSv1\",\"TLSv1.1\",\"TLSv1.2\",\"GMVPN\"],\"ssl_session_ticket_keep_session_id\":\"off\",\"ssl_session_ticket_sid\":\"1111111111111111\",\"ssl_session_tickets\":\"on\",\"ssl_session_timeout\":\"3600s\",\"ssl_trust_local_cert_chain\":\"off\",\"ssl_verify_client\":\"off\",\"ssl_verify_depth\":\"10\"}}},\"tcp_transparent\":{\"modelVersion\":\"2.0.0\",\"server\":{\"advanced_configuration\":\"\",\"enable\":\"on\",\"id\":\"\",\"listen\":{\"backlog\":\"65535\",\"ip\":\"127.0.0.1\",\"port\":\"34404\"},\"resource_info\":[{\"access_control_enable\":\"off\",\"client_local_port\":\"\",\"client_ssl_protocol\":\"GMVPN\",\"enable\":\"on\",\"proxy_pass\":\"\",\"proxy_protocol\":\"off\",\"resource_id\":\"\",\"resource_url\":\"\"}]}},\"upstream\":{\"id\":\"\",\"keepalive_connections\":\"100\",\"modelVersion\":\"2.0.0\",\"name\":\"\",\"server\":[{\"fail_timeout\":\"10s\",\"ip\":\"\",\"max_fails\":\"1\",\"port\":\"\",\"weight\":\"1\"}],\"upstream_load_mode\":\"none\"}},\"log\":{\"access_log\":{\"access_log_filter_exclude_mode\":\"exclude\",\"access_log_filter_resource_type\":\"jpeg;jpg;png;gif;ico;swf;css;js\",\"log_format\":\"TRP_Audit\",\"log_format_template\":[{\"log_format\":[{\"name\":\"date\",\"value\":\"$time_iso8601\"},{\"name\":\"app_id\",\"value\":\"$appid\"},{\"name\":\"bytes_recv\",\"value\":\"$request_length$i\"},{\"name\":\"bytes_sent\",\"value\":\"$bytes_sent$i\"},{\"name\":\"cert_cn\",\"value\":\"$KOAL_CERT_CN\"},{\"name\":\"cert_email\",\"value\":\"$KOAL_CERT_E\"},{\"name\":\"cert_gn\",\"value\":\"$KOAL_CERT_GN\"},{\"name\":\"cert_l\",\"value\":\"$KOAL_CERT_L\"},{\"name\":\"cert_o\",\"value\":\"$KOAL_CERT_O\"},{\"name\":\"cert_ou\",\"value\":\"$KOAL_CERT_OU\"},{\"name\":\"cert_st\",\"value\":\"$KOAL_CERT_ST\"},{\"name\":\"client_id\",\"value\":\"$client_id\"},{\"name\":\"client_ip\",\"value\":\"$remote_addr\"},{\"name\":\"client_port\",\"value\":\"$remote_port\"},{\"name\":\"client_request_addr\",\"value\":\"$scheme://$server_addr:$server_port\"},{\"name\":\"client_security_mark\",\"value\":\"$client_security_mark\"},{\"name\":\"current_upstream\",\"value\":\"$proxy_scheme://$upstream_addr\"},{\"name\":\"http_host\",\"value\":\"$http_host\"},{\"name\":\"id\",\"value\":\"$request_id\"},{\"name\":\"media_type\",\"value\":\"$http_media_type\"},{\"name\":\"method\",\"value\":\"$request_method\"},{\"name\":\"multi_location\",\"value\":\"$multi_location\"},{\"name\":\"parent_span_id\",\"value\":\"$parent_span_id\"},{\"name\":\"pass_channel\",\"value\":\"$pass_channel\"},{\"name\":\"proxy_local_ip\",\"value\":\"$proxy_local_addr\"},{\"name\":\"proxy_local_port\",\"value\":\"$proxy_local_port\"},{\"name\":\"proxy_request_addr\",\"value\":\"$resource_url\"},{\"name\":\"request_args\",\"value\":\"$args\"},{\"name\":\"resource_url\",\"value\":\"$resource_url\"},{\"name\":\"result\",\"value\":\"$result\"},{\"name\":\"result_detail\",\"value\":\"\"},{\"name\":\"service_info\",\"value\":\"$server_addr:$server_port\"},{\"name\":\"session_id\",\"value\":\"$session_id\"},{\"name\":\"session_type\",\"value\":\"$session_type\"},{\"name\":\"spent\",\"value\":\"$request_time$i\"},{\"name\":\"ssl_cipher\",\"value\":\"$ssl_cipher\"},{\"name\":\"ssl_client_verify_code\",\"value\":\"$ssl_client_verify_code$i\"},{\"name\":\"ssl_handshake_code\",\"value\":\"$ssl_handshake_code$i\"},{\"name\":\"ssl_handshake_time\",\"value\":\"$ssl_handshake_time$i\"},{\"name\":\"ssl_protocol\",\"value\":\"$ssl_protocol\"},{\"name\":\"ssl_session_reused\",\"value\":\"$ssl_session_reused\"},{\"name\":\"status\",\"value\":\"$status$i\"},{\"name\":\"term_gps\",\"value\":\"$term_gps\"},{\"name\":\"term_id\",\"value\":\"$term_id\"},{\"name\":\"term_location\",\"value\":\"$term_location\"},{\"name\":\"term_model\",\"value\":\"$term_model\"},{\"name\":\"term_type\",\"value\":\"$term_type\"},{\"name\":\"trace_id\",\"value\":\"$trace_id\"},{\"name\":\"upstream_bytes_received\",\"value\":\"$upstream_bytes_received$i\"},{\"name\":\"upstream_connect_time\",\"value\":\"$upstream_connect_time$i\"},{\"name\":\"upstream_header_time\",\"value\":\"$upstream_header_time$i\"},{\"name\":\"upstream_response_time\",\"value\":\"$upstream_response_time$i\"},{\"name\":\"upstream_status\",\"value\":\"$upstream_status$i\"},{\"name\":\"url\",\"value\":\"$uri\"},{\"name\":\"user_agent\",\"value\":\"$http_user_agent\"},{\"name\":\"user_full_name\",\"value\":\"$user_full_name\"},{\"name\":\"user_group_info\",\"value\":\"$user_group_info\"},{\"name\":\"user_id\",\"value\":\"$user_id\"},{\"name\":\"user_name\",\"value\":\"$user_name\"},{\"name\":\"user_type\",\"value\":\"$user_type\"}],\"modelVersion\":\"2.0.0\",\"template_name\":\"TRP_Audit\"}],\"write_to_file\":\"off\",\"write_to_syslog\":\"on\"},\"error_log\":{\"error_log_dump_http\":\"off\",\"error_log_dump_ssl\":\"off\",\"error_log_dump_tcp\":\"off\",\"lapse_time_stamp\":\"\",\"log_level\":\"error\",\"previse_log_level\":\"\",\"write_to_file\":\"off\",\"write_to_syslog\":\"on\"},\"gw2sdk_log\":{\"write_to_syslog\":\"off\"},\"log_rotate_count\":\"5\",\"log_rotate_size\":\"5m\"},\"modelVersion\":\"2.0.0\",\"plugins\":{\"autologin\":{\"configure_path\":\"\",\"enable\":\"\",\"hook_point\":\"\",\"plugins_path\":\"\"},\"info_binding\":{\"bind_templates\":[{\"bind_mapping\":[{\"bind_name\":\"name\",\"var_src\":\"fullName\"},{\"bind_name\":\"createTime\",\"var_src\":\"createTime\"},{\"bind_name\":\"accountName\",\"var_src\":\"name\"},{\"bind_name\":\"id\",\"var_src\":\"id\"},{\"bind_name\":\"cn\",\"var_src\":\"KOAL_CERT_CN\"},{\"bind_name\":\"dn\",\"var_src\":\"KOAL_CERT_DN\"},{\"bind_name\":\"serial\",\"var_src\":\"KOAL_CERT_SERIAL_NUMBER_HEX\"},{\"bind_name\":\"client_ip\",\"var_src\":\"KOAL_CLIENT_IP\"},{\"bind_name\":\"SSL_VERIFY_CERT\",\"var_src\":\"SSL_VERIFY_CERT\"}],\"template_name\":\"default\"},{\"bind_mapping\":[{\"bind_charset\":\"UTF-8\",\"bind_name\":\"KOAL_CERT_IP\",\"bind_url_encode\":\"off\",\"var_src\":\"KOAL_CLIENT_IP\"},{\"bind_name\":\"KOAL_NOT_AFTER\",\"var_src\":\"KOAL_NOT_AFTER\"},{\"bind_name\":\"KOAL_CERT_CN\",\"var_src\":\"KOAL_CERT_CN\"},{\"bind_name\":\"KOAL_CERT_E\",\"var_src\":\"KOAL_CERT_E\"},{\"bind_name\":\"KOAL_CERT_O\",\"var_src\":\"KOAL_CERT_O\"},{\"bind_name\":\"KOAL_CERT_OU\",\"var_src\":\"KOAL_CERT_OU\"},{\"bind_name\":\"KOAL_CERT_G\",\"var_src\":\"KOAL_CERT_GN\"},{\"bind_name\":\"KOAL_CERT_ALIAS\",\"var_src\":\"KOAL_CERT_ALIAS\"},{\"bind_name\":\"KOAL_CERT_DEC_SERIAL_NUMBER\",\"var_src\":\"KOAL_CERT_SERIAL_NUMBER\"},{\"bind_name\":\"KOAL_CERT_EXT_WORK\",\"var_src\":\"KOAL_CERT_EXT_WORK\"},{\"bind_name\":\"KOAL_CERT_EXT_LEVEL\",\"var_src\":\"KOAL_CERT_EXT_LEVEL\"},{\"bind_name\":\"KOAL_CERT_EXT_CUSTOM\",\"var_src\":\"KOAL_CERT_EXT_CUSTOM\"}],\"template_name\":\"XXS\"}],\"modelVersion\":\"2.0.0\"}},\"proxy_cache\":{\"enable\":\"off\",\"proxy_cache_bypass\":\"\",\"proxy_cache_file_type\":\"jpeg;jpg;png;gif;ico;swf;css;jsa\",\"proxy_cache_keys_zone_size\":\"1m\",\"proxy_cache_max_size\":\"16m\",\"proxy_cache_path\":\"/opt/TRP/data/0/cache\",\"proxy_cache_valid\":\"10m\"},\"proxy_parameter\":{\"enable\":\"on\",\"proxy_bind\":\"off\",\"proxy_buffer_size\":\"8k\",\"proxy_buffering\":\"off\",\"proxy_buffers\":\"32\",\"proxy_busy_buffers_size\":\"8k\",\"proxy_connect_timeout\":\"1m\",\"proxy_intercept_errors\":\"off\",\"proxy_max_temp_file_size\":\"1024m\",\"proxy_next_upstream\":\"error timeout\",\"proxy_read_timeout\":\"1m\",\"proxy_send_timeout\":\"1m\"},\"proxy_ssl\":{\"proxy_ssl_protocols\":\"\"},\"proxy_temp_path\":\"/opt/TRP/data/0/temp/proxy_temp\",\"reject_request_without_host_header\":\"off\",\"resolver\":\"127.0.0.1\",\"resolver_timeout\":\"30s\",\"root\":\"/opt/TRP/data/0/\",\"scgi_temp_path\":\"/opt/TRP/data/0/temp/scgi_temp\",\"skywalking\":{\"buffer\":\"\",\"enabled\":\"off\",\"instance_name\":\"\",\"server_address\":\"\"},\"ssl\":{\"crl_verify\":{\"crl_cache_enable\":\"off\",\"crl_cache_max_entry\":\"100000\",\"ocsp_enable\":\"off\",\"ocsp_request_method\":\"get\",\"ocsp_responder\":\"\",\"ocsp_treat_unknown_status_as_revoked\":\"off\",\"ocsp_verify_response\":\"off\"},\"ssl_handshake_failed_access_log\":\"on\",\"ssl_protocols\":\"\",\"ssl_session_cache\":\"shared\",\"ssl_session_cache_max_size\":\"64m\",\"ssl_session_ticket_key\":\"sessionKey\",\"ssl_session_timeout\":\"3600s\"},\"ssl_certificate_encoding\":\"on\",\"ssl_engine\":{\"cryptoEngineEnabled\":\"off\",\"cryptoEngineName\":\"\",\"enable_engine_method\":\"ALL\",\"fullTrafficEnc\":\"off\",\"use_soft_crypto_on_failure\":\"on\"},\"tcp_forward\":{\"server\":{\"enable\":\"on\",\"id\":\"10087\",\"listen\":{\"backlog\":\"65535\",\"ip\":\"0.0.0.0\",\"port\":\"34402\"},\"proxy_pass\":\"$ssl_server_name\",\"proxy_ssl\":{\"enable\":\"off\",\"proxy_rsa_certificate\":\"\",\"proxy_sm2_certificate_enc\":\"\",\"proxy_sm2_certificate_sig\":\"\",\"proxy_ssl_ciphers\":\"ECDHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES128-SHA256:AES128-SHA:AES256-SHA:ECDHE-RSA-AES256-SHA:ECDHE-RSA-AES128-SHA\",\"proxy_ssl_crl\":\"\",\"proxy_ssl_name\":\"\",\"proxy_ssl_protocols\":[\"TLSv1\",\"TLSv1.1\",\"TLSv1.2\"],\"proxy_ssl_server_name\":\"off\",\"proxy_ssl_session_reuse\":\"on\",\"proxy_ssl_sm2_ciphers\":\"ECC-SM4-SM3:ECDHE-SM4-SM3:RSA-SM4-SM3\",\"proxy_ssl_trusted_certificate\":\"\",\"proxy_ssl_verify\":\"off\",\"proxy_ssl_verify_depth\":\"10\"},\"ssl\":{\"enable\":\"on\",\"rsa_site_certificate\":\"\",\"sm2_site_certificate_enc\":\"\",\"sm2_site_certificate_sig\":\"\",\"ssl_ciphers\":\"ECC-SM4-SM3:ECDHE-SM4-SM3:ECC-ZUC-SM3:ECDHE-ZUC-SM3:ECDHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES128-SHA256:AES128-SHA:AES256-SHA:ECDHE-RSA-AES256-SHA:ECDHE-RSA-AES128-SHA:RSA-SM4-SM3\",\"ssl_client_certificate\":\"\",\"ssl_client_sigalgs_list\":\"DSA+SHA1:ECDSA+SHA1:RSA+SHA1:DSA+SHA256:ECDSA+SHA256:RSA+SHA256:RSA-PSS+SHA256\",\"ssl_close_if_nocert\":\"off\",\"ssl_close_if_verify_failed\":\"off\",\"ssl_ecdh_curve\":\"prime256v1:secp384r1\",\"ssl_ignore_cert_validity\":\"off\",\"ssl_ignore_cert_validity_time\":\"10h\",\"ssl_prefer_server_ciphers\":\"on\",\"ssl_protocols\":[\"TLSv1\",\"TLSv1.1\",\"TLSv1.2\",\"GMVPN\"],\"ssl_session_ticket_keep_session_id\":\"off\",\"ssl_session_ticket_sid\":\"1111111111111111\",\"ssl_session_tickets\":\"on\",\"ssl_session_timeout\":\"3600s\",\"ssl_trust_local_cert_chain\":\"off\",\"ssl_verify_client\":\"on\",\"ssl_verify_depth\":\"10\"}}},\"tcp_transparent\":{\"server\":{\"enable\":\"on\",\"id\":\"10089\",\"listen\":{\"backlog\":\"65535\",\"ip\":\"127.0.0.1\",\"port\":\"34404\"},\"proxy_pass\":\"$origin_backend_addr:$origin_backend_port\"}},\"underscores_in_headers\":\"on\",\"user\":\"root root\",\"uwsgi_temp_path\":\"/opt/TRP/data/0/temp/uwsgi_temp\",\"worker_connections\":\"12500\",\"worker_cpu_affinity\":\"auto\",\"worker_processes\":\"auto\",\"worker_rlimit_core\":\"500M\",\"worker_rlimit_nofile\":\"819200\",\"working_directory\":\"/opt/TRP/data/0/\"}}\n";
        String errorPageEnable = JacksonUtil.getByPath(json, "/service_instances/friendly_error_prompt/enable");
        System.out.printf(errorPageEnable);
        String byPath = JacksonUtil.getByPath(json, "/service_instances/friendly_error_prompt/external_mapping");
        System.out.printf(byPath);*/

        /*List<String> updateKeys = Lists.newArrayList();
        updateKeys.add("cache.userSession.host");
        updateKeys.add("cache.ssoTicket.host");
        updateKeys.add("cache.crl.host");
        updateKeys.add("cache.aclPolicy.host");
        updateKeys.add("cache.taskLock.host");
        String[] strings = updateKeys.toArray(new String[0]);
        System.out.println(strings.toString());

        String[] strings1 = updateKeys.toArray(new String[]{});
        System.out.println(strings1.toString());*/


    }

}
