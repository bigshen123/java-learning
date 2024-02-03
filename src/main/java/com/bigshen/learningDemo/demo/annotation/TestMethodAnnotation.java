package com.bigshen.learningDemo.demo.annotation;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author BYJ
 * @Date 2024/2/3 15:45
 * @Describe
 */
public class TestMethodAnnotation {
    @Override
    @MyMethodAnnotation(title = "toStringMethod", description = "override toString method")
    public String toString() {
        return "Override toString method";
    }

    @Deprecated
    @MyMethodAnnotation(title = "old static method", description = "deprecated old static method")
    public static void oldMethod() {
        System.out.println("old method, don't use it.");
    }

    @SuppressWarnings({"unchecked", "deprecation"})
    @MyMethodAnnotation(title = "test method", description = "suppress warning static method")
    public static void genericsTest() throws FileNotFoundException {
        List l = new ArrayList();
        l.add("abc");
        oldMethod();
    }
}
