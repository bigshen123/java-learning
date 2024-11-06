package com.bigshen.learningDemo.bigdata.flink;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.aggregation.Aggregations;
import org.apache.flink.api.java.operators.UnsortedGrouping;
import org.apache.flink.api.java.tuple.Tuple2;

import java.util.ArrayList;


/**
 * @author byj
 * @date 2024/9/18
 * @Description aggregate
 * 按照内置的方式来进行聚合, Aggregate只能作用于元组上。例如：SUM/MIN/MAX…
 */
public class GroupByAggregateExample {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        DataSet<Tuple2<String, Integer>> wordCountDataSet = env.fromCollection(new ArrayList<Tuple2<String, Integer>>() {{
            add(new Tuple2<>("java", 1));
            add(new Tuple2<>("java", 1));
            add(new Tuple2<>("scala", 1));
        }});

        UnsortedGrouping<Tuple2<String, Integer>> groupedDataSet = wordCountDataSet.groupBy(0);

        DataSet<Tuple2<String, Integer>> resultDataSet = groupedDataSet.aggregate(Aggregations.SUM, 1);

        resultDataSet.print();
    }

}
