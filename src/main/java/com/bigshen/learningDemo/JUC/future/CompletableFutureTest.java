package com.bigshen.learningDemo.JUC.future;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * @author byj
 * @date 2022/11/23
 */
@Slf4j
public class CompletableFutureTest {

    private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors() + 1;

    public static ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

    /**
     * 3个服务并发调用，然后对结果进行合并处理，阻塞主线程。
     * supplyAsync 没有入参，但有返回值
     */
    @Test
    public void CompletableFutureDemo1() {
        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread() + "-f1");
            return "f1";
        },executor);

        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread() + "-f2");
            return "f2";
        },executor);

        CompletableFuture<String> f3 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread() + "-f3");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "f3";
        },executor);

        CompletableFuture<Void> allOf  = CompletableFuture.allOf(f1, f2, f3);
        allOf.thenApplyAsync(v->{
            System.out.print("begin......");
            try {
                System.out.println(Thread.currentThread() + f1.get());
                System.out.println(Thread.currentThread() + f2.get());
                System.out.println(Thread.currentThread() + f3.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            return 1;
        }).join();
        System.out.println(Thread.currentThread() + " end");
    }

    /**
     * 组合 s1执行完成后并发执行s2和s3，然后消费相关结果，不阻塞主线程。
     */
    @Test
    public void thenCombineAsyncTest() {
        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread() + "-f1");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "f1";
        });
        CompletableFuture<String> f2 = f1.thenApplyAsync((v) -> {
            System.out.println(Thread.currentThread() + "-f2");
            return "f2";
        });
        CompletableFuture<String> f3 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread() + "-f3");
            return "f3";
        });
        // thenCombineAsync:当两个CompletableFutures都完成后，对它们的结果进行操作，并返回一个新的CompletableFuture
        f2.thenCombineAsync(f3, (f2s, f3s) -> {
            System.out.println(Thread.currentThread() + f2s);
            System.out.println(Thread.currentThread() + f3s);
            return null;
        });
        System.out.println(Thread.currentThread() + " end");
    }

    /**
     * 异步执行 无返回值
     * supplyAsync有返回值
     * runAsync 无参无返回值
     */
    @Test
    public void runAsyncTest() {
        System.out.println(THREAD_COUNT);
        CompletableFuture.runAsync(() -> {
            System.out.println("当前线程：" + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行线程：" + i);
        }, executor);
    }

    /**
     * whenComplete 方法完成后的感知
     * handle 方法完成后的处理 处理异常，并修改返回值
     */
    @Test
    public void whenCompleteTest() {
        CompletableFuture.supplyAsync(() -> {
                    System.out.println("当前线程" + Thread.currentThread().getId());
                    int i = 10 / 0;
                    System.out.println("运行结果" + i);
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return i;
                }, executor).whenComplete((res, exception) -> {
                    //虽然能得到异常信息，但是无法修改返回数据
                    System.out.println("异步任务完成。。。结果是：" + res + ";异常是" + exception);
                }).exceptionally(throwable -> 10)
                .handle((res, thr) -> {
                    if (res != null) {
                        System.out.println("处理结果" + res * 2);
                        return res * 2;
                    }
                    if (thr != null) {
                        System.out.println("异常是" + thr);
                        return 0;
                    }
                    return 0;
                });
    }

    /**
     * 线程串行化
     * 1) thenRunAsync: 不能获取上一步的执行结果，无返回值
     * .thenRunAsync(() -> {
     * System.out.println("任务2启动了");
     * }, executor);
     * 2) thenAcceptAsync: 能接收上一步的结果，但是无返回值
     * .thenAcceptAsync(res->{
     * System.out.println("任务2启动了"+res);
     * });
     * 3)thenApplyAsync 能接收上一步的结果，而且有返回值
     */
    @Test
    public void thenAsyncTest() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("当前线程:" + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行结果:" + i);
            return i;
        }, executor).thenApplyAsync(res -> {
            System.out.println("任务2启动了:" + res);
            return "hello" + res;
        }, executor);

        System.out.println("任务2返回值:" + future.get());
    }

    /**
     * runAfterBothAsync  等待前两个任务执行完之后再执行此方法（无返回值）
     * <p>
     * thenAcceptBothAsync  等待前两个任务执行完之后再执行此方法（有返回值）
     * <p>
     * runAfterEitherAsync  两个任务，只要一个执行完成，就执行任务3 ( 不感知结果，自己业务返回值)
     * <p>
     * allOf 必须所有任务执行完成，才算成功
     * <p>
     * anyOf  只要一个执行完成，就算成功
     */
    @Test
    public void runAfterBothAsyncTest() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("任务线程1");
            return "任务线程1";
        }, executor);

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("任务线程2");
            return "任务线程2";
        }, executor);

        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> {
            System.out.println("任务线程3");
            return "任务线程3";
        }, executor);

        //等待前两个任务执行完之后再执行此方法（无返回值）
        future1.runAfterBothAsync(future2, () -> System.out.println("任务线程3开始"), executor);

        /* 等待前两个任务执行完之后再执行此方法（有返回值）*/
        future1.thenAcceptBothAsync(future2, (f1, f2) -> System.out.println("任务线程3开始,之前的任务：" + f1 + "---->>" + f2), executor);
        /*
         * 两个任务，只要一个执行完成，就执行任务3
         * runAfterEitherAsync 不感知结果，自己业务返回值
         * */
        future1.runAfterEitherAsync(future2, () -> System.out.println("任务线程3开始"), executor);

        /*必须所有任务执行完成，才算成功*/
        CompletableFuture<Void> allOf = CompletableFuture.allOf(future1, future2, future3);
        allOf.get();

        /*只要一个执行完成，就算成功*/
        CompletableFuture<Object> anyOf = CompletableFuture.anyOf(future1, future2, future3);
        anyOf.get();

    }

    /**
     * 感知结果，接收返回值并消费掉，不产生返回值
     */
    @Test
    public void acceptEitherAsyncTest() {
        CompletableFuture<Integer> f1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("任务一开始");
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
            return 1;
        }, executor);

        CompletableFuture<Integer> f2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("任务二开始");
            return 2;
        });

        f1.acceptEitherAsync(f2, (res) -> {
            //需要注意使用lambda表达式需要future1和future2返回值类型相同
            System.out.println("感知结果执行任务三");
        }, executor);
    }

    /**
     * 感知返回值，转换返回值得到一个新的结果
     */
    @Test
    public void applyToEitherAsyncTest() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("任务一开始");
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
            return 1;
        });

        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("任务二开始");
            return 2;
        });
        CompletableFuture<String> f3 = future1.applyToEitherAsync(future2, (res) -> {
            System.out.println("感知结果执行任务三");
            return "新的返回值:" + res * 2;
        }, executor);
        System.out.println(f3.get());
    }

    /**
     * 多任务都完成
     * 问题一：是执行future1.get();时主线程是堵塞的，因此future2.get();future3.get();
     * 都不会执行需要等待future1先得到返回值，也就是说会加上多余的堵塞时间。虽然任务是异步的(的确任务已经完成了，但是线程没有交还线程池)
     * 而我们希望的是对于提前完成任务的线程将他交还给线程池，让它可以再次被其它任务执行
     * 例如：future1 耗时4s，future2耗时3秒，future3耗时5s。如果是直接下面代码，则会导致future2在3秒的时候就已经可以完成任务了，
     * 但是由于没有进行get，线程一直在等待返回数据，那么future2的线程相当于被占用着。
     * 问题二：就是产生冗余代码
     */
    @Test
    public void allOfTest() {
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("任务一开始");
            return 1;
        }, executor);
        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("任务二开始");
            return 2;
        });
        CompletableFuture<Integer> future3 = CompletableFuture.supplyAsync(() -> {
            System.out.println("任务三开始");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return 3;
        }, executor);
        CompletableFuture<Void> allOf = CompletableFuture.allOf(future1, future2, future3);
        try {
            allOf.get();//等待所有任务都完成
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("end.......");

    }

    /**
     * 多任务组合只要有一个完成
     */
    @Test
    public void anyOfTest() {
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("任务一开始");
            return 1;
        }, executor);
        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("任务二开始");
            return 2;
        });
        CompletableFuture<Integer> future3 = CompletableFuture.supplyAsync(() -> {
            System.out.println("任务三开始");
            return 3;
        }, executor);
        CompletableFuture<Object> anyOf = CompletableFuture.anyOf(future1, future2, future3);
        try {
            Integer result = (Integer) anyOf.get();//获得完成的那个任务结果,其它任务的结果就获取不到，想要获取得调用各自得get
            System.out.println("结果："+result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * 多线程任务，堵塞执行，获取所有结果
     */
    @Test
    public void concurrencyAsyncTest() {
        Instant start = Instant.now();
        List<CompletableFuture<String>> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(CompletableFuture.supplyAsync(this::getData, executor));
        }
        CompletableFuture.allOf(list.toArray(new CompletableFuture[0])).join();
        log.info(String.format("********全部执行完毕，总耗时:%s ********", (ChronoUnit.SECONDS.between(start, Instant.now()))));
        //打印结果
        list.parallelStream().forEach((cf) -> log.info(cf.join()));
    }

    private <U> String getData() {
        return "获取数据" + UUID.randomUUID();
    }
}
