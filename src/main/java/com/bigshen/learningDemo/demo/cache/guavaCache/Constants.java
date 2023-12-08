package com.bigshen.learningDemo.demo.cache.guavaCache;

import java.util.HashMap;
import java.util.Map;

/**
 * @author byj
 * @date 2023/12/5
 * @Description
 */
public class Constants {
    public static Map<String,String> hm=new HashMap<>();

    static {
        hm.put("1","吴京");
        hm.put("2","李晨");
        hm.put("3","韩东君");
        hm.put("4","易烊千玺");
    }
}
