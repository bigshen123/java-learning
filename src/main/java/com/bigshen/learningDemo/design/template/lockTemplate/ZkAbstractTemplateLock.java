package com.bigshen.learningDemo.design.template.lockTemplate;

/**
 * @Author BYJ
 * @Date 2022/5/6 14:50
 * @Describe
 */
public abstract class ZkAbstractTemplateLock implements ZkLock {

    @Override
    public void zkLock() {
        // 尝试获取锁
        if(tryLock()) {
            System.out.println(Thread.currentThread().getName() + "\t 占用锁成功");
        } else {
            // 等待锁
            waitZkLock();
            // 重新调用获取锁的方法
            zkLock();
        }
    }

    protected abstract void waitZkLock();

    protected abstract boolean tryLock();
    

    @Override
    public void zkUnlock() {

    }
}
