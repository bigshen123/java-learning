package com.bigshen.learningDemo.JUC.thirdCode.thread.threadLocal;

/**
 * @ClassName ThreadLocalUsage05New
 * @Description: TODO
 * @Author BYJ
 * @Date 2020/7/20
 * @Version V1.0
 **/
public class ThreadLocalUsage05New {
    public static void main(String[] args) {
        init();
        new NameService2().getName();
        new SexService2().getSex();
        new ScoreService2().getScore();
    }

    private static void init() {
        Student2 student = new Student2();
        student.name = "Lemon";
        student.sex = "female";
        student.score = "100";
        ThreadLocalProcessor.studentThreadLocal.set(student);
    }

}

class ThreadLocalProcessor {

    public static ThreadLocal<Student2> studentThreadLocal = new ThreadLocal<>();

}

class Student2 {

    /**
     * 姓名、性别、成绩
     */
    String name;
    String sex;
    String score;

}

class NameService2 {

    public void getName() {
        System.out.println(ThreadLocalProcessor.studentThreadLocal.get().name);
    }

}

class SexService2 {

    public void getSex() {
        System.out.println(ThreadLocalProcessor.studentThreadLocal.get().sex);
    }

}

class ScoreService2 {

    public void getScore() {
        System.out.println(ThreadLocalProcessor.studentThreadLocal.get().score);
    }

}
