package com.bigshen.learningDemo.lambda.future;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

/**
 * @ClassName FutureDemo03
 * @Description:TODO
 * @Author: byj
 * @Date: 2020/8/12
 */
public class FutureTest {

    /**
     * runAsync()方法的使用，—》使用了ForkJoinPool.commonPool() 作为线程池，并进行异步执行
     */
    @Test
    public void runAsync(){
        CompletableFuture<Void> future = CompletableFuture.runAsync(()-> System.out.println("hello world"));
        try {
            future.get();
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("completableFuture end!");
    }

    /**
     * supplyAsync 和 runAsync 的区别是supplyAsyncy有返回值，而runAsync没有返回值。
     */
    @Test
    public void supplyAsync(){
        CompletableFuture<String> future=CompletableFuture.supplyAsync(() -> "Hello");
        try {
            System.out.println(future.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("completableFuture end!");
    }

    /**
     * 如果future已经执行完毕能够返回结果，此时再调complete(T t)则会无效
     */
    @Test
    public void complete(){
        CompletableFuture<String> future = CompletableFuture.supplyAsync(()->"Hello");
//        多次重复调会失效
        future.complete("World");
        future.complete("World2");
        future.complete("World3");
        future.complete("World4");
        try {
            System.out.println(future.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("completableFuture end!");
    }

    /**
     * 如果future已经执行完毕能够返回结果，此时再调complete(T t)则会无效
     */
    @Test
    public void complete02(){
        CompletableFuture<String> future = CompletableFuture.supplyAsync(()->"Hello");
        //如果future已经执行完毕能够返回结果，此时再调complete(T t)则会无效
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            System.out.println(future.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("completableFuture end!");
    }

    /**
     * thenApply 是将CompletableFuture 转换成CompletableFuture,也就是类型转换
     */
    @Test
    public void thenApply(){
        CompletableFuture<String> future = CompletableFuture.supplyAsync(()->"Hello").thenApply(s->s+" world").thenApply(String::toUpperCase);
        try {
            System.out.println(future.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("completableFuture end!");
    }

    /**
     * thenCompose 组合多个CompletableFuture，将前一个结果作为下一个计算的参数，他们之间存在着先后顺序。还是串行
     */
    @Test
    public void thenCompose(){
        CompletableFuture<String> future = CompletableFuture.supplyAsync(()->"Hello").thenCompose(s->CompletableFuture.supplyAsync(()->s + " world"));
//thenCompose 可以用于组合多个CompletableFuture，将前一个结果作为下一个计算的参数，它们之间存在着先后顺序
        try {
            System.out.println(future.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * thenCombine 是将两个或多个CompletableFuture的结果进行汇总。
     */
    @Test
    public void thenCombine(){
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(()->"100");
        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(()->10);
        CompletableFuture<Double> future = future1.thenCombine(future2,(s,i)->Double.parseDouble(s+i));
//        使用thenCombine是将future1 和future2的结果汇总，这一点跟thenCompose()不同。其中future1和future2是并行执行的。
        try {
            System.out.println(future.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("completableFuture end!");

    }

    /**
     * thenAcceptBoth 是当两个CompletableFuture都正常完成后，执行提供的action,用它来组合另一个CompletableFuture的结果。
     */
    @Test
    public void thenAcceptBoth(){
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(()->"100");
        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(()->70);
        CompletableFuture<Void> future = future1.thenAcceptBoth(future2,(s,i)-> System.out.println(s+i));
    }

    /**
     * 当CompletableFuture完成计算结果后，我们需要对结果进行一些处理。 whenComplete 对结果的异常进行处理。
     */
    @Test
    public void whenComplete(){
        CompletableFuture.supplyAsync(()->"Hello").thenApply(s->s+" world").thenApply(s->s+"\nThis is CompletableFuture demo")
                .thenApply(String::toLowerCase).whenComplete((result,throwable)-> System.out.println(result));
    }

    /**
     * handle 当CompletableFuture完成计算结果或者抛出异常的时候，执行提供的fn;
     */
    @Test
    public void handle(){
        CompletableFuture<Double> future = CompletableFuture.supplyAsync(()->"100").thenApply(s->s+"10")
                .handle((s,t)->s!=null? Double.parseDouble(s):0);
        try {
            System.out.println(future.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("completableFuture end!");
    }

    /**
     * handle :当CompletableFuture完成计算结果或者抛出异常的时候，执行提供的方法。相当于whenComplete()+转换
     */
    @Test
    public void handle02(){
        CompletableFuture<Double> future = CompletableFuture.supplyAsync(()->"100").thenApply(s->s+"10").handle(new BiFunction<String, Throwable, Double>() {
            @Override
            public Double apply(String s, Throwable throwable) {
                return s!=null?Double.parseDouble(s):0;
            }
        });
        try {
            System.out.println(future.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 纯消费（执行Action）thenAccept,只会对计算结果进行消费而不会返回任何结果的方法。
     */
    @Test
    public void thenAccept(){
        CompletableFuture.supplyAsync(()->"Hello")
                .thenApply(s->s+" world")
                .thenApply(s->s+"\nThis is CompletableFuture demo")
                .thenApply(String::toLowerCase).thenAccept(System.out::println);
    }
}
