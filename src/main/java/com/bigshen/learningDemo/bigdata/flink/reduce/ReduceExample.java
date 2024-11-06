package com.bigshen.learningDemo.bigdata.flink.reduce;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;

import java.util.ArrayList;


/**
 * @author byj
 * @date 2024/9/18
 * @Description reduce  reduce是将数据一个个拉取到另外一个节点，然后再执行计算
 * 可以对一个dataset或者一个group来进行聚合计算，最终聚合成一个元素
 *
 */
public class ReduceExample {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        DataSet<Tuple2<String, Integer>> wordCountDataSet = env.fromCollection(new ArrayList<Tuple2<String, Integer>>() {{
            add(new Tuple2<>("java", 1));
            add(new Tuple2<>("java", 1));
            add(new Tuple2<>("java", 1));
        }});

        DataSet<Tuple2<String, Integer>> resultDataSet = wordCountDataSet.reduce((wc1, wc2) ->
                new Tuple2<>(wc2.f0, wc1.f1 + wc2.f1)
        );

        resultDataSet.print();
    }
}
