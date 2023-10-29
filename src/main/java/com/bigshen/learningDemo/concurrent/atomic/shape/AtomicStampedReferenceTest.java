package com.bigshen.learningDemo.concurrent.atomic.shape;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @Author BYJ
 * @Date 2023/10/29 17:51
 * @Describe ABA 问题 demo
 */
public class AtomicStampedReferenceTest {

    static BuildingBlock A = new BuildingBlock("三角形");
    // 初始化一个积木对象B，形状为四边形
    static BuildingBlock B = new BuildingBlock("四边形");
    // 初始化一个积木对象D，形状为五边形
    static BuildingBlock D = new BuildingBlock("五边形");

    static AtomicReference<BuildingBlock> atomicReference = new AtomicReference<>(A);

    // 传递两个值，一个是初始值，一个是初始版本号
    static AtomicStampedReference<BuildingBlock> atomicStampedReference = new AtomicStampedReference<>(A, 1);

    public static void main(String[] args) {
        // 初始化一个积木对象A，形状为三角形
//        new Thread(() -> {
//            try {
//                // 睡眠一秒，保证t1线程，完成了ABA操作
//                TimeUnit.SECONDS.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            // 可以替换成功，因为乙线程执行了A->B->A，形状没变，所以甲可以进行替换。
//            // true    BuildingBlock{shape='五边形}
//            System.out.println(atomicReference.compareAndSet(A, D) + "\t" + atomicReference.get());
//        }, "甲").start();


        new Thread(() -> {
            // 获取版本号
            int stamp = atomicStampedReference.getStamp();
            System.out.println(Thread.currentThread().getName() + "\t 第一次版本号" + stamp);
            // 暂停线程“乙”1秒钟，使线程“甲”可以获取到原子引用的版本号
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            /*
             * 乙线程开始ABA替换
             * */
            // 1.比较并替换，传入4个值，期望值A，更新值B，期望版本号，更新版本号
            atomicStampedReference.compareAndSet(A, B, atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1);
            System.out.println(Thread.currentThread().getName() + "\t 第二次版本号" + atomicStampedReference.getStamp()); //乙     第一次版本号1
            // 2.比较并替换，传入4个值，期望值B，更新值A，期望版本号，更新版本号
            atomicStampedReference.compareAndSet(B, A, atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1); // 乙     第二次版本号2
            System.out.println(Thread.currentThread().getName() + "\t 第三次版本号" + atomicStampedReference.getStamp()); // 乙     第三次版本号3
        }, "乙").start();


        new Thread(() -> {
            // 获取版本号
            int stamp = atomicStampedReference.getStamp();
            System.out.println(Thread.currentThread().getName() + "\t 第一次版本号" + stamp); // 甲   第一次版本号1
            // 暂停线程“甲”3秒钟，使线程“乙”进行一次ABA替换操作
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean result = atomicStampedReference.compareAndSet(A,D,stamp,stamp + 1);
            System.out.println(Thread.currentThread().getName() + "\t 修改成功否" + result + "\t 当前最新实际版本号：" + atomicStampedReference.getStamp()); // 甲     修改成功否false     当前最新实际版本号：3
            System.out.println(Thread.currentThread().getName() + "\t 当前实际最新值：" + atomicStampedReference.getReference()); // 甲     当前实际最新值：BuildingBlock{shape='三角形}

        }, "甲").start();


    }

}
