package com.bigshen.learningDemo.bigdata.flink;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;

import java.util.ArrayList;


/**
 * @author byj
 * @date 2024/9/18
 * @Description distinct
 * 去除重复的数据
 */
public class DistinctExample {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        DataSet<Tuple2<String, Integer>> wordCountDataSet = env.fromCollection(new ArrayList<Tuple2<String, Integer>>() {{
            add(new Tuple2<>("java", 1));
            add(new Tuple2<>("java", 1));
            add(new Tuple2<>("scala", 1));
        }});

        DataSet<Tuple2<String, Integer>> resultDataSet = wordCountDataSet.distinct(0);

        resultDataSet.print();
    }
}
