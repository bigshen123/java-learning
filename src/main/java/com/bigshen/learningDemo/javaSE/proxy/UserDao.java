package com.bigshen.learningDemo.javaSE.proxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @ClassName UserDao
 * @Description: TODO
 * @Author BYJ
 * @Date 2020/6/25
 * @Version V1.0
 **/
@Repository //-->这个在Dao层中使⽤
public class UserDao {
    @Autowired
    AOPTest aop;
    public void save2() {
        begin();
        System.out.println("DB:保存⽤户");
        close();
    }
    public void begin() {
        System.out.println("开始事务");
    }
    public void close() {
        System.out.println("关闭事务");
    }
    public void save() {
        aop.begin();
        System.out.println("DB:保存⽤户");
        aop.close();
    }
}
