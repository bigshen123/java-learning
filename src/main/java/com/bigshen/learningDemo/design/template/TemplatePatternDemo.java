package com.bigshen.learningDemo.design.template;

/**
 * @Description:模板设计模式demo
 * @Author: byj
 * @Date: 2019/12/3 15:37
 */

public class TemplatePatternDemo {
    public static void main(String[] args) {

        Game game = new Cricket();
        game.play();
        System.out.println();
        game = new Football();
        game.play();
    }
}