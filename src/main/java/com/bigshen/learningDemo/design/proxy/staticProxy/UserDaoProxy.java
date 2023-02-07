package com.bigshen.learningDemo.design.proxy.staticProxy;

/**
 * @ClassName UserDaoProxy
 * @Description:TODO ?????????
 * @Author: byj
 * @Date: 2020/12/1
 */
public class UserDaoProxy extends UserDao {
    private UserDao userDao;

    public UserDaoProxy(UserDao userDao){
        this.userDao=userDao;
    }

    public void save(){
        System.out.println("��������......");
        userDao.save();
        System.out.println("�ر�����......");
    }

    public static void main(String[] args) {
        UserDao userDao=new UserDao();
        UserDaoProxy userDaoProxy=new UserDaoProxy(userDao);
        userDaoProxy.save();
    }
}
