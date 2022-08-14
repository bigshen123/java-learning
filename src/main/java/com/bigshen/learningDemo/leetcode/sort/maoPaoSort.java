package com.bigshen.learningDemo.leetcode.sort;

import java.util.Arrays;

/**
 * @Description:冒泡排序
 * @Author: BIGSHEN
 * @Date: 2019/12/5 20:07
 */
public class maoPaoSort {
    public static void main(String[] args) {
        int score[]={12,89,43,34,56};
        oneMethod(score);
        System.out.println("正序排序后的数组："+ Arrays.toString(score));
        twoMethod(score);
        System.out.println("倒叙排列后的数组: "+Arrays.toString(score));
    }
    private static void oneMethod(int[] score){
        int temp;
        for (int i = 0; i < score.length-1; i++) {
            for (int j = 0; j < score.length - 1 - i; j++) {
                if (score[j]>score[j+1]){
                    temp=score[j];
                    score[j]=score[j+1];
                    score[j+1]=temp;
                }
            }
        }
    }
    private static void twoMethod(int[] score){
        int temp;
        for (int i = 0; i < score.length - 1; i++) {
            for (int j=score.length-2;j>=i;j--){
                if (score[j]<score[j+1]){
                    temp=score[j];
                    score[j]=score[j+1];
                    score[j+1]=temp;
                }
            }
        }



    }
}
