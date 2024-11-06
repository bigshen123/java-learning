package com.bigshen.learningDemo.bigdata.flink;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;

import java.util.ArrayList;

/**
 * @author byj
 * @date 2024/9/18
 * @Description filter
 * 过滤出来一些符合条件的元素   过滤出来以下以h开头的单词。
 */
public class FilterExample {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        DataSet<String> wordDataSet = env.fromCollection(new ArrayList<String>() {{
            add("hadoop");
            add("hive");
            add("spark");
            add("flink");
        }});

        DataSet<String> resultDataSet = wordDataSet.filter(word -> word.startsWith("h"));

        resultDataSet.print();
    }
}
