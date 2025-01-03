package com.bigshen.learningDemo.javaSE.lambda;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName StudentScoreSum
 * @Description:TODO
 * @Author: byj
 * @Date: 2020/5/26
 */
public class StudentScoreSum {
    @Data
    static class StudentScore {
        private Integer sid;
        private String scoreName;
        private Integer score;

        public StudentScore(Integer sid, String scoreName, Integer score) {
            this.sid = sid;
            this.scoreName = scoreName;
            this.score = score;
        }

        public StudentScore() {
        }
    }

    public static void main(String[] args) {
        List<StudentScore> list = new ArrayList<>();
        list.add(new StudentScore(1, "chinese", 110));
        list.add(new StudentScore(1, "english", 120));
        list.add(new StudentScore(1, "math", 135));
        list.add(new StudentScore(2, "chinese", 99));
        list.add(new StudentScore(2, "english", 100));
        list.add(new StudentScore(2, "math", 133));
        list.add(new StudentScore(3, "chinese", 88));
        list.add(new StudentScore(3, "english", 140));
        list.add(new StudentScore(3, "math", 90));
        list.add(new StudentScore(4, "chinese", 108));
        list.add(new StudentScore(4, "english", 123));
        list.add(new StudentScore(4, "math", 114));
        list.add(new StudentScore(5, "chinese", 116));
        list.add(new StudentScore(5, "english", 133));
        list.add(new StudentScore(5, "math", 135));

        System.out.println(sum1(list));
        System.out.println(sum2(list));
    }
    /**
     传统写法
     */
    public static Map<Integer, Integer> sum1(List<StudentScore> list) {
        Map<Integer, Integer> map = new HashMap<>();
        for (StudentScore studentScore : list) {
            if (map.containsKey(studentScore.getSid())) {
                map.put(studentScore.getSid(),
                        map.get(studentScore.getSid()) + studentScore.getScore());
            } else {
                map.put(studentScore.getSid(), studentScore.getScore());
            }
        }
        return map;
    }

    /**
     merger写法
     */
    public static Map<Integer, Integer> sum2(List<StudentScore> list) {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(6,140);
        list.stream().forEach(studentScore -> map.merge(studentScore.getSid()
                , studentScore.getScore(), Integer::sum));
        return map;
    }
}
