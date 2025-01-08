package com.bigshen.learningDemo.jvm.javap;

/**
 * @author byj
 * @date 2025/1/3
 * @Description
 *   javap -v -p .\TestCode.class  查看字节码 -v 详细信息  -p 显示所有类 .\TestCode.class 文件路径
 *    0: iconst_1 将int类型1推送至栈顶
 *    1: istore_1 将栈顶int类型数值存入第一个本地变量
 *    2: iload_1 将第一个本地变量推送至栈顶
 *    3: istore_2 将栈顶int类型数值存入第二个本地变量
 *    4: iload_1 将第一个本地变量推送至栈顶
 *    5: ireturn 从当前方法返回int类型数据
 *    6: astore_3 将栈顶引用类型数值存入第三个本地变量
 *    7: iconst_2 将int类型2推送至栈顶
 *    8: istore_1 将栈顶int类型数值存入第一个本地变量
 *    9: iload_1 将第一个本地变量推送至栈顶
 *    10: istore_2 将栈顶int类型数值存入第二个本地变量
 *    11: iload_1 将第一个本地变量推送至栈顶
 *    12: ireturn 从当前方法返回int类型数据
 *    13: astore_3 将栈顶引用类型数值存入第三个本地变量
 *    14: iconst_3 将int类型3推送至栈顶
 *    15: istore_1 将栈顶int类型数值存入第一个本地变量
 *    16: iload_1 将第一个本地变量推送至栈顶
 *    17: pop 从操作数栈中弹出数值
 *    18: iload_1 将第一个本地变量推送至栈顶
 *    19: ireturn 从当前方法返回int类型数据
 *    20: astore_3 将栈顶引用类型数值存入第三个本地变量
 *
 *
 */
public class TestCode {
    public int foo() {
        int x;
        try {
            x = 1;
            return x;
        } catch (Exception e) {
            x = 2;
            return x;
        } finally {
            x = 3;
        }
    }
}
