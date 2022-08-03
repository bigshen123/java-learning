package com.bigshen.learningDemo.design.template;

/**
 * @Description:Game抽象类
 * @Author: byj
 * @Date: 2019/12/3 15:35
 */
public abstract class Game {
    abstract void initialize();

    abstract void startPlay();

    abstract void endPlay();

    /**
     * 模板方法
     */
    final void play() {

        //初始化游戏
        initialize();

        //开始游戏
        startPlay();

        //结束游戏
        endPlay();
    }
}
