package com.bigshen.learningDemo.utils;

import com.bigshen.learningDemo.common.util.HumpUtil;

import javax.validation.constraints.NotNull;

/**
 * @author byj
 * @date 2022/10/24
 */
public class EnumUtil {
    public static <T extends Enum<T>> T valueOfWithFormat(Class<T> enumClass, String name, T defaultValue) {
        if (name == null) {
            return defaultValue;
        }
        name = name.trim();
        try {
            String underlineName = HumpUtil.humpToUnderline(name);
            return Enum.valueOf(enumClass, underlineName);
        } catch (Exception e) {
            try {
                String upperCaseName = name.toUpperCase();
                return Enum.valueOf(enumClass, upperCaseName);
            } catch (Exception e2) {
                return defaultValue;
            }
        }

    }

    @NotNull
    public static <T extends Enum<T>> T valueOfWithFormat(Class<T> enumClass, String name) {
        if (name == null) {
            throw new NullPointerException("Name is null");
        }

        T enumItem = valueOfWithFormat(enumClass, name, null);
        if (enumItem == null) {
            throw new IllegalArgumentException(
                    "No enum constant " + enumClass.getCanonicalName() + '.' + name);

        }
        return enumItem;

    }
}
