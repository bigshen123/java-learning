package com.bigshen.learningDemo.javaSE.genericity;

import java.util.ArrayList;

/**
 * @Author BYJ
 * @Date 2024/2/3 14:04
 * @Describe 验证泛型擦除
 */
public class Test {

    public static void main(String[] args) throws Exception {
        ArrayList<String> list1 = new ArrayList<String>();
        list1.add("abc");
        ArrayList<Integer> list2 = new ArrayList<Integer>();
        list2.add(123);
        System.out.println(list1.getClass() == list2.getClass()); // true

        // 通过反射添加其它类型的元素
        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(1);  //这样调用 add 方法只能存储整形，因为泛型类型的实例为 Integer
        list.getClass().getMethod("add", Object.class).invoke(list, "asd");
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));// 1，asd
        }

        /**不指定泛型的时候*/
        int i = Test.add(1, 2); //这两个参数都是Integer，所以T为Integer类型
        Number f = Test.add(1, 1.2); //这两个参数一个是Integer，一个是Float，所以取同一父类的最小级，为Number
        Object o = Test.add(1, "asd"); //这两个参数一个是Integer，一个是String，所以取同一父类的最小级，为Object
        /**指定泛型的时候*/
        int a = Test.<Integer>add(1, 2); //指定了Integer，所以只能为Integer类型或者其子类
     //   int b = Test.<Integer>add(1, 2.2); //编译错误，指定了Integer，不能为Float
        Number c = Test.<Number>add(1, 2.2); //指定为Number，所以可以为Integer和Float
    }

    public static <T> T add(T x,T y){
        return y;
    }
}
