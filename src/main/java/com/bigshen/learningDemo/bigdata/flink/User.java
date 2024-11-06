package com.bigshen.learningDemo.bigdata.flink;

import java.util.Arrays;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.common.functions.MapFunction;

/**
 * @author byj
 * @date 2024/9/18
 * @Description MapFunction
 */
public class User {
    public String id;
    public String name;

    public User() {}

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "User(" + id + ", " + name + ")";
    }

    public static void main(String[] args) throws Exception {
        // 1.获取ExecutionEnvironment运行环境
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        // 2.使用fromCollection构建数据源
        DataSet<String> textDataSet = env.fromCollection(
                Arrays.asList("1,张三", "2,李四", "3,王五", "4,赵六")
        );

        // 3.创建一个User样例类
        // 4.使用map操作执行转换
        DataSet<User> userDataSet = textDataSet.map((MapFunction<String, User>) text -> {
            String[] fieldArr = text.split(",");
            return new User(fieldArr[0], fieldArr[1]);
        });

        // 5.打印测试
        userDataSet.print();
    }
}
