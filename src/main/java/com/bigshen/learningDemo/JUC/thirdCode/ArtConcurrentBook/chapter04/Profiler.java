package com.bigshen.learningDemo.JUC.thirdCode.ArtConcurrentBook.chapter04;

import java.util.concurrent.TimeUnit;

/**
 * 6-15
 */
public class Profiler {
    /**
     ��һ��get()��������ʱ����г�ʼ�������set����û�е��ã���ÿ���̻߳����һ��
     *
     */
    private static final ThreadLocal<Long> TIME_THREADLOCAL = ThreadLocal.withInitial(System::currentTimeMillis);

    public static final void begin() {
        TIME_THREADLOCAL.set(System.currentTimeMillis());
    }

    public static final long end() {
        return System.currentTimeMillis() - TIME_THREADLOCAL.get();
    }

    public static void main(String[] args) throws Exception {
        Profiler.begin();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("Cost: " + Profiler.end() + " mills");
    }
}
