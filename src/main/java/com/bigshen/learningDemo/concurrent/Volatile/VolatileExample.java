package com.bigshen.learningDemo.concurrent.Volatile;

class VolatileExample {
    int              a    = 0;
    volatile boolean flag = false;

    public void writer() {
        a = 1; //1
        flag = true; //2
    }

    public void reader() {
        if (flag) { //3
            int i = a; //4
            System.out.println(i);
            //����
        }
    }

    public static void main(String[] args) {
        VolatileExample volatileExample=new VolatileExample();
        volatileExample.writer();
        volatileExample.reader();
        System.out.println(volatileExample.a);
        System.out.println(volatileExample.flag);
    }
}
