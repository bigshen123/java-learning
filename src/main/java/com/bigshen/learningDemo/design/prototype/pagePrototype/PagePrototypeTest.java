package com.bigshen.learningDemo.design.prototype.pagePrototype;

/**
 * @author byj
 * @date 2022/10/9
 * *
 *  原型模式主要解决的问题就是创建大量重复的类，而我们模拟的场景就需要给不同的用户都创建相同的试卷，
 *  但这些试卷的题目不便于每次都从库中获取，甚至有时候需要从远程的RPC中获取。这样都是非常耗时的，而且随着创建对象的增多将严重影响效率。
 *
 *  在原型模式中所需要的非常重要的手段就是克隆，在需要用到克隆的类中都需要实现 implements Cloneable 接口。
 */
public class PagePrototypeTest {
    public static void main(String[] args) throws CloneNotSupportedException {
        QuestionBankController questionBankController = new QuestionBankController();
        System.out.println(questionBankController.createPaper("花花", "1000001921032"));
        System.out.println(questionBankController.createPaper("豆豆", "1000001921051"));
        System.out.println(questionBankController.createPaper("大宝", "1000001921987"));
    }
}
