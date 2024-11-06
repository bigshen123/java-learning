package com.bigshen.learningDemo.bigdata.flink;

import org.apache.flink.streaming.api.datastream.ConnectedStreams;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoMapFunction;

/**
 * @author byj
 * @date 2024/9/18
 * @Description connect
 * connect()提供了和union()类似的功能，即连接两个数据流，它与union()的区别如下:
 * connect()只能连接两个数据流，union()可以连接多个数据流。
 * connect()所连接的两个数据流的数据类型可以不一致，union()所连接的两个或多个数据流的数据类型必须一致。
 * 两个DataStream经过connect()之后被转化为ConnectedStreams, ConnectedStreams会对两个流的数据应用不同的处理方法，且两个流之间可以共享状态。
 */
public class ConnectExample {
    public static void main(String[] args) {
        StreamExecutionEnvironment senv = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<Integer> intStream  = senv.fromElements(2, 1, 5, 3, 4, 7);
        DataStream<String> stringStream  = senv.fromElements("A", "B", "C", "D");

        ConnectedStreams<Integer, String> connectedStream =
                intStream.connect(stringStream);
        DataStream<String> mapResult = connectedStream.map(new MyCoMapFunction());
        mapResult.print();
    }

    /**
     CoMapFunction的3个泛型分别对应第一个流的输入类型、第二个流的输入类型，输出类型
     */
    public static class MyCoMapFunction implements CoMapFunction<Integer, String, String>
    {
        @Override
        public String map1(Integer input1) {
            return input1.toString();
        }

        @Override
        public String map2(String input2) {
            return input2;
        }
    }
}
