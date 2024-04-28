package com.bigshen.learningDemo.javaSE.genericity;

/**
 * @Author BYJ
 * @Date 2024/2/3 13:53
 * @Describe
 */
class Info<T extends Number>{    // 此处泛型只能是数字类型
    private T var ;        // 定义泛型变量
    public void setVar(T var){
        this.var = var ;
    }
    public T getVar(){
        return this.var ;
    }
    @Override
    public String toString(){    // 直接打印
        return this.var.toString() ;
    }
}


class Info2<T>{
    private T var ;        // 定义泛型变量
    public void setVar(T var){
        this.var = var ;
    }
    public T getVar(){
        return this.var ;
    }
    @Override
    public String toString(){    // 直接打印
        return this.var.toString() ;
    }
}
public class GenericsDemo21{
    public static void main(String args[]){
        // 声明String的泛型对象
        Info2<String> i1 = new Info2<String>() ;
        // 声明Object的泛型对象
        Info2<Object> i2 = new Info2<Object>() ;
        i1.setVar("hello") ;
        i2.setVar(new Object()) ;
        fun(i1) ;
        fun(i2) ;
    }

    /**
     *  只能接收String或Object类型的泛型，String类的父类只有Object类
     * @param temp
     */
    public static void fun(Info2<? super String> temp){
        System.out.print(temp + ", ") ;
    }
}
