package com.bigshen.learningDemo.demo.async;

import java.util.concurrent.Executor;

/**
 * @Description:
 * 很多人对于 TaskExecutor 不是太了解，所以我们花一点篇幅先介绍一下这个东西。从名字就能看出它是任务的执行者，
 * 它领导执行着线程来处理任务，
 * 就像司令官一样，而我们的线程就好比一只只军队一样，这些军队可以异步对敌人进行打击
 * @Author: BIGSHEN
 * @Date: 2019/12/29 10:25
 */
@FunctionalInterface
public interface TaskExecutor extends Executor {
    void execute(Runnable var1);
}
