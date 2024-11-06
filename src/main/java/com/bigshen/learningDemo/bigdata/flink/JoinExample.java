package com.bigshen.learningDemo.bigdata.flink;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple4;


/**
 * @author byj
 * @date 2024/9/18
 * @Description join
 * 使用join可以将两个DataSet连接起来
 */
public class JoinExample {
    public static class Score {
        public int id;
        public String name;
        public int subjectId;
        public double score;

        public Score() {}

        public Score(int id, String name, int subjectId, double score) {
            this.id = id;
            this.name = name;
            this.subjectId = subjectId;
            this.score = score;
        }

        @Override
        public String toString() {
            return "Score(" + id + ", " + name + ", " + subjectId + ", " + score + ")";
        }
    }

    public static class Subject {
        public int id;
        public String name;

        public Subject() {}

        public Subject(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return "Subject(" + id + ", " + name + ")";
        }
    }

    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        DataSet<Score> scoreDataSet = env.readCsvFile("./data/join/input/score.csv")
                .ignoreFirstLine()
                .pojoType(Score.class);

        DataSet<Subject> subjectDataSet = env.readCsvFile("./data/join/input/subject.csv")
                .ignoreFirstLine()
                .pojoType(Subject.class);

        DataSet<Tuple4<Integer, String, Integer, Double>> joinedDataSet = scoreDataSet.join(subjectDataSet)
                .where("subjectId")
                .equalTo("id")
                .projectFirst(0, 1, 2, 3)
                .projectSecond(1);

        joinedDataSet.print();
    }
}
