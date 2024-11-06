package com.bigshen.learningDemo.bigdata.flink;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;

import java.util.ArrayList;

/**
 * @author byj
 * @date 2024/9/18
 * @Description union
 * 将两个DataSet取并集，不会去重。
 */
public class UnionExample {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        DataSet<String> wordDataSet1 = env.fromCollection(new ArrayList<String>() {{
            add("hadoop");
            add("hive");
            add("flume");
        }});

        DataSet<String> wordDataSet2 = env.fromCollection(new ArrayList<String>() {{
            add("hadoop");
            add("hive");
            add("spark");
        }});

        DataSet<String> resultDataSet = wordDataSet1.union(wordDataSet2);

        resultDataSet.print();
    }
}
