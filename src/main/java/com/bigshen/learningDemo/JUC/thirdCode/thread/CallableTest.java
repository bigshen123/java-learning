package com.bigshen.learningDemo.JUC.thirdCode.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * @Description:
 * @Author: BIGSHEN
 * @Date: 2019/12/17 16:17
 */
public class CallableTest {
    public static void main(String[] args) throws Exception {
        Callable<Integer> callable = new CallableImpl();
        FutureTask<Integer> futureTask = new FutureTask<>(callable);
        System.out.println("立即返回");
        Thread thread = new Thread(futureTask);
        thread.start();
        // 阻塞5秒, 等待子线程执行完
        System.out.println(futureTask.get());
        // 上面拿到了返回值, 才进行下一步操作
        System.out.println("success");
    }
}

class CallableImpl implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        Thread.sleep(5000);
        return 1;
    }
}
