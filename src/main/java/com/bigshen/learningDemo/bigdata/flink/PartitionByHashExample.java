package com.bigshen.learningDemo.bigdata.flink;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.core.fs.FileSystem;

import java.util.ArrayList;

/**
 * @author byj
 * @date 2024/9/18
 * @Description hashPartition
 * 按照指定的key进行hash分区
 *
 */
public class PartitionByHashExample {

    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        // 设置并行度为2
        env.setParallelism(2);

        // 使用fromCollection构建测试数据集
        DataSet<Integer> numDataSet = env.fromCollection(new ArrayList<Integer>() {{
            add(1);
            add(1);
            add(1);
            add(1);
            add(1);
            add(1);
            add(1);
            add(2);
            add(2);
            add(2);
            add(2);
            add(2);
        }});

        // 使用partitionByHash按照字符串的hash进行分区
        DataSet<Integer> partitionDataSet = numDataSet.partitionByHash(Object::toString);

        // 调用writeAsText写入文件到data/partition_output目录中
        partitionDataSet.writeAsText("./data/partition_output", FileSystem.WriteMode.OVERWRITE);

        partitionDataSet.print();
        env.execute();
    }
}
