package com.bigshen.learningDemo.demo.reflections;

import lombok.Getter;

import java.io.Serializable;
import java.lang.reflect.Constructor;

/**
 * @Author BYJ
 * @Date 2024/2/3 17:03
 * @Describe
 */
public class ConstructionTest implements Serializable {


    /**
     * @param args
     * @throws Exception
     * return :
     * /* output
     * User{age=20, name='Jack'}
     * --------------------------------------------
     * user1:User{age=22, name='hiway'}
     * --------------------------------------------
     * user2:User{age=25, name='hiway2'}
     * --------------------------------------------
     * 构造函数[0]:private com.example.javabase.User(int,java.lang.String)
     * 参数类型[0]:(int,java.lang.String)
     * 构造函数[1]:public com.example.javabase.User(java.lang.String)
     * 参数类型[1]:(java.lang.String)
     * 构造函数[2]:public com.example.javabase.User()
     * 参数类型[2]:()
     */
    public static void main(String[] args) throws Exception {

        Class<?> clazz;

        //获取Class对象的引用
        clazz = Class.forName("com.bigshen.learningDemo.demo.reflections.User");

        //第一种方法，实例化默认构造方法，User必须无参构造函数,否则将抛异常
        User user = (User) clazz.newInstance();
        user.setAge(20);
        user.setName("Jack");
        System.out.println(user);

        System.out.println("--------------------------------------------");

        //获取带String参数的public构造函数
        Constructor<?> cs1 =clazz.getConstructor(String.class);
        //创建User
        User user1= (User) cs1.newInstance("hiway");
        user1.setAge(22);
        System.out.println("user1:"+ user1);

        System.out.println("--------------------------------------------");

        //取得指定带int和String参数构造函数,该方法是私有构造private
        Constructor<?> cs2=clazz.getDeclaredConstructor(int.class,String.class);
        //由于是private必须设置可访问
        cs2.setAccessible(true);
        //创建user对象
        User user2= (User) cs2.newInstance(25,"hiway2");
        System.out.println("user2:"+user2.toString());

        System.out.println("--------------------------------------------");

        //获取所有构造包含private
        Constructor<?>[] cons = clazz.getDeclaredConstructors();
        // 查看每个构造方法需要的参数
        for (int i = 0; i < cons.length; i++) {
            //获取构造函数参数类型
            Class<?>[] clazzs = cons[i].getParameterTypes();
            System.out.println("构造函数["+i+"]:"+cons[i].toString() );
            System.out.print("参数类型["+i+"]:(");
            for (int j = 0; j < clazzs.length; j++) {
                if (j == clazzs.length - 1) {
                    System.out.print(clazzs[j].getName());
                } else {
                    System.out.print(clazzs[j].getName() + ",");
                }
            }
            System.out.println(")");
        }
    }
}


@Getter
class User {
    private int age;
    private String name;
    public User() {
        super();
    }
    public User(String name) {
        super();
        this.name = name;
    }

    /**
     * 私有构造
     * @param age
     * @param name
     */
    private User(int age, String name) {
        super();
        this.age = age;
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}
