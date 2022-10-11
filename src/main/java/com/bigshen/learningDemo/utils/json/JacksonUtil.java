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

}
