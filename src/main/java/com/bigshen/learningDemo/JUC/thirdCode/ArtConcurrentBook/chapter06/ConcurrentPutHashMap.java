/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.bigshen.learningDemo.JUC.thirdCode.ArtConcurrentBook.chapter06;

import java.util.*;

/**
 * 并发put
 * 
 * @author tengfei.fangtf
 * @version $Id: Snippet.java, v 0.1 2015-7-31 下午11:53:55 tengfei.fangtf Exp $
 */
public class ConcurrentPutHashMap {

    public static void main(String[] args) throws InterruptedException {
        final HashMap<String, String> map = new HashMap<String, String>(2);
        Thread t = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                new Thread(() -> map.put(UUID.randomUUID().toString(), ""), "ftf" + i).start();
            }
        }, "ftf");
        t.start();
        t.join();
        map.forEach((s, s2) -> System.out.println(s));
    }

}
