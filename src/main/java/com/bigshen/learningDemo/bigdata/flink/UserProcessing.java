package com.bigshen.learningDemo.bigdata.flink;


import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.util.Collector;

import java.util.ArrayList;

/**
 * @author byj
 * @date 2024/9/18
 * @Description FlatMapFunction
 * 将DataSet中的每一个元素转换为0…n个元素
 */
public class UserProcessing {
    public static class User {
        public String name;
        public String country;
        public String province;
        public String city;

        public User() {}

        public User(String name, String country, String province, String city) {
            this.name = name;
            this.country = country;
            this.province = province;
            this.city = city;
        }

        @Override
        public String toString() {
            return "User(" + name + ", " + country + ", " + province + ", " + city + ")";
        }
    }

    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        DataSet<String> userDataSet = env.fromCollection(new ArrayList<String>() {{
            add("张三,中国,江西省,南昌市");
            add("李四,中国,河北省,石家庄市");
            add("Tom,America,NewYork,Manhattan");
        }});

        DataSet<User> resultDataSet = userDataSet.flatMap(new FlatMapFunction<String, User>() {
            @Override
            public void flatMap(String text, Collector<User> collector) {
                String[] fieldArr = text.split(",");
                String name = fieldArr[0];
                String country = fieldArr[1];
                String province = fieldArr[2];
                String city = fieldArr[3];

                collector.collect(new User(name, country, province, city));
                collector.collect(new User(name, country, province + city, ""));
                collector.collect(new User(name, country, province + city, city));
            }
        });

        resultDataSet.print();
    }
}
