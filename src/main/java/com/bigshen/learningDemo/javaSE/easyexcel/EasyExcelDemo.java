package com.bigshen.learningDemo.javaSE.easyexcel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author byj
 * @date 2023/7/4
 *  see https://easyexcel.opensource.alibaba.com/docs/current/
 */
public class EasyExcelDemo {

    public static void main(String[] args) {
        // 准备数据
//        List<User> userList = generateData();
//
//        // 导出Excel文件
//        String fileName = "src/main/resources/file.xlsx";
//        EasyExcel.write(fileName, User.class).sheet("Sheet1").doWrite(userList);
//        System.out.println("Excel导出成功！");
        List<String> tests = new ArrayList<>();
        tests.add("11.11.11.11");
        tests.add("22.22.22.22");
        tests.add("33.33.33.33");
        tests.removeIf(test -> test.equals("11.11.11.11"));
        System.out.println(tests);
    }

    private static List<User> generateData() {
        List<User> userList = new ArrayList<>();

        // 添加示例数据
        userList.add(new User("Alice", 25, "alice@example.com"));
        userList.add(new User("Bob", 30, "bob@example.com"));
        userList.add(new User("Charlie", 28, "charlie@example.com"));

        return userList;
    }
}
