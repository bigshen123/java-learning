package com.bigshen.learningDemo.design.facade;

/**
 * @author byj
 * @date 2025/3/24
 * @Description
 * 观看电影需要操作很多电器，使用外观模式实现一键看电影功能。
 * 最少知识原则: 只和你的密友谈话。也就是说客户对象所需要交互的对象应当尽可能少。
 */
public class Facade {
    private SubSystem subSystem = new SubSystem();

    public void watchMovie() {
        subSystem.turnOnTV();
        subSystem.setCD("a movie");
        subSystem.starWatching();
    }
}
