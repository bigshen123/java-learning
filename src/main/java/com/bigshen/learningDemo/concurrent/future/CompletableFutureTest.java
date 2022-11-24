package com.bigshen.learningDemo.concurrent.future;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author byj
 * @date 2022/11/23
 */
public class CompletableFutureTest {

    /**
     * 3个服务并发调用，然后对结果进行合并处理，阻塞主线程。
     * @param args args
     */
    @Test
    public void CompletableFutureDemo1(String[] args) {
        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread() + "-f1");
            return "f1";
        });

        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread() + "-f2");
            return "f2";
        });

        CompletableFuture<String> f3 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread() + "-f3");
            return "f3";
        });

        CompletableFuture.allOf(f1,f2,f3).thenApply((Integer) -> {
            try {
                System.out.println(Thread.currentThread() + f1.get());
                System.out.println(Thread.currentThread() + f2.get());
                System.out.println(Thread.currentThread() + f3.get());
            }catch (InterruptedException | ExecutionException e){
                e.printStackTrace();
            }
            return 1;
        });
        System.out.println(Thread.currentThread() + " end");
    }
}
