package com.bigshen.learningDemo.bigdata.flink;

import org.apache.flink.api.common.operators.Order;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.core.fs.FileSystem;

import java.util.ArrayList;

/**
 * @author byj
 * @date 2024/9/18
 * @Description sortPartition
 *  指定字段对分区中的数据进行排序
 */
public class SortPartitionExample {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        env.setParallelism(2);

        DataSet<String> wordDataSet = env.fromCollection(new ArrayList<String>() {{
            add("hadoop");
            add("hadoop");
            add("hadoop");
            add("hive");
            add("hive");
            add("spark");
            add("spark");
            add("flink");
        }});

        DataSet<String> sortedDataSet = wordDataSet.sortPartition(str -> str, Order.DESCENDING);

        sortedDataSet.writeAsText("./data/sort_output/", FileSystem.WriteMode.OVERWRITE);

        env.execute("App");
    }
}
