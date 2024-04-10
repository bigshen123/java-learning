package com.bigshen.learningDemo.JUC.cyclicbarrier;

import java.util.concurrent.CyclicBarrier;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

/**
 * @Author BYJ
 * @Date 2020/12/17 22:29
 * @Describe CyclicBarrier 中文名叫做屏障或者是栅栏，也可以用于线程间通信。
 *
 * 它可以等待 N 个线程都达到某个状态后继续运行的效果。
 *
 * 首先初始化线程参与者。
 * 调用 await() 将会在所有参与者线程都调用之前等待。
 * 直到所有参与者都调用了 await() 后，所有线程从 await() 返回继续后续逻辑。
 * 可以看出由于其中一个线程休眠了五秒，所有其余所有的线程都得等待这个线程调用 await() 。
 *
 * 该工具可以实现 CountDownLatch 同样的功能，但是要更加灵活。甚至可以调用 reset() 方法重置 CyclicBarrier
 * (需要自行捕获 BrokenBarrierException 处理) 然后重新执行。
 */
public class cyclicBarrierDemo2 {
    private static void cyclicBarrier() {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(3) ;

        new Thread(() -> {
            LOGGER.info("thread run");
            try {
                cyclicBarrier.await() ;
            } catch (Exception e) {
                e.printStackTrace();
            }

            LOGGER.info("thread end do something");
        }).start();

        new Thread(() -> {
            LOGGER.info("thread run");
            try {
                cyclicBarrier.await() ;
            } catch (Exception e) {
                e.printStackTrace();
            }

            LOGGER.info("thread end do something");
        }).start();

        new Thread(() -> {
            LOGGER.info("thread run");
            try {
                Thread.sleep(5000);
                cyclicBarrier.await() ;
            } catch (Exception e) {
                e.printStackTrace();
            }

            LOGGER.info("thread end do something");
        }).start();

        LOGGER.info("main thread");
    }

    public static void main(String[] args) {
        cyclicBarrier();
    }
}
