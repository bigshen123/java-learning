package com.bigshen.learningDemo.design.proxy.jdkProxy.user;



import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @ClassName InvocationHandlerImpl
 * @Description:TODO JDK��̬����
 * @Author: byj
 * @Date: 2020/12/1
 */
public class InvocationHandlerImpl implements InvocationHandler {

    private Object target;

    public InvocationHandlerImpl(Object target){
        this.target=target;
    }

    /**
     * ��̬����ʵ�����еķ���
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("���ÿ�ʼ����......");
        Object result = method.invoke(target, args);
        System.out.println("���ý���......");
        return result;
    }

    public static void main(String[] args) {
        UserDao userDaoImpl=new UserDaoImpl();
        //���������
        InvocationHandlerImpl invocationHandlerImpl=new InvocationHandlerImpl(userDaoImpl);
        //�������
        ClassLoader classLoader = userDaoImpl.getClass().getClassLoader();
        Class<?>[] interfaces = userDaoImpl.getClass().getInterfaces();
        UserDao newProxyInstance = (UserDao) Proxy.newProxyInstance(classLoader, interfaces, invocationHandlerImpl);
        newProxyInstance.save();
    }
}
