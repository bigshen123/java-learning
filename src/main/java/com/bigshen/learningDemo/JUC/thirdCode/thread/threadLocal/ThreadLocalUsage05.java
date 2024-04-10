package com.bigshen.learningDemo.JUC.thirdCode.thread.threadLocal;

/**
 * @ClassName ThreadLocalUsage05
 * @Description: TODO
 * @Author BYJ
 * @Date 2020/7/20
 * @Version V1.0
 **/
public class ThreadLocalUsage05 {
    public static void main(String[] args) {
        Student1 student = init();
        new NameService().getName(student);
        new SexService().getSex(student);
        new ScoreService().getScore(student);
    }

    private static Student1 init() {
        Student1 student = new Student1();
        student.name = "Lemon";
        student.sex = "female";
        student.score = "100";
        return student;
    }

}

class Student1 {

    /**
     * 姓名、性别、成绩
     */
    String name;
    String sex;
    String score;

}

class NameService {

    public void getName(Student1 student) {
        System.out.println(student.name);
    }

}

class SexService {

    public void getSex(Student1 student) {
        System.out.println(student.sex);
    }

}

class ScoreService {

    public void getScore(Student1 student) {
        System.out.println(student.score);
    }

}
