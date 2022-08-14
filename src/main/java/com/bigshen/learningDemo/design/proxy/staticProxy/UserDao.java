package com.bigshen.learningDemo.design.proxy.staticProxy;

/**
 * @ClassName UserDao
 * @Description:TODO ԭʼ����
 * @Author: byj
 * @Date: 2020/12/1
 */
public class UserDao {
    public void save(){
        System.out.println("�������ݵķ���.......");
    }

    public static void main(String[] args) {
        UserDao userDao=new UserDao();
        userDao.save();
    }
}
