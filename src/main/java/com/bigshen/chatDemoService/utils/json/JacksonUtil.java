package com.bigshen.chatDemoService.utils.json;

import com.bigshen.chatDemoService.common.exception.ApiException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
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
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class JacksonUtil implements ApplicationContextAware {

    private static ObjectMapper objectMapper;
    private static final LinkedHashMap<String, Object> httpReverseMap = new LinkedHashMap<>();

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
            log.warn("ApplicationContext 中还未初始化 JacksonBuilder，故使用缺省值. ");
            return Jackson2ObjectMapperBuilder.json().createXmlMapper(false).build();
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
            throw new ApiException("序列化资源为JSON失败", e);
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
            throw new ApiException("序列化资源为JSON失败", e);
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
            throw new ApiException("序列化资源为JSON失败", e);
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
            throw new ApiException("序列化资源为JSON失败", e);
        }
    }


    public static Map<String, Object> parseObject(String jsonString) {
        try {
            TypeReference<Map<String, Object>> type = new TypeReference<Map<String, Object>>() {
            };
            jsonString = formatEmptyJsonString(jsonString, type);
            return json().readValue(jsonString, type);
        } catch (IOException e) {
            throw new ApiException("反序列JSON资源失败", e);
        }
    }

    public static <T> T parseObject(String jsonString, Class<T> valueType) {
        try {
            jsonString = formatEmptyJsonString(jsonString, valueType);
            return json().readValue(jsonString, valueType);
        } catch (Exception e) {
            throw new ApiException("反序列JSON资源失败", e);
        }
    }

    public static <T> T parseObject(String jsonString, TypeReference<T> type) {
        try {
            jsonString = formatEmptyJsonString(jsonString, type);
            return json().readValue(jsonString, type);
        } catch (IOException e) {
            throw new ApiException("反序列JSON资源失败", e);
        }
    }

    public static <T> T parseObject(String jsonString, Class<?> parametrized, Class<?>... parameterClasses) {
        try {
            jsonString = formatEmptyJsonString(jsonString, parametrized);
            return json().readValue(jsonString, json().getTypeFactory().constructParametricType(parametrized, parameterClasses));
        } catch (IOException e) {
            throw new ApiException("反序列JSON资源失败", e);
        }
    }

    public static <T> T parseObject(Map<String, Object> map, Class<T> valueType) {
        try {
            String jsonString = toJsonString(map);
            jsonString = formatEmptyJsonString(jsonString, valueType);
            return json().readValue(jsonString, valueType);
        } catch (IOException e) {
            throw new ApiException("反序列JSON资源失败", e);
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

    /**
     * 通过表达式获取JSON中嵌套的JsonNode
     *
     * @param content    JSON content to parse to build the JSON tree.
     * @param expression 表达式 例如： ”/admin/auth“
     * @return 嵌套的JsonNode
     */
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

    private static boolean isArrayType(Class<?> type) {
        return type.isArray() || Collection.class.isAssignableFrom(type);
    }

    public static <T> T clone(Object from, Class<T> toClass) {
        return parseObject(toJsonString(from), toClass);
    }

    /*public static Object addJson(String json, String ex, LinkedHashMap<String, Object> addMap) throws JsonProcessingException {
        LinkedHashMap<String, Object> oldMap = JacksonUtil.getLinkedHashMapByPath(json, ex);
        for (Map.Entry<String, Object> newEntry : addMap.entrySet()) {
            oldMap.put(newEntry.getKey(),newEntry.getValue());
        }
        return oldMap;
    }*/

    public static void main(String[] args) throws JsonProcessingException {
        String json = "{\"logrotate\":{\"rotate\":\"9\",\"size\":\"10M\"},\"service_instances\":{\"modelVersion\":\"1.0\",\"id\":\"1\",\"user\":\"root root\",\"include\":\"mime.types\",\"reject_request_without_host_header\":\"off\",\"license_device\":\"\",\"ssl_engine\":[],\"default_portal\":{\"mode\":\"internal\",\"url\":\"\"},\"working_directory\":\"/opt/TRP/data/0/\",\"worker_processes\":\"auto\",\"worker_cpu_affinity\":\"auto\",\"worker_rlimit_nofile\":\"819200\",\"worker_rlimit_core\":\"500M\",\"worker_connections\":\"12500\",\"debug_connection\":[\"\"],\"skywalking\":{\"enabled\":\"off\",\"server_address\":\"\",\"buffer\":\"\",\"instance_name\":\"\"},\"enable_ssl_data_fragment\":\"on\",\"ssl_certificate_encoding\":\"on\",\"resolver\":\"127.0.0.1\",\"resolver_timeout\":\"30s\",\"httpclient\":{\"http_request_timeout\":\"5000\"},\"root\":\"/opt/TRP/data/0/\",\"default_type\":\"text/plain\",\"proxy_temp_path\":\"/opt/TRP/data/0/temp/proxy_temp\",\"fastcgi_temp_path\":\"/opt/TRP/data/0/temp/fastcgi_temp\",\"scgi_temp_path\":\"/opt/TRP/data/0/temp/scgi_temp\",\"uwsgi_temp_path\":\"/opt/TRP/data/0/temp/uwsgi_temp\",\"ignore_invalid_headers\":\"on\",\"underscores_in_headers\":\"on\",\"enable_cookies_to_append_for_response\":\"on\",\"enable_mobile_set_cookie_expire\":\"off\",\"client_header_timeout\":\"60s\",\"client_header_buffer_size\":\"8k\",\"large_client_header_buffers\":\"4 8k\",\"event_cert_skey\":\"qKM7k9pIenlmgzk81rl+1Q==\",\"hmac_secret\":\"abcdefg\",\"plugins\":{\"info_binding\":{\"bind_templates\":[{\"template_name\":\"default\",\"bind_mapping\":[{\"bind_name\":\"name\",\"var_src\":\"fullName\"},{\"bind_name\":\"createTime\",\"var_src\":\"createTime\"},{\"bind_name\":\"accountName\",\"var_src\":\"name\"},{\"bind_name\":\"id\",\"var_src\":\"id\"},{\"bind_name\":\"cn\",\"var_src\":\"KOAL_CERT_CN\"},{\"bind_name\":\"dn\",\"var_src\":\"KOAL_CERT_DN\"},{\"bind_name\":\"serial\",\"var_src\":\"KOAL_CERT_SERIAL_NUMBER_HEX\"},{\"bind_name\":\"client_ip\",\"var_src\":\"KOAL_CLIENT_IP\"},{\"bind_name\":\"SSL_VERIFY_CERT\",\"var_src\":\"SSL_VERIFY_CERT\"}]},{\"template_name\":\"XXS\",\"bind_mapping\":[{\"bind_name\":\"KOAL_CERT_IP\",\"var_src\":\"KOAL_CLIENT_IP\",\"bind_url_encode\":\"off\",\"bind_charset\":\"UTF-8\"},{\"bind_name\":\"KOAL_NOT_AFTER\",\"var_src\":\"KOAL_NOT_AFTER\"},{\"bind_name\":\"KOAL_CERT_CN\",\"var_src\":\"KOAL_CERT_CN\"},{\"bind_name\":\"KOAL_CERT_E\",\"var_src\":\"KOAL_CERT_E\"},{\"bind_name\":\"KOAL_CERT_O\",\"var_src\":\"KOAL_CERT_O\"},{\"bind_name\":\"KOAL_CERT_OU\",\"var_src\":\"KOAL_CERT_OU\"},{\"bind_name\":\"KOAL_CERT_G\",\"var_src\":\"KOAL_CERT_GN\"},{\"bind_name\":\"KOAL_CERT_ALIAS\",\"var_src\":\"KOAL_CERT_ALIAS\"},{\"bind_name\":\"KOAL_CERT_DEC_SERIAL_NUMBER\",\"var_src\":\"KOAL_CERT_SERIAL_NUMBER\"},{\"bind_name\":\"KOAL_CERT_EXT_WORK\",\"var_src\":\"KOAL_CERT_EXT_WORK\"},{\"bind_name\":\"KOAL_CERT_EXT_LEVEL\",\"var_src\":\"KOAL_CERT_EXT_LEVEL\"},{\"bind_name\":\"KOAL_CERT_EXT_CUSTOM\",\"var_src\":\"KOAL_CERT_EXT_CUSTOM\"}]}]},\"autologin\":{\"enable\":\"\",\"hook_point\":\"\",\"plugins_path\":\"\",\"configure_path\":\"\"}},\"friendly_error_prompt\":{\"enable\":\"on\",\"external_mapping\":[]},\"ssl\":{\"enable\":\"on\",\"rsa_certificate\":\"\",\"rsa_certificate_key\":\"\",\"sm2_certificate\":\"\",\"sm2_certificate_key\":\"\",\"ssl_client_certificate\":[],\"ssl_verify_client\":\"off\",\"ssl_trust_local_cert_chain\":\"off\",\"ssl_close_if_nocert\":\"off\",\"ssl_ignore_cert_validity\":\"off\",\"ssl_protocols\":[\"TLSv1\",\"TLSv1.1\",\"TLSv1.2\",\"GMVPN\"],\"ssl_ciphers\":\"ECC-SM4-SM3:ECDHE-SM4-SM3:ECC-ZUC-SM3:ECDHE-ZUC-SM3:ECDHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES128-SHA256:AES128-SHA:AES256-SHA:ECDHE-RSA-AES256-SHA:ECDHE-RSA-AES128-SHA:RSA-SM4-SM3\",\"ssl_client_sigalgs_list\":\"DSA+SHA1:ECDSA+SHA1:RSA+SHA1:DSA+SHA256:ECDSA+SHA256:RSA+SHA256:RSA-PSS+SHA256\",\"ssl_verify_depth\":\"10\",\"ssl_ecdh_curve\":\"prime256v1:secp384r1\",\"ssl_prefer_server_ciphers\":\"on\",\"ssl_session_cache\":\"shared\",\"ssl_session_cache_max_size\":\"64m\",\"ssl_session_timeout\":\"3600s\",\"ssl_session_tickets\":\"on\",\"ssl_session_ticket_key\":\"sessionKey\",\"ssl_session_ticket_keep_session_id\":\"off\",\"crl_verify\":{\"crl_cache_enable\":\"off\",\"crl_cache_max_entry\":\"100000\",\"ocsp_enable\":\"off\",\"ocsp_responder\":\"\",\"ocsp_verify_response\":\"off\",\"ocsp_treat_unknown_status_as_revoked\":\"off\",\"ocsp_request_method\":\"get\"}},\"proxy_parameter\":{\"enable\":\"on\",\"proxy_max_temp_file_size\":\"1024m\",\"proxy_intercept_errors\":\"off\",\"proxy_connect_timeout\":\"1m\",\"proxy_send_timeout\":\"1m\",\"proxy_read_timeout\":\"1m\",\"proxy_next_upstream\":\"error timeout\",\"proxy_buffering\":\"off\",\"proxy_busy_buffers_size\":\"8k\",\"proxy_buffer_size\":\"8k\",\"proxy_bind\":\"auto\",\"proxy_buffers\":\"32\"},\"proxy_ssl\":{\"enable\":\"on\",\"proxy_rsa_certificate\":\"/opt/TRP/data/0/cert/proxy_ssl/rsa.pem\",\"proxy_rsa_certificate_key\":\"/opt/TRP/data/0/cert/proxy_ssl/rsa.key\",\"proxy_sm2_certificate\":\"/opt/TRP/data/0/cert/proxy_ssl/sm2.pem\",\"proxy_sm2_certificate_key\":\"/opt/TRP/data/0/cert/proxy_ssl/sm2.key\",\"proxy_ssl_ciphers\":\"ECDHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES128-SHA256:AES128-SHA:AES256-SHA:ECDHE-RSA-AES256-SHA:ECDHE-RSA-AES128-SHA:ECC-SM4-SM3:ECDHE-SM4-SM3:RSA-SM4-SM3\",\"proxy_ssl_crl\":\"/opt/TRP/data/0/cert/proxy_ssl/crl.pem\",\"proxy_ssl_name\":\"www.test.com\",\"proxy_ssl_protocols\":[\"TLSv1\",\"TLSv1.1\",\"TLSv1.2\"],\"proxy_ssl_server_name\":\"off\",\"proxy_ssl_session_reuse\":\"on\",\"proxy_ssl_trusted_certificate\":\"/opt/TRP/data/0/cert/proxy_ssl/ca_proxy.pem\",\"proxy_ssl_verify\":\"off\",\"proxy_ssl_verify_depth\":\"3\"},\"limit_speed\":{\"enable\":\"on\",\"req_dry_run\":\"off\",\"req_dry_run_log_level\":\"warn\",\"limit_req_variable\":\"global\",\"limit_req_zone\":\"global-req\",\"limit_req\":\"\",\"limit_req_burst\":\"50\",\"limit_req_delay\":\"nodelay\",\"limit_req_log_sampling_rate\":\"10\",\"limit_req_factor\":\"0.9\",\"cpu\":[{\"cpu_name\":\"J1900\",\"default_limit_req\":\"1000\"},{\"cpu_name\":\"i3-7\",\"default_limit_req\":\"20000\"},{\"cpu_name\":\"i5-7\",\"default_limit_req\":\"50000\"}]},\"frontend_keepalive\":{\"enable\":\"on\",\"keepalive_requests\":\"100\",\"keepalive_timeout\":\"15s\"},\"backend_keepalive\":{\"enable\":\"on\",\"enable_websocket\":\"on\"},\"proxy_cache\":{\"enable\":\"off\",\"proxy_cache_path\":\"/opt/TRP/data/0/cache\",\"proxy_cache_bypass\":\"\",\"proxy_cache_valid\":\"10m\",\"proxy_cache_keys_zone_size\":\"1m\",\"proxy_cache_max_size\":\"16m\",\"proxy_cache_file_type\":\"jpeg;jpg;png;gif;ico;swf;css;js\"},\"address_fake\":{\"enable\":\"off\",\"address_fake_item_mapping\":[{\"fake_address\":\"10.0.80.222\",\"real_address\":\"10.0.1.8\"}]},\"access_control\":{\"mode\":\"default\",\"protocol\":\"pes\",\"service_url\":\"\",\"handling_errors\":{}},\"log\":{\"log_rotate_size\":\"5m\",\"log_rotate_count\":\"5\",\"error_log\":{\"write_to_file\":\"on\",\"write_to_syslog\":\"on\",\"log_level\":\"error\",\"error_log_dump_ssl\":\"off\",\"error_log_dump_http\":\"off\"},\"access_log\":{\"write_to_file\":\"on\",\"write_to_syslog\":\"on\",\"access_log_filter_exclude_mode\":\"include\",\"access_log_filter_resource_type\":\"jpg;png;gif\",\"log_format\":\"TRP_Audit\",\"log_format_template\":[{\"template_name\":\"TRP_Audit\",\"log_format\":[{\"name\":\"date\",\"value\":\"$time_iso8601\"},{\"name\":\"id\",\"value\":\"$request_id\"},{\"name\":\"parent_span_id\",\"value\":\"$parent_span_id\"},{\"name\":\"trace_id\",\"value\":\"$trace_id\"},{\"name\":\"user_id\",\"value\":\"$user_id\"},{\"name\":\"session_id\",\"value\":\"$session_id\"},{\"name\":\"app_id\",\"value\":\"$appid\"},{\"name\":\"client_id\",\"value\":\"$client_id\"},{\"name\":\"client_ip\",\"value\":\"$remote_addr\"},{\"name\":\"client_port\",\"value\":\"$remote_port\"},{\"name\":\"client_request_addr\",\"value\":\"$scheme://$server_addr:$server_port\"},{\"name\":\"proxy_local_ip\",\"value\":\"$proxy_local_addr\"},{\"name\":\"proxy_local_port\",\"value\":\"$proxy_local_port\"},{\"name\":\"proxy_request_addr\",\"value\":\"$resource_url\"},{\"name\":\"term_id\",\"value\":\"$term_id\"},{\"name\":\"user_name\",\"value\":\"$user_name\"},{\"name\":\"user_full_name\",\"value\":\"$user_full_name\"},{\"name\":\"resource_url\",\"value\":\"$resource_url\"},{\"name\":\"method\",\"value\":\"$request_method\"},{\"name\":\"url\",\"value\":\"$url\"},{\"name\":\"status\",\"value\":\"$status$i\"},{\"name\":\"upstream_status\",\"value\":\"$upstream_status$i\"},{\"name\":\"bytes_recv\",\"value\":\"$request_length$i\"},{\"name\":\"bytes_sent\",\"value\":\"$bytes_sent$i\"},{\"name\":\"spent\",\"value\":\"$request_time$i\"},{\"name\":\"ssl_protocol\",\"value\":\"$ssl_protocol\"},{\"name\":\"ssl_cipher\",\"value\":\"$ssl_cipher\"},{\"name\":\"ssl_handshake_time\",\"value\":\"$ssl_handshake_time$i\"},{\"name\":\"ssl_session_reused\",\"value\":\"$ssl_session_reused\"},{\"name\":\"term_type\",\"value\":\"$term_type\"},{\"name\":\"term_model\",\"value\":\"$term_model\"},{\"name\":\"term_location\",\"value\":\"$term_location\"},{\"name\":\"term_gps\",\"value\":\"$term_gps\"},{\"name\":\"user_agent\",\"value\":\"$http_user_agent\"},{\"name\":\"client_security_mark\",\"value\":\"$client_security_mark\"},{\"name\":\"media_type\",\"value\":\"$http_media_type\"},{\"name\":\"result\",\"value\":\"$result\"},{\"name\":\"result_detail\",\"value\":\"\"},{\"name\":\"user_type\",\"value\":\"$user_type\"},{\"name\":\"cert_cn\",\"value\":\"$KOAL_CERT_CN\"},{\"name\":\"cert_o\",\"value\":\"$KOAL_CERT_O\"},{\"name\":\"cert_ou\",\"value\":\"$KOAL_CERT_OU\"},{\"name\":\"cert_email\",\"value\":\"$KOAL_CERT_E\"},{\"name\":\"cert_gn\",\"value\":\"$KOAL_CERT_GN\"},{\"name\":\"cert_l\",\"value\":\"$KOAL_CERT_L\"},{\"name\":\"cert_st\",\"value\":\"$KOAL_CERT_ST\"},{\"name\":\"service_info\",\"value\":\"$server_addr:$server_port\"},{\"name\":\"http_host\",\"value\":\"$http_host\"},{\"name\":\"user_group_info\",\"value\":\"$user_group_info\"},{\"name\":\"session_type\",\"value\":\"$session_type\"},{\"name\":\"upstream_connect_time\",\"value\":\"$upstream_connect_time$i\"},{\"name\":\"upstream_header_time\",\"value\":\"$upstream_header_time$i\"},{\"name\":\"upstream_response_time\",\"value\":\"$upstream_response_time$i\"},{\"name\":\"upstream_bytes_received\",\"value\":\"$upstream_bytes_received$i\"},{\"name\":\"pass_channel\",\"value\":\"$pass_channel\"}]}]}},\"location_template\":{\"http_forward\":{\"server\":{\"enable\":\"on\",\"id\":\"10086\",\"listen\":{\"ip\":\"0.0.0.0\",\"port\":\"34401\",\"backlog\":\"65535\"},\"location\":[{\"enable\":\"on\",\"id\":\"\",\"app_id\":\"\",\"path\":\"\",\"proxy_pass\":\"\",\"client_local_port\":\"\",\"client_ssl_protocol\":\"\",\"client_body_timeout\":\"60s\",\"client_body_buffer_size\":\"128k\",\"proxy_request_buffering\":\"on\",\"client_max_body_size\":\"64m\",\"client_body_temp_path\":\"/opt/TRP/data/0/temp\",\"resource_skywalking\":\"off\",\"enable_chunked_transfer_encoding\":\"on\",\"send_timeout\":\"120s\",\"sendfile\":\"on\",\"gzip\":{\"enable\":\"off\",\"gzip_types\":\"text/html\",\"gzip_disable\":\"\",\"gzip_http_version\":\"1.1\"},\"log\":{\"error_log\":{\"log_level\":\"error\",\"write_to_file\":\"on\",\"write_to_syslog\":\"on\"},\"access_log\":{\"write_to_file\":\"on\",\"write_to_syslog\":\"on\"}},\"proxy_bind\":\"\",\"resource_url\":\"\",\"http_security\":\"off\",\"access_control_enable\":\"off\",\"access_control_cache_level\":\"domain\",\"enable_x_real_ip\":\"on\",\"enable_x_forwarded_for\":\"on\",\"enable_x_forwarded_host\":\"on\",\"enable_x_forwarded_proto\":\"on\",\"proxy_certificate\":\"\",\"proxy_ssl\":{\"enable\":\"off\",\"proxy_ssl_ciphers\":\"ECDHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES128-SHA256:AES128-SHA:AES256-SHA:ECDHE-RSA-AES256-SHA:ECDHE-RSA-AES128-SHA:ECC-SM4-SM3:ECDHE-SM4-SM3:RSA-SM4-SM3\",\"proxy_ssl_name\":\"www.test.com\",\"proxy_ssl_protocols\":[\"TLSv1\",\"TLSv1.1\",\"TLSv1.2\"],\"proxy_ssl_server_name\":\"off\",\"proxy_ssl_session_reuse\":\"on\",\"proxy_ssl_verify\":\"off\",\"proxy_ssl_verify_depth\":\"3\",\"proxy_ssl_certificate\":\"\"},\"limit_speed\":{\"enable\":\"off\",\"req_dry_run\":\"off\",\"req_dry_run_log_level\":\"warn\",\"conn_dry_run\":\"off\",\"conn_dry_run_log_level\":\"warn\",\"limit_req_variable\":\"$binary_remote_addr\",\"limit_conn_variable\":\"$binary_remote_addr\",\"limit_conn_zone\":\"\",\"limit_conn\":\"200\",\"limit_req_zone\":\"\",\"limit_req\":\"200\",\"limit_req_burst\":\"20\",\"limit_req_delay\":\"nodelay\",\"limit_req_log_sampling_rate\":\"10\",\"limit_conn_log_sampling_rate\":\"10\"},\"ssl\":{\"ssl_session_timeout\":\"3600s\"},\"host_rewrite\":{\"host\":\"\",\"proxy_redirect\":[{\"origin\":\"\",\"replacement\":\"\"}]},\"friendly_error_prompt\":{\"enable\":\"on\",\"external_mapping\":[]},\"frontend_keepalive\":{\"enable\":\"on\",\"keepalive_requests\":\"100\",\"keepalive_timeout\":\"15s\"},\"backend_keepalive\":{\"enable\":\"on\",\"enable_websocket\":\"on\"},\"proxy_read_timeout\":\"1m\",\"proxy_send_timeout\":\"1m\",\"info_binding\":{\"enable\":\"off\",\"sign_bind_enable\":\"off\",\"sign_bind_key\":\"CASC-DIGITALSIGNATURE\",\"sign_algorithm\":\"hmac-sha256\",\"sign_hmac_key\":\"54138789178694204349224596949811\",\"bind_filter\":{\"exclude\":\"true\",\"url_suffix\":[],\"sites\":[],\"mimes\":[]},\"default_bind_mode\":\"cookie\",\"default_bind_url_encode\":\"on\",\"bind_mapping\":[],\"default_bind_charset\":\"UTF-8\"},\"response_replace\":{\"enable\":\"off\",\"sub_filter_once\":\"off\",\"sub_filter_types\":[\"*\"],\"sub_filter_last_modified\":\"off\",\"sub_filters\":[{\"origin\":\"\",\"replacement\":\"\"}]},\"proxy_cache\":{\"enable\":\"off\",\"proxy_cache_valid\":\"10m\",\"proxy_cache_file_type\":\"jpeg;jpg;png;gif;ico;swf;css;js\"},\"proxy_parameter\":{\"proxy_intercept_errors\":\"inherit\",\"proxy_buffering\":\"off\",\"proxy_busy_buffers_size\":\"8k\",\"proxy_buffer_size\":\"8k\",\"proxy_buffers\":\"32\"},\"auto_login\":{\"enable\":\"off\",\"mode\":\"\",\"ejs_url\":\"\",\"ejs2_url1\":\"\",\"ejs2_url2\":\"\",\"request_method\":\"get\",\"form_request_method\":\"post\",\"usernameKeyword\":\"\",\"passwordKeyword\":\"\",\"js_string\":\"\"},\"link_track\":\"on\",\"advanced_configuration\":\"\"}]}},\"http_reverse\":{\"server\":{\"enable\":\"on\",\"id\":\"10086\",\"listen\":{\"ip\":\"0.0.0.0\",\"ipv6\":\"[::]\",\"port\":\"443\",\"backlog\":\"65535\"},\"firewall_extranet_ip\":\"\",\"server_name\":\"\",\"host_name\":\"\",\"event_cert_enabled\":\"off\",\"virtual_hosts_mode\":\"\",\"ssl\":{\"enable\":\"on\",\"rsa_site_certificate\":\"\",\"sm2_site_certificate_enc\":\"\",\"sm2_site_certificate_sig\":\"\",\"ssl_protocols\":[\"TLSv1\",\"TLSv1.1\",\"TLSv1.2\"],\"ssl_verify_client\":\"off\",\"ssl_trust_local_cert_chain\":\"off\",\"ssl_close_if_nocert\":\"off\",\"ssl_prefer_server_ciphers\":\"on\",\"ssl_ignore_cert_validity\":\"off\",\"ssl_ciphers\":\"ECC-SM4-SM3:ECDHE-SM4-SM3:ECC-ZUC-SM3:ECDHE-ZUC-SM3:ECDHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES128-SHA256:AES128-SHA:AES256-SHA:ECDHE-RSA-AES256-SHA:ECDHE-RSA-AES128-SHA:RSA-SM4-SM3\",\"ssl_client_sigalgs_list\":\"DSA+SHA1:ECDSA+SHA1:RSA+SHA1:DSA+SHA256:ECDSA+SHA256:RSA+SHA256:RSA-PSS+SHA256\",\"ssl_session_ticket_keep_session_id\":\"off\",\"ssl_session_timeout\":\"3600s\",\"md5_cert_enable\":\"off\",\"challenge\":{\"verify\":\"off\",\"signature_key\":\"gr_sign_data\",\"access_deny_on_verify_failed\":\"off\"},\"ssl_stapling\":{\"enable\":\"on\",\"request_method\":\"get\",\"responder\":\"\"}},\"location\":[{\"enable\":\"on\",\"id\":\"\",\"app_id\":\"\",\"app_key\":\"\",\"app_secret\":\"\",\"oauth_path\":\"/nsag-oauth-callback\",\"path\":\"\",\"proxy_pass\":\"\",\"resource_skywalking\":\"off\",\"client_local_port\":\"\",\"client_ssl_protocol\":\"\",\"client_body_timeout\":\"60s\",\"client_body_buffer_size\":\"128k\",\"proxy_request_buffering\":\"on\",\"client_max_body_size\":\"0\",\"client_body_temp_path\":\"/opt/TRP/data/0/temp\",\"enable_chunked_transfer_encoding\":\"on\",\"send_timeout\":\"120s\",\"sendfile\":\"on\",\"gzip\":{\"enable\":\"off\",\"gzip_types\":\"text/html\",\"gzip_disable\":\"\",\"gzip_http_version\":\"1.1\"},\"log\":{\"error_log\":{\"log_level\":\"error\",\"write_to_file\":\"on\",\"write_to_syslog\":\"on\"},\"access_log\":{\"write_to_file\":\"on\",\"write_to_syslog\":\"on\"}},\"proxy_bind\":\"\",\"proxy_read_timeout\":\"1m\",\"proxy_send_timeout\":\"1m\",\"resource_url\":\"\",\"http_security\":\"off\",\"access_control_enable\":\"off\",\"access_control_cache_level\":\"domain\",\"enable_x_real_ip\":\"on\",\"enable_x_forwarded_for\":\"on\",\"enable_x_forwarded_host\":\"on\",\"enable_x_forwarded_proto\":\"on\",\"token_priority\":\"off\",\"allow_cors\":\"on\",\"token_name\":\"X-Auth-Token\",\"limit_speed\":{\"enable\":\"off\",\"req_dry_run\":\"off\",\"req_dry_run_log_level\":\"warn\",\"conn_dry_run\":\"off\",\"conn_dry_run_log_level\":\"warn\",\"limit_req_variable\":\"$binary_remote_addr\",\"limit_conn_variable\":\"$binary_remote_addr\",\"limit_conn_zone\":\"\",\"limit_conn\":\"200\",\"limit_req_zone\":\"\",\"limit_req\":\"200\",\"limit_req_burst\":\"20\",\"limit_req_delay\":\"nodelay\",\"limit_req_log_sampling_rate\":\"10\",\"limit_conn_log_sampling_rate\":\"10\"},\"host_rewrite\":{\"host\":\"\",\"auto_proxy_redirect\":\"on\",\"proxy_redirect\":[{\"origin\":\"\",\"replacement\":\"\"}]},\"friendly_error_prompt\":{\"enable\":\"on\",\"external_mapping\":[]},\"frontend_keepalive\":{\"enable\":\"on\",\"keepalive_requests\":\"100\",\"keepalive_timeout\":\"15s\"},\"backend_keepalive\":{\"enable\":\"on\",\"enable_websocket\":\"on\"},\"info_binding\":{\"enable\":\"off\",\"sign_bind_enable\":\"off\",\"sign_bind_key\":\"CASC-DIGITALSIGNATURE\",\"sign_algorithm\":\"hmac-sha256\",\"sign_hmac_key\":\"54138789178694204349224596949811\",\"bind_filter\":{\"exclude\":\"true\",\"url_suffix\":[],\"sites\":[],\"mimes\":[]},\"default_bind_mode\":\"cookie\",\"default_bind_url_encode\":\"on\",\"bind_mapping\":[],\"default_bind_charset\":\"UTF-8\"},\"response_replace\":{\"enable\":\"off\",\"sub_filter_once\":\"off\",\"sub_filter_types\":[\"*\"],\"sub_filter_last_modified\":\"off\",\"sub_filters\":[{\"origin\":\"\",\"replacement\":\"\"}]},\"proxy_cache\":{\"enable\":\"off\",\"proxy_cache_valid\":\"10m\",\"proxy_cache_file_type\":\"jpeg;jpg;png;gif;ico;swf;css;js\"},\"proxy_parameter\":{\"proxy_intercept_errors\":\"inherit\",\"proxy_buffering\":\"off\",\"proxy_busy_buffers_size\":\"8k\",\"proxy_buffer_size\":\"8k\",\"proxy_buffers\":\"32\"},\"proxy_ssl\":{\"enable\":\"off\",\"proxy_ssl_ciphers\":\"ECDHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES128-SHA256:AES128-SHA:AES256-SHA:ECDHE-RSA-AES256-SHA:ECDHE-RSA-AES128-SHA:ECC-SM4-SM3:ECDHE-SM4-SM3:RSA-SM4-SM3\",\"proxy_ssl_name\":\"www.test.com\",\"proxy_ssl_protocols\":[\"TLSv1\",\"TLSv1.1\",\"TLSv1.2\"],\"proxy_ssl_server_name\":\"off\",\"proxy_ssl_session_reuse\":\"on\",\"proxy_ssl_verify\":\"off\",\"proxy_ssl_verify_depth\":\"3\",\"proxy_ssl_certificate\":\"\"},\"auto_login\":{\"enable\":\"off\",\"mode\":\"\",\"ejs_url\":\"\",\"ejs2_url1\":\"\",\"ejs2_url2\":\"\",\"request_method\":\"get\",\"form_request_method\":\"post\",\"usernameKeyword\":\"\",\"passwordKeyword\":\"\",\"js_string\":\"\"},\"link_track\":\"on\",\"advanced_configuration\":\"\"}]}},\"http_transparent\":{\"server\":{\"enable\":\"on\",\"id\":\"34403\",\"listen\":{\"ip\":\"127.0.0.1\",\"port\":\"34403\",\"backlog\":\"65535\"},\"ssl\":{\"enable\":\"off\",\"rsa_site_certificate\":\"\",\"sm2_site_certificate_enc\":\"\",\"sm2_site_certificate_sig\":\"\",\"ssl_protocols\":[\"TLSv1\",\"TLSv1.1\",\"TLSv1.2\"],\"ssl_verify_client\":\"off\",\"ssl_trust_local_cert_chain\":\"off\",\"ssl_close_if_nocert\":\"off\",\"ssl_ignore_cert_validity\":\"off\",\"ssl_ciphers\":\"ECDHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES128-SHA256:ECC-SM4-SM3:ECDHE-SM4-SM3:RSA-SM4-SM3\",\"ssl_client_sigalgs_list\":\"DSA+SHA1:ECDSA+SHA1:RSA+SHA1:DSA+SHA256:ECDSA+SHA256:RSA+SHA256:RSA-PSS+SHA256\",\"ssl_session_ticket_keep_session_id\":\"off\"},\"location\":[{\"enable\":\"on\",\"id\":\"\",\"app_id\":\"\",\"path\":\"\",\"proxy_pass\":\"\",\"resource_skywalking\":\"off\",\"client_local_port\":\"\",\"client_ssl_protocol\":\"\",\"client_body_timeout\":\"60s\",\"client_body_buffer_size\":\"128k\",\"proxy_request_buffering\":\"on\",\"client_max_body_size\":\"64m\",\"client_body_temp_path\":\"/opt/TRP/data/0/temp\",\"enable_chunked_transfer_encoding\":\"on\",\"send_timeout\":\"120s\",\"sendfile\":\"on\",\"gzip\":{\"enable\":\"off\",\"gzip_types\":\"text/html\",\"gzip_disable\":\"\",\"gzip_http_version\":\"1.1\"},\"log\":{\"error_log\":{\"log_level\":\"error\",\"write_to_file\":\"on\",\"write_to_syslog\":\"on\"},\"access_log\":{\"write_to_file\":\"on\",\"write_to_syslog\":\"on\"}},\"ssl\":{\"ssl_session_timeout\":\"3600s\"},\"proxy_bind\":\"\",\"proxy_read_timeout\":\"1m\",\"proxy_send_timeout\":\"1m\",\"resource_url\":\"\",\"http_security\":\"off\",\"access_control_enable\":\"off\",\"access_control_cache_level\":\"domain\",\"enable_x_real_ip\":\"on\",\"enable_x_forwarded_for\":\"on\",\"enable_x_forwarded_host\":\"on\",\"enable_x_forwarded_proto\":\"on\",\"proxy_certificate\":\"\",\"proxy_ssl\":{\"enable\":\"off\",\"proxy_ssl_ciphers\":\"ECDHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES128-SHA256:AES128-SHA:AES256-SHA:ECDHE-RSA-AES256-SHA:ECDHE-RSA-AES128-SHA:ECC-SM4-SM3:ECDHE-SM4-SM3:RSA-SM4-SM3\",\"proxy_ssl_name\":\"www.test.com\",\"proxy_ssl_protocols\":[\"TLSv1\",\"TLSv1.1\",\"TLSv1.2\"],\"proxy_ssl_server_name\":\"off\",\"proxy_ssl_session_reuse\":\"on\",\"proxy_ssl_verify\":\"off\",\"proxy_ssl_verify_depth\":\"3\",\"proxy_ssl_certificate\":\"\"},\"limit_speed\":{\"enable\":\"off\",\"req_dry_run\":\"off\",\"req_dry_run_log_level\":\"warn\",\"conn_dry_run\":\"off\",\"conn_dry_run_log_level\":\"warn\",\"limit_req_variable\":\"$binary_remote_addr\",\"limit_conn_variable\":\"$binary_remote_addr\",\"limit_conn_zone\":\"\",\"limit_conn\":\"200\",\"limit_req_zone\":\"\",\"limit_req\":\"200\",\"limit_req_burst\":\"20\",\"limit_req_delay\":\"nodelay\",\"limit_req_log_sampling_rate\":\"10\",\"limit_conn_log_sampling_rate\":\"10\"},\"host_rewrite\":{\"host\":\"\",\"proxy_redirect\":[{\"origin\":\"\",\"replacement\":\"\"}]},\"friendly_error_prompt\":{\"enable\":\"on\",\"external_mapping\":[]},\"frontend_keepalive\":{\"enable\":\"on\",\"keepalive_requests\":\"100\",\"keepalive_timeout\":\"15s\"},\"backend_keepalive\":{\"enable\":\"on\",\"enable_websocket\":\"on\"},\"info_binding\":{\"enable\":\"off\",\"sign_bind_enable\":\"off\",\"sign_bind_key\":\"CASC-DIGITALSIGNATURE\",\"sign_algorithm\":\"hmac-sha256\",\"sign_hmac_key\":\"54138789178694204349224596949811\",\"bind_filter\":{\"exclude\":\"true\",\"url_suffix\":[],\"sites\":[],\"mimes\":[]},\"default_bind_mode\":\"cookie\",\"default_bind_url_encode\":\"on\",\"bind_mapping\":[],\"default_bind_charset\":\"UTF-8\"},\"response_replace\":{\"enable\":\"off\",\"sub_filter_once\":\"off\",\"sub_filter_types\":[\"*\"],\"sub_filter_last_modified\":\"off\",\"sub_filters\":[{\"origin\":\"\",\"replacement\":\"\"}]},\"proxy_cache\":{\"enable\":\"off\",\"proxy_cache_valid\":\"10m\",\"proxy_cache_file_type\":\"jpeg;jpg;png;gif;ico;swf;css;js\"},\"proxy_parameter\":{\"proxy_intercept_errors\":\"inherit\",\"proxy_buffering\":\"off\",\"proxy_busy_buffers_size\":\"8k\",\"proxy_buffer_size\":\"8k\",\"proxy_buffers\":\"32\"},\"auto_login\":{\"enable\":\"off\",\"mode\":\"\",\"ejs_url\":\"\",\"ejs2_url1\":\"\",\"ejs2_url2\":\"\",\"request_method\":\"get\",\"form_request_method\":\"post\",\"usernameKeyword\":\"\",\"passwordKeyword\":\"\",\"js_string\":\"\"},\"link_track\":\"on\",\"advanced_configuration\":\"\"}]}},\"tcp_forward\":{\"server\":{\"enable\":\"on\",\"id\":\"\",\"listen\":{\"ip\":\"\",\"port\":\"\",\"backlog\":\"\"},\"advanced_configuration\":\"\",\"resource_info\":[{\"enable\":\"on\",\"client_local_port\":\"\",\"client_ssl_protocol\":\"GMVPN\",\"resource_url\":\"\",\"resource_id\":\"\",\"proxy_protocol\":\"off\",\"proxy_pass\":\"\",\"access_control_enable\":\"off\"}]}},\"tcp_transparent\":{\"server\":{\"enable\":\"on\",\"id\":\"\",\"listen\":{\"ip\":\"127.0.0.1\",\"port\":\"34404\",\"backlog\":\"65535\"},\"advanced_configuration\":\"\",\"resource_info\":[{\"enable\":\"on\",\"client_local_port\":\"\",\"client_ssl_protocol\":\"GMVPN\",\"resource_url\":\"\",\"resource_id\":\"\",\"proxy_protocol\":\"off\",\"proxy_pass\":\"\",\"access_control_enable\":\"off\"}]}},\"tcp_reverse\":{\"server\":{\"enable\":\"on\",\"id\":\"\",\"listen\":{\"ip\":\"\",\"port\":\"\",\"backlog\":\"65535\"},\"log\":{\"error_log\":{\"log_level\":\"error\",\"write_to_file\":\"on\",\"write_to_syslog\":\"on\"},\"access_log\":{\"write_to_file\":\"on\",\"write_to_syslog\":\"on\"}},\"ssl\":{\"enable\":\"off\",\"rsa_site_certificate\":\"\",\"sm2_site_certificate_enc\":\"\",\"sm2_site_certificate_sig\":\"\",\"ssl_protocols\":[\"TLSv1\",\"TLSv1.1\",\"TLSv1.2\"],\"ssl_verify_client\":\"off\",\"ssl_ciphers\":\"ECDHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES128-SHA256:ECC-SM4-SM3:ECDHE-SM4-SM3:RSA-SM4-SM3\",\"ssl_client_sigalgs_list\":\"DSA+SHA1:ECDSA+SHA1:RSA+SHA1:DSA+SHA256:ECDSA+SHA256:RSA+SHA256:RSA-PSS+SHA256\",\"ssl_session_ticket_keep_session_id\":\"off\"},\"proxy_ssl\":{\"enable\":\"off\",\"proxy_certificate\":\"\"},\"limit_speed\":{\"enable\":\"off\",\"conn_dry_run\":\"off\",\"conn_dry_run_log_level\":\"warn\",\"limit_conn_variable\":\"$binary_remote_addr\",\"limit_conn_zone\":\"\",\"limit_conn\":\"200\",\"limit_conn_log_sampling_rate\":\"10\"},\"proxy_buffer_size\":\"16k\",\"advanced_configuration\":\"\",\"resource_url\":\"\",\"proxy_protocol\":\"off\",\"proxy_pass\":\"\",\"access_control_enable\":\"off\"}},\"upstream\":{\"id\":\"\",\"name\":\"\",\"keepalive_connections\":\"100\",\"upstream_load_mode\":\"none\",\"server\":[{\"ip\":\"\",\"port\":\"\",\"weight\":\"1\",\"max_fails\":\"1\",\"fail_timeout\":\"10s\"}]}},\"http_forward\":{\"server\":{\"enable\":\"on\",\"id\":\"10086\",\"listen\":{\"ip\":\"0.0.0.0\",\"port\":\"34401\",\"backlog\":\"65535\"},\"location\":[]}},\"http_transparent\":{\"server\":{\"enable\":\"on\",\"id\":\"10088\",\"listen\":{\"ip\":\"127.0.0.1\",\"port\":\"34403\",\"backlog\":\"65535\"},\"ssl\":{\"enable\":\"off\",\"rsa_site_certificate\":\"\",\"sm2_site_certificate_enc\":\"\",\"sm2_site_certificate_sig\":\"\",\"ssl_protocols\":[\"TLSv1\",\"TLSv1.1\",\"TLSv1.2\"],\"ssl_verify_client\":\"off\",\"ssl_trust_local_cert_chain\":\"off\",\"ssl_close_if_nocert\":\"off\",\"ssl_ignore_cert_validity\":\"off\",\"ssl_ciphers\":\"ECDHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES128-SHA256:ECC-SM4-SM3:ECDHE-SM4-SM3:RSA-SM4-SM3\",\"ssl_client_sigalgs_list\":\"DSA+SHA1:ECDSA+SHA1:RSA+SHA1:DSA+SHA256:ECDSA+SHA256:RSA+SHA256:RSA-PSS+SHA256\",\"ssl_session_timeout\":\"3600s\",\"ssl_session_ticket_keep_session_id\":\"off\"},\"location\":[]}},\"http_reverse\":{\"server\":{\"enable\":\"on\",\"id\":\"pps\",\"listen\":{\"ip\":\"0.0.0.0\",\"ipv6\":\"[::]\",\"port\":\"443\",\"backlog\":\"65535\"},\"virtual_hosts_mode\":\"multi_location\",\"event_cert_enabled\":\"off\",\"pps_proxy_addr\":\"127.0.0.1:60500\",\"ssl\":{\"enable\":\"on\",\"rsa_site_certificate\":\"\",\"sm2_site_certificate_enc\":\"\",\"sm2_site_certificate_sig\":\"\",\"ssl_protocols\":[\"TLSv1\",\"TLSv1.1\",\"TLSv1.2\"],\"ssl_verify_client\":\"off\",\"ssl_trust_local_cert_chain\":\"off\",\"ssl_close_if_nocert\":\"off\",\"ssl_prefer_server_ciphers\":\"on\",\"ssl_ignore_cert_validity\":\"off\",\"ssl_ciphers\":\"ECDHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES128-SHA256:ECC-SM4-SM3:ECDHE-SM4-SM3:RSA-SM4-SM3\",\"ssl_client_sigalgs_list\":\"DSA+SHA1:ECDSA+SHA1:RSA+SHA1:DSA+SHA256:ECDSA+SHA256:RSA+SHA256:RSA-PSS+SHA256\",\"ssl_session_timeout\":\"3600s\",\"ssl_session_ticket_keep_session_id\":\"off\",\"md5_cert_enable\":\"off\"},\"location\":[]},\"server_pps_ssl\":{\"enable\":\"on\",\"id\":\"pps-ssl\",\"listen\":{\"ip\":\"0.0.0.0\",\"ipv6\":\"[::]\",\"port\":\"34400\",\"backlog\":\"65535\"},\"virtual_hosts_mode\":\"multi_location\",\"pps_proxy_cert_verify_addr\":\"127.0.0.1:60502\",\"ssl\":{\"enable\":\"on\",\"rsa_site_certificate\":\"\",\"sm2_site_certificate_enc\":\"\",\"sm2_site_certificate_sig\":\"\",\"ssl_protocols\":[\"TLSv1\",\"TLSv1.1\",\"TLSv1.2\"],\"ssl_verify_client\":\"on\",\"ssl_trust_local_cert_chain\":\"off\",\"ssl_close_if_nocert\":\"off\",\"ssl_prefer_server_ciphers\":\"on\",\"ssl_ignore_cert_validity\":\"off\",\"ssl_ciphers\":\"ECDHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES128-SHA256:ECC-SM4-SM3:ECDHE-SM4-SM3:RSA-SM4-SM3\",\"ssl_client_sigalgs_list\":\"DSA+SHA1:ECDSA+SHA1:RSA+SHA1:DSA+SHA256:ECDSA+SHA256:RSA+SHA256:RSA-PSS+SHA256\",\"ssl_session_timeout\":\"3600s\",\"ssl_session_ticket_keep_session_id\":\"off\",\"md5_cert_enable\":\"off\"},\"location\":[]}},\"tcp_forward\":{\"server\":{\"enable\":\"on\",\"id\":\"10087\",\"listen\":{\"ip\":\"0.0.0.0\",\"port\":\"34402\",\"backlog\":\"65535\"},\"proxy_pass\":\"$ssl_server_name\"}},\"tcp_transparent\":{\"server\":{\"enable\":\"on\",\"id\":\"10089\",\"listen\":{\"ip\":\"127.0.0.1\",\"port\":\"34404\",\"backlog\":\"65535\"},\"proxy_pass\":\"$origin_backend_addr:$origin_backend_port\"}},\"tcp_reverse\":{\"server\":{}}}}\n";
        /*LinkedHashMap<String,LinkedHashMap<String,Object>> addConfigMap = new LinkedHashMap<>();
        addConfigMap.put("/service_instances/location_template/http_reverse/server/location",httpReverseMap);
        addConfigMap.put("/service_instances/http_reverse/server/ssl",httpReverseMap);
        for (Map.Entry<String, LinkedHashMap<String,Object>> entry : addConfigMap.entrySet()) {
            json = JacksonUtil.addJson(json, entry.getKey(), entry.getValue());
        }*/
        removeJson(json,"/service_instances","proxy_ssl");
        removeJson(json,"/service_instances/location_template/http_reverse/server/location","proxy_ssl_certificate");
        System.out.println(json);

    }

    /**
     * Json中添加数据
     *
     * @param json   原json
     * @param ex     表达式
     * @param addMap map
     * @return 处理后的json
     */
    public static String addJson(String json, String ex, LinkedHashMap<String, Object> addMap) {
        ObjectNode jsonNodes;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            jsonNodes = objectMapper.readValue(json, ObjectNode.class);
            if (jsonNodes.at(ex) instanceof ObjectNode) {
                ObjectNode objectNode = (ObjectNode) jsonNodes.at(ex);
                addMap.forEach(objectNode::putPOJO);
            }
            //兼容service_instances...xxx.location[0].proxy_ssl 有list数组的情况
            if (jsonNodes.at(ex) instanceof ArrayNode) {
                ArrayNode arrayNode = (ArrayNode) jsonNodes.at(ex);
                addMap.forEach((k, v) -> {
                    ObjectNode next = (ObjectNode) arrayNode.elements().next().get("proxy_ssl");
                    next.putPOJO(k, v);
                });
            }
            return objectMapper.writeValueAsString(jsonNodes);
        } catch (Exception e) {
            throw new ApiException("add Json 异常", e);
        }
    }

    /**
     * Json中删除数据
     *
     * @param json   原json
     * @param ex     表达式
     * @param removeKey 需要删除的key
     * @return 处理后的json
     */
    public static String removeJson(String json, String ex, String removeKey) {
        ObjectNode jsonNodes;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            jsonNodes = objectMapper.readValue(json, ObjectNode.class);
            if (jsonNodes.at(ex) instanceof ObjectNode) {
                ObjectNode objectNode = (ObjectNode) jsonNodes.at(ex);
                objectNode.remove(removeKey);
            }
            //兼容service_instances...xxx.location[0].proxy_ssl 有list数组的情况
            if (jsonNodes.at(ex) instanceof ArrayNode) {
                ArrayNode arrayNode = (ArrayNode) jsonNodes.at(ex);
                ObjectNode next = (ObjectNode) arrayNode.elements().next().get("proxy_ssl");
                next.remove(removeKey);
            }
            return objectMapper.writeValueAsString(jsonNodes);
        } catch (Exception e) {
            throw new ApiException("add Json 异常", e);
        }
    }

    static {
        httpReverseMap.put("ssl_client_certificate", null);
        httpReverseMap.put("ssl_verify_depth", null);
        httpReverseMap.put("ssl_ecdh_curve", null);
        httpReverseMap.put("ssl_session_tickets", "1111111111111111");
    }

}


