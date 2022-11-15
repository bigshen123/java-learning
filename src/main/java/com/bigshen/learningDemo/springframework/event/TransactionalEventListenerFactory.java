package com.bigshen.learningDemo.springframework.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationListenerMethodAdapter;
import org.springframework.context.event.EventListenerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.lang.reflect.Method;

/**
 * @author byj
 * @date 2022/11/2
 */
public class TransactionalEventListenerFactory implements EventListenerFactory, Ordered {
    private int order = 50; // 执行时机还是比较早的~~~（默认的工厂是最低优先级）

    // 显然这个工厂只会生成标注有此注解的handler~~~
    @Override
    public boolean supportsMethod(Method method) {
        return AnnotatedElementUtils.hasAnnotation(method, TransactionalEventListener.class);
    }

    // 这里使用的是ApplicationListenerMethodTransactionalAdapter，而非ApplicationListenerMethodAdapter
    // 虽然ApplicationListenerMethodTransactionalAdapter是它的子类
    @Override
    public ApplicationListener<?> createApplicationListener(String beanName, Class<?> type, Method method) {
        return new ApplicationListenerMethodTransactionalAdapter(beanName, type, method);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    // @since 4.2
    class ApplicationListenerMethodTransactionalAdapter extends ApplicationListenerMethodAdapter {

        private final TransactionalEventListener annotation;

        // 构造函数
        public ApplicationListenerMethodTransactionalAdapter(String beanName, Class<?> targetClass, Method method) {
            // 这一步的初始化交给父类，做了很多事情   强烈建议看看上面推荐的事件/监听的博文
            super(beanName, targetClass, method);

            // 自己个性化的：和事务相关
            TransactionalEventListener ann = AnnotatedElementUtils.findMergedAnnotation(method, TransactionalEventListener.class);
            if (ann == null) {
                throw new IllegalStateException("No TransactionalEventListener annotation found on method: " + method);
            }
            this.annotation = ann;
        }

        @Override
        public void onApplicationEvent(ApplicationEvent event) {
            // 若**存在事务**：毫无疑问 就注册一个同步器进去~~
            if (TransactionSynchronizationManager.isSynchronizationActive()) {
                TransactionSynchronization transactionSynchronization = createTransactionSynchronization(event);
                TransactionSynchronizationManager.registerSynchronization(transactionSynchronization);
            }
            // 若fallbackExecution=true，那就是表示即使没有事务  也会执行handler
            else if (this.annotation.fallbackExecution()) {
                if (this.annotation.phase() == TransactionPhase.AFTER_ROLLBACK && logger.isWarnEnabled()) {
                    logger.warn("Processing " + event + " as a fallback execution on AFTER_ROLLBACK phase");
                }
                processEvent(event);
            }
            else {
                // No transactional event execution at all
                // 若没有事务，输出一个debug信息，表示这个监听器没有执行~~~~
                if (logger.isDebugEnabled()) {
                    logger.debug("No transaction is active - skipping " + event);
                }
            }
        }

        // TransactionSynchronizationEventAdapter是一个内部类，它是一个TransactionSynchronization同步器
        // 此类实现也比较简单，它的order由listener.getOrder();来决定
        private TransactionSynchronization createTransactionSynchronization(ApplicationEvent event) {
            return new TransactionSynchronizationEventAdapter(this, event, this.annotation.phase());
        }


        private class TransactionSynchronizationEventAdapter extends TransactionSynchronizationAdapter {

            private final ApplicationListenerMethodAdapter listener;
            private final ApplicationEvent event;
            private final TransactionPhase phase;

            public TransactionSynchronizationEventAdapter(ApplicationListenerMethodAdapter listener,
                                                          ApplicationEvent event, TransactionPhase phase) {
                this.listener = listener;
                this.event = event;
                this.phase = phase;
            }

            // 它的order又监听器本身来决定
            @Override
            public int getOrder() {
                return this.listener.getOrder();
            }

            // 最终都是委托给了listenner来真正的执行处理  来执行最终处理逻辑（也就是解析classes、condtion、执行方法体等等）
            @Override
            public void beforeCommit(boolean readOnly) {
                if (this.phase == TransactionPhase.BEFORE_COMMIT) {
                    processEvent();
                }
            }

            // 此处结合status和phase   判断是否应该执行~~~~
            // 此处小技巧：我们发现TransactionPhase.AFTER_COMMIT也是放在了此处执行的，只是它结合了status进行判断而已~~~
            @Override
            public void afterCompletion(int status) {
                if (this.phase == TransactionPhase.AFTER_COMMIT && status == STATUS_COMMITTED) {
                    processEvent();
                } else if (this.phase == TransactionPhase.AFTER_ROLLBACK && status == STATUS_ROLLED_BACK) {
                    processEvent();
                } else if (this.phase == TransactionPhase.AFTER_COMPLETION) {
                    processEvent();
                }
            }

            protected void processEvent() {
                this.listener.processEvent(this.event);
            }
        }
    }

}

