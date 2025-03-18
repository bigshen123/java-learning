package com.bigshen.learningDemo.jvm.classLoader.demo;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 * 类加载器体验主类
 * @author byj
 * @date 2025/2/19
 * @Description
 */
public class ClassLoaderExperienceMain {
    public static void main(String[] args) throws Exception {
        // 1. 实例化一个自定义的类加载器
        //    book-sample 模块上的类所在根目录，请根据自己电脑的实际情况更改
        MyClassloader myClassloader = new MyClassloader("D:\\tmp\\DemoClass");

        // 2. 加载 BookApi 这个Class
        Class bookApiClass = myClassloader.loadClass("vip.guzb.clrdemo.BookApi");

        // 3. 创建 DemoA Class 的实例，
        //    这里不直接写成 DemoA demoA = new DemoA(); 因为 DemoA 在类路径下不存在。
        //    即使存在，根据本文本一开始的场景，也因为同时要加载同名的类，而不允许存在
        Object bookApiObj = bookApiClass.newInstance();

        // 4. 调用 BookApi 的 description() 方法
        //    该方法很简单，返回类型为标准库中的 java.lang.String, 因此代码书写也相对容易
        Method testAMethod = bookApiClass.getMethod("description");
        String resultOfDescription = (String)testAMethod.invoke(bookApiObj);
        System.out.printf("description()方法的调用结果: %s\n\n", resultOfDescription);

        // 5. 调用 BookApi 的 getBooksOfAuthor 方法
        //    该方法的返回值是一个集合，而集合中的对象在 Classpath 中不存在，
        //    获取集合元素的属性和方法的代码将会显示很冗长
        Method getBooksOfAuthorMethod = bookApiClass.getMethod("getBooksOfAuthor", String.class);
        Collection<?> books = (Collection<?>) getBooksOfAuthorMethod.invoke(bookApiObj, "老舍");
        System.out.println("老舍的作品列表: ");
        for (Object book : books) {
            // books 集合中的对象类型为 vip.guzb.clrdemo.Book,
            // 但由于是使用单独的类加载器加载的，不能像那样直接在源码中书写，依然要通过反射来获取
            Method bookNameMethod = book.getClass().getMethod("getName");
            Method bookPriceMethod = book.getClass().getMethod("getPrice");
            String bookName = (String)bookNameMethod.invoke(book);
            Double price = (Double) bookPriceMethod.invoke(book);

            // 同理, vip.guzb.clrdemo.Press 对象的访问也需要通过反射
            Method pressMethod = book.getClass().getMethod("getPress");
            Object pressObj = pressMethod.invoke(book);
            Method pressNameMethod = pressObj.getClass().getMethod("getName");
            Method pressAddressMethod = pressObj.getClass().getMethod("getAddress");
            String pressName = (String)pressNameMethod.invoke(pressObj);
            String pressAddress = (String)pressAddressMethod.invoke(pressObj);
            System.out.printf(" · 书名: 《%s》, 价格: %.2f, 出版社: %s, 地址: %s\n", bookName, price, pressName, pressAddress);
        }
    }
}
