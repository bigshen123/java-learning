package com.bigshen.learningDemo.bigdata.flink;

import org.apache.flink.api.common.functions.MapPartitionFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author byj
 * @date 2024/9/18
 * @Description mapPartition
 * 将一个分区中的元素转换为另一个元素

 * map和mapPartition的效果是一样的，但如果在map的函数中，需要访问一些外部存储。例如：
 * 访问mysql数据库，需要打开连接, 此时效率较低。而使用mapPartition可以有效减少连接数，提高效率
 */
public class MapPartitionExample {
    public static class User {
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
    }

    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        DataSet<String> userDataSet = env.fromCollection(new ArrayList<String>() {{
            add("1,张三");
            add("2,李四");
            add("3,王五");
            add("4,赵六");
        }});

        DataSet<User> resultDataSet = userDataSet.mapPartition(new MapPartitionFunction<String, User>() {
            @Override
            public void mapPartition(Iterable<String> iterable, Collector<User> collector) throws Exception {
                // TODO: 打开连接

                Iterator<String> iterator = iterable.iterator();
                while (iterator.hasNext()) {
                    String ele = iterator.next();
                    String[] fieldArr = ele.split(",");
                    collector.collect(new User(fieldArr[0], fieldArr[1]));
                }

                // TODO: 关闭连接
            }
        });

        resultDataSet.print();
    }
}
