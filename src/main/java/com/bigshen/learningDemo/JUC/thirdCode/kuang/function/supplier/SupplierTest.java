package com.bigshen.learningDemo.JUC.thirdCode.kuang.function.supplier;

import java.util.Arrays;

/**
 * @author byj
 * @date 2024/11/25
 * @Description 无参有返回值的接口，对于的Lambda表达式需要提供一个返回数据的类型。
 */
public class SupplierTest {
    public static void main(String[] args) {
        fun1(() -> {
            int arr[] = {22, 33, 55, 66, 44, 99, 10}; // 计算出数组中的最大值
            Arrays.sort(arr);
            return arr[arr.length - 1];
        });
    }
    private static void fun1(Supplier<Integer> supplier) {
        Integer max = supplier.get();
        System.out.println("max = " + max);
    }
}
