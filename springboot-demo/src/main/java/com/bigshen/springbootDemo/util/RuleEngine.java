package com.bigshen.springbootDemo.util;

import com.bigshen.springbootDemo.model.ImportRule;
import lombok.extern.slf4j.Slf4j;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;
import java.util.regex.Pattern;

@Slf4j
public class RuleEngine {

    private static final ScriptEngineManager SEM = new ScriptEngineManager();

    public static boolean eval(ImportRule rule, String value) {
        String type = rule.getRuleType();
        String content = rule.getRuleContent();
        if ("NOT_NULL".equalsIgnoreCase(type)) {
            return value != null && !value.trim().isEmpty();
        } else if ("REGEX".equalsIgnoreCase(type)) {
            if (content == null) {
                return true;
            }
            return value != null && Pattern.matches(content, value);
        } else if ("RANGE".equalsIgnoreCase(type)) {
            if (content == null || value == null) {
                return false;
            }
            try {
                String[] parts = content.split("~");
                double min = Double.parseDouble(parts[0]);
                double max = Double.parseDouble(parts[1]);
                double v = Double.parseDouble(value);
                return v >= min && v <= max;
            } catch (Exception e) {
                log.warn("RANGE eval error", e);
                return false;
            }
        } else if ("CUSTOM_JS".equalsIgnoreCase(type)) {
            if (content == null) return true;
            try {
                ScriptEngine engine = SEM.getEngineByName("javascript");
                if (engine == null) {
                    log.warn("No JS engine available");
                    return false;
                }
                SimpleBindings bindings = new SimpleBindings();
                bindings.put("value", value);
                Object res = engine.eval(content, bindings);
                if (res instanceof Boolean) return (Boolean) res;
                return Boolean.parseBoolean(String.valueOf(res));
            } catch (Exception e) {
                log.warn("CUSTOM_JS eval error", e);
                return false;
            }
        }
        return true;
    }
}
