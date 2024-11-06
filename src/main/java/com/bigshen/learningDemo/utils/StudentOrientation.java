package com.bigshen.learningDemo.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * @author byj
 * @date 2024/10/24
 * @Description 100 名同学面向老师站成一行，老师先让大家按 1,2,3,…99,100 的顺序从左向右依次报数，再让报数是 2的倍数的同学向后转，接着又让报数是3的倍数的同学向后转，最后让报数是 5的倍数的同学向后转。最后面向老师的同学还有多少名?(提醒:部分同学会有多次向后转)“
 */
public class StudentOrientation {
    public static void main(String[] args) {

        //优雅
//        AtomicInteger count = new AtomicInteger(0);
//        IntStream.range(1,100).forEach(i->{
//            if (i%2 ==0 ^ i%3==0 ^ i%5==0){
//                count.incrementAndGet();
//            }
//        });
//        System.out.println(100-count.get());

        // 初始化100名同学，true表示面向老师，false表示背对老师
        boolean[] students = new boolean[100];
        for (int i = 0; i < 100; i++) {
            students[i] = true; // 初始状态都是面向老师
        }

        // 获取初始面向老师的同学数量和编号
        Result initialResult = getCount(students);
        System.out.println("初始最后面向老师的同学共有：" + initialResult.count + "名，编号：" + initialResult.numbers);

        // 让报数是2的倍数的同学向后转
        for (int i = 2; i <= 100; i += 2) {
            students[i - 1] = !students[i - 1]; // 转向
        }
        Result afterTwoResult = getCount(students);
        System.out.println("让报数是2的倍数的同学向后转后 面向老师的同学共有：" + afterTwoResult.count + "名，编号：" + afterTwoResult.numbers);

        // 让报数是3的倍数的同学向后转
        for (int i = 3; i <= 100; i += 3) {
            students[i - 1] = !students[i - 1]; // 转向
        }
        Result afterThreeResult = getCount(students);
        System.out.println("让报数是3的倍数的同学向后转后 面向老师的同学共有：" + afterThreeResult.count + "名，编号：" + afterThreeResult.numbers);

        // 让报数是5的倍数的同学向后转
        for (int i = 5; i <= 100; i += 5) {
            students[i - 1] = !students[i - 1]; // 转向
        }

        Result finalResult = getCount(students);
        System.out.println("最后面向老师的同学共有 " + finalResult.count + " 名，编号：" + finalResult.numbers);
    }

    private static class Result {
        int count;              // 面向老师的同学数量
        List<Integer> numbers;   // 面向老师的同学编号

        Result(int count, List<Integer> numbers) {
            this.count = count;
            this.numbers = numbers;
        }
    }

    private static Result getCount(boolean[] students) {
        // 统计面向老师的同学数量及其编号
        int count = 0;
        List<Integer> numbers = new ArrayList<>();

        for (int i = 0; i < students.length; i++) {
            if (students[i]) {
                count++;
                numbers.add(i + 1); // 学生编号从1开始
            }
        }
        return new Result(count, numbers);
    }
}
