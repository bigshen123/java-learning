package com.bigshen.chatDemoService.design.proxy.jdkProxy;

/**
 * @ClassName UserDaoImpl
 * @Description:TODO
 * @Author: byj
 * @Date: 2020/12/1
 */
public class UserDaoImpl implements UserDao {
    @Override
    public void save() {
        System.out.println("保存数据的方法......");
    }
}
