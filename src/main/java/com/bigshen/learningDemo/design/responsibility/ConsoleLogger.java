package com.bigshen.learningDemo.design.responsibility;

/**
 * @ClassName ConsoleLogger
 * @Description: TODO
 * @Author BYJ
 * @Date 2020/6/27
 * @Version V1.0
 **/
public class ConsoleLogger extends AbstractLogger {

    public ConsoleLogger(int level){
        this.level = level;
    }

    @Override
    protected void write(String message) {
        System.out.println("Standard Console::Logger: " + message);
    }
}
