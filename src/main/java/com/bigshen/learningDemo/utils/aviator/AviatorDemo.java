package com.bigshen.learningDemo.utils.aviator;

import com.google.common.collect.Maps;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author byj
 * @date 2023/3/22
 */
public class AviatorDemo {


    @Test
    public void AviatorTest1() {
        Long result1 = (Long) AviatorEvaluator.execute("1+2+3");
        System.out.println(result1);

        String name = "唐简";
        Map<String,Object> env = new HashMap<>(Maps.newHashMapWithExpectedSize(12));
        env.put("name", name);
        String result2 = (String) AviatorEvaluator.execute(" 'Hello ' + name ", env);
        System.out.println(result2);

        String str = "小哥哥带你使用Aviator";
        Map<String,Object> env2 = new HashMap<>();
        env2.put("str",str);
        Expression expression = AviatorEvaluator.compile("string.length(str)", true);
        Long length = (Long)expression.execute(env2);
        System.out.println(length);

        String expression2 = "a-(b-c)>100";
        Expression compiledExp = AviatorEvaluator.compile(expression2);
        Map<String, Object> env3 = new HashMap<>();
        env3.put("a", 100.3);
        env3.put("b", 45);
        env3.put("c", -199.100);
        Boolean result = (Boolean) compiledExp.execute(env3);
        System.out.println(result);


    }
}
