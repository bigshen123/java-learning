package com.bigshen.learningDemo.design.responsibility;

/**
 * @ClassName ErrorLogger
 * @Description: TODO
 * @Author BYJ
 * @Date 2020/6/27
 * @Version V1.0
 **/
public class ErrorLogger extends AbstractLogger {

    public ErrorLogger(int level){
        this.level = level;
    }

    @Override
    protected void write(String message) {
        System.out.println("Error Console::Logger: " + message);
    }
}
