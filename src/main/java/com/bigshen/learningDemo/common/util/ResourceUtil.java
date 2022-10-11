package com.bigshen.learningDemo.common.util;

import com.bigshen.learningDemo.common.annotation.Path;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Table;

/**
 * @author byj
 * @date 2022/10/11
 */
public class ResourceUtil {
    public static String getPath(Class<?> resourceClass) {
        String path = null;
        if (resourceClass.isAnnotationPresent(Path.class)) {
            path = resourceClass.getAnnotation(Path.class).value();
        }
        if (StringUtils.isEmpty(path)) {
            throw new RuntimeException("无法获取实体类 " + resourceClass.getCanonicalName() + " 对应的路径（类型标志）!");
        }
        return path;
    }

    public static String getTable(Class<?> resourceClass) {
        String table = null;
        if (resourceClass.isAnnotationPresent(Table.class)) {
            table = resourceClass.getAnnotation(Table.class).name();
        }
        if (StringUtils.isEmpty(table)) {
            throw new RuntimeException("无法获取实体类 " + resourceClass.getCanonicalName() + " 对应的数据库表名!");
        }
        return table;
    }
}
