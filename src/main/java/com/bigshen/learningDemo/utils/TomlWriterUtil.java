package com.bigshen.learningDemo.utils;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;

import java.util.Map;

/**
 * @author byj
 * @date 2022/11/16
 */
public class TomlWriterUtil {

    public static String tomlMap2String(Map<String, Object> map) {
        TomlWriter tomlWriter = new TomlWriter();
        return tomlWriter.write(map);
    }

    public static Map<String, Object> tomlString2Map(String tomlStr) {
        Toml toml = new Toml().read(tomlStr);
        return toml.toMap();
    }

    public static void main(String[] args) {

    }

}
