package com.bigshen.chatDemoService.design.template.lockTemplate;

/**
 * @Author BYJ
 * @Date 2022/5/6 14:49
 * @Describe
 */
public interface ZkLock {
    public void zkLock();

    public void zkUnlock();
}
