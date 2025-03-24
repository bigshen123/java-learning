package com.bigshen.learningDemo.design.facade;

/**
 * @author byj
 * @date 2025/3/24
 * @Description
 */
public class Client {
    public static void main(String[] args) {
        Facade facade = new Facade();
        facade.watchMovie();
    }
}
