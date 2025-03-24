package com.bigshen.learningDemo.design.proxy.staticProxy;

import com.bigshen.learningDemo.design.proxy.ISender;

/**
 * @author byj
 * @date 2025/3/21
 * @Description
 */
public class ProxySender implements ISender {
    private ISender sender;

    public ProxySender(ISender sender) {
        this.sender = sender;
    }

    public boolean send() {
        System.out.println("处理前");
        boolean result = sender.send();
        System.out.println("处理后");
        return result;
    }
}
