package com.bigshen.learningDemo.design.facade;

/**
 * @author byj
 * @date 2025/3/24
 * @Description
 */
public class SubSystem {
    public void turnOnTV() {
        System.out.println("turnOnTV()");
    }

    public void setCD(String cd) {
        System.out.println("setCD( " + cd + " )");
    }

    public void starWatching(){
        System.out.println("starWatching()");
    }
}
