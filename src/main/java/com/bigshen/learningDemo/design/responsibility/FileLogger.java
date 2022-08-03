package com.bigshen.learningDemo.design.responsibility;

/**
 * @ClassName FileLogger
 * @Description: TODO
 * @Author BYJ
 * @Date 2020/6/27
 * @Version V1.0
 **/
public class FileLogger extends AbstractLogger {

    public FileLogger(int level){
        this.level = level;
    }

    @Override
    protected void write(String message) {
        System.out.println("File::Logger: " + message);
    }
}
