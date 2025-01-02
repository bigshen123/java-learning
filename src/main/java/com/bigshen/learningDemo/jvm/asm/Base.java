package com.bigshen.learningDemo.jvm.asm;

/**
 * @Author BYJ
 * @Date 2025/1/2 20:42
 * @Describe 利用ASM的CoreAPI来增强类 我们期望的是，方法执行前输出“start”，之后输出”end”。
 *
 * 利用ASM实现AOP，需要定义两个类：一个是MyClassVisitor类，用于对字节码的visit以及修改；
 * 另一个是Generator类，在这个类中定义ClassReader和ClassWriter，其中的逻辑是，classReader读取字节码，然后交给MyClassVisitor类处理，
 * 处理完成后由ClassWriter写字节码并将旧的字节码替换掉。
 * ------
 * 著作权归@pdai所有
 * 原文链接：https://pdai.tech/md/java/jvm/java-jvm-class-enhancer.html
 *
 *编译后的.class文件  会在process 前后增加 System.out.println("start");  和System.out.println("end");
 */
public class Base {
    public void process(){
        System.out.println("process");
    }
}
