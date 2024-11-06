package com.bigshen.learningDemo.bigdata.flink;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;

/**
 * @author byj
 * @date 2024/9/18
 * @Description rebalance
 * Flink也会产生数据倾斜的时候，例如：当前的数据量有10亿条，在处理过程就有可能发生如下状况：分区1 处理9.9亿数据 其他分区只有几百数据
 * rebalance会使用轮询的方式将数据均匀打散，这是处理数据倾斜最好的选择。
 *
 */
public class MapWithSubtaskIndexExample {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        // 使用env.generateSequence创建0-100的并行数据
        DataSet<Long> numDataSet = env.generateSequence(0, 100);

        // 使用fiter过滤出来大于8的数字
        DataSet<Long> filterDataSet = numDataSet.filter(num -> num > 8);

        // 使用map操作传入RichMapFunction，将当前子任务的ID和数字构建成一个元组
        // 在RichMapFunction中可以使用getRuntimeContext.getIndexOfThisSubtask获取子任务序号
        DataSet<Tuple2<Integer, Long>> resultDataSet = filterDataSet.map(new RichMapFunction<Long, Tuple2<Integer, Long>>() {
            @Override
            public Tuple2<Integer, Long> map(Long in) {
                return new Tuple2<>(getRuntimeContext().getIndexOfThisSubtask(), in);
            }
        });

        resultDataSet.print();
    }
}
