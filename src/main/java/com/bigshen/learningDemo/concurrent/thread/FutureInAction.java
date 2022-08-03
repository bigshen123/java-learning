package com.bigshen.learningDemo.concurrent.thread;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Description:Future实现立马回调一个结果
 * @Author: BIGSHEN
 * @Date: 2019/12/11 21:08
 */
public class FutureInAction {
    public static void main(String[] args){
        Future<String> future = invoke(()->{
            try {
                Thread.sleep(10000L); // 设定方法执行十秒钟，十秒钟过后才返回结果
                return "I am finished";
            } catch (InterruptedException e) {
                return "error";
            }
        });  // 此处不会产生阻塞，会立刻返回
        System.out.println(future.get());  // 可以做任意其他操作
        System.out.println(future.get());
        System.out.println(future.get());

        // 如果方法还没有执行完，则等待10秒钟
        // 因为我们设定了方法执行时间为10秒，所以这里等待10秒，肯定能拿到结果
        while(!future.isDown()){
            try {
                Thread.sleep(10000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(future.get());
    }

    /**
     * 调用方法
     * @param callable
     * @param <T>
     * @return
     */
    private static <T> Future<T> invoke(Callable<T> callable){
        AtomicReference<T> result = new AtomicReference<>();
        AtomicBoolean finished = new AtomicBoolean(false);

        Thread t = new Thread(()->{
            T value = callable.action();
            result.set(value);
            finished.set(true);
        });
        t.start();

        Future<T> future = new Future<T>() {
            @Override
            public T get() {
                return result.get();
            }

            @Override
            public boolean isDown() {
                return false;
            }
        };

        return future;
    }

    private interface Future<T>{
        T get();
        boolean isDown();
    }

    private interface Callable<T>{
        T action();
    }
}
