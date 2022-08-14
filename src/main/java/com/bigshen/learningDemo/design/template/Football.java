package com.bigshen.learningDemo.design.template;

/**
 * @Description:
 * @Author: byj
 * @Date: 2019/12/3 15:37
 */
public class Football extends Game {

    @Override
    void endPlay() {
        System.out.println("Football Game Finished!");
    }

    @Override
    void initialize() {
        System.out.println("Football Game Initialized! Start playing.");
    }

    @Override
    void startPlay() {
        System.out.println("Football Game Started. Enjoy the game!");
    }
}
