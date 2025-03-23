package com.bigshen.learningDemo.design.responsibility;

/**
 * @ClassName ChainPatternDemo
 * @Description: TODO 职责链模式
 * 赋予它们不同的错误级别，并在每个记录器中设置下一个记录器
 * <p>
 * 每个记录器中的下一个记录器代表的是链的一部分
 * @Author BYJ
 * @Date 2020/6/27
 * @Version V1.0
 **/
public class ChainPatternDemo {

    private static AbstractLogger getChainOfLoggers() {

        AbstractLogger errorLogger = new ErrorLogger(AbstractLogger.ERROR);
        AbstractLogger fileLogger = new FileLogger(AbstractLogger.DEBUG);
        AbstractLogger consoleLogger = new ConsoleLogger(AbstractLogger.INFO);

        errorLogger.setNextLogger(fileLogger);
        fileLogger.setNextLogger(consoleLogger);

        return errorLogger;
    }

    public static void main(String[] args) {
        AbstractLogger loggerChain = getChainOfLoggers();

        loggerChain.logMessage(AbstractLogger.INFO,
                "This is an information.");

        loggerChain.logMessage(AbstractLogger.DEBUG,
                "This is an debug level information.");

        loggerChain.logMessage(AbstractLogger.ERROR,
                "This is an error information.");
    }
}
