package com.bigshen.learningDemo.JUC.future;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * @author byj
 * @date 2025/3/25
 * @Description
 */
public class FutureCallableExample {
    static class CalculationCallable implements Callable<Integer> {
        private int x;
        private int y;

        public CalculationCallable(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public Integer call() throws Exception {
            System.out.println("开始执行：" + new Date());
            TimeUnit.SECONDS.sleep(2);//模拟任务执行的耗时
            return x + y;
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CalculationCallable calculationCallable = new CalculationCallable(1, 2);
        FutureTask<Integer> futureTask = new FutureTask<>(calculationCallable);
        new Thread(futureTask).start();
        System.out.println("开始执行futureTask：" + new Date());
        // get()方法是个阻塞方法,当线程还没有执行完FutureTask之前，主线程会阻塞在get()方法中。直到FutureTask执行结束，主线程才会被唤醒。
        Integer rs = futureTask.get();
        System.out.println("执行结果：" + rs);
        System.out.println("结束执行futureTask：" + new Date());
    }
}
