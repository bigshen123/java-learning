package com.bigshen.learningDemo.demo.authencator;

import com.bigshen.learningDemo.demo.authencator.auth.DefaultApiAuthencatorImpl;

/**
 * @Description:
 * @Author: BIGSHEN
 * @Date: 2019/12/21 18:50
 */
public class Client {
    public static void main(String[] args) {
        String req = "geekbang?"
                + "AppID=designpattern"
                + "&Token=IXIGIpJ9hdOBCyjStaDJ5Nom07g="
                + "&Timestamp=1465185768";

        DefaultApiAuthencatorImpl authencator = new DefaultApiAuthencatorImpl();
        authencator.auth(req);
//    buildAuthToken: IXIGIpJ9hdOBCyjStaDJ5Nom07g=
//    pass auth!
    }
}
