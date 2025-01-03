package com.bigshen.learningDemo.JUC.threadLocal;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * 下面是个ThreadLocal使用的实例，两个任务共享同一个变量，并且两个任务都把该变量设置为了线程私有变量，
 * 这样，虽然两个任务都”持有“同一变量，但各自持有该变量的拷贝。
 * 因此，当一个线程修改该变量时，不会影响另一线程该变量的值。
 * @Author: BIGSHEN
 * @Date: 2019/12/9 20:56
 */
public class ThreadLocalDemo implements Runnable {

    private static ThreadLocal<Student> localStudent = new ThreadLocal<Student>() {
        @Override
        public Student initialValue() {
            return new Student();
        }
    };
    private Student student = null;

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        System.out.println("【" + threadName + "】：is running !");
        Random ramdom = new Random();
        //随机生成一个变量
        int age = ramdom.nextInt(100);

        System.out.println("【" + threadName + "】：set age to :" + age);
        // 获得线程局部变量，改变属性值
        Student stu = getStudent();
        stu.setAge(age);

        System.out.println("【" + threadName + "】：第一次读到的age值为 :" + stu.getAge());

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("【" + threadName + "】：第二次读到的age值为 :" + stu.getAge());
    }
    private Student getStudent() {
        student = localStudent.get();

    // 如果不重写初始化方法，则需要判断是否为空，然后手动为ThreadLocal赋值，否则的话会报空指针异常// if(student == null){// student = new com.bigshen.chatDemoService.demo.test.Student();// localStudent.set(student);// }

        return student;
    }

    public static void main(String[] args) {
        ThreadLocalDemo ll = new ThreadLocalDemo();
        Thread t1 = new Thread(ll, "线程1");
        Thread t2 = new Thread(ll, "线程2");

        t1.start();
        t2.start();
    }
}
