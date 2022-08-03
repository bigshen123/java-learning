package com.bigshen.learningDemo.leetcode.sort.merge;

import java.util.Arrays;

/**
 * @ClassName MergeSort2
 * @Description: TODO ��·�鲢�㷨
 * @Author BYJ
 * @Date 2020/10/18
 * @Version V1.0
 **/
public class MergeSort2 {

    /**
     * ��·�鲢�㷨
     * @param a
     * @param left
     * @param mid
     * @param right
     */
    public static void merge(int[] a,int left,int mid,int right){
        int[] tmp=new int[a.length];
        //p1,p2�Ǽ��ָ�룬k�Ǵ��ָ��
        int p1=left,p2=mid+1,k=left;
        while (p1<=mid && p2<=right){
            if (a[p1]<=a[p2]){
                tmp[k++]=a[p1++];
            }else{
                tmp[k++]=a[p2++];
            }
        }
        //�����һ������δ����ֱ꣬�ӽ���������Ԫ�ؼӵ��ϲ���������
        while (p1<=mid){
            tmp[k++]=a[p1++];
        }
        //ͬ��
        while (p2<=right){
            tmp[k++]=a[p2++];
        }
        //���ƻ�ԭ����
        for (int i=left;i<=right;i++){
            a[i]=tmp[i];
        }
    }

    /**
     * �鲢����
     * @param a
     * @param start
     * @param end
     */
    public static void mergeSort(int[] a,int start,int end){
        //����������ֻ��һ��Ԫ��ʱ�����ݹ�
        if (start<end){
            //����������
            int mid=(start+end)/2;
            //����������н��еݹ�����
            mergeSort(a,start,mid);
            //���Ҳ������н��еݹ�����
            mergeSort(a,mid+1,end);
            //�ϲ�
            merge(a,start,mid,end);
        }
    }

    public static void main(String[] args) {
        int[] arr = {12, 2, 45, 6, 34, 77, 4, 22, 54, 36};
        System.out.println("����ǰ�����飺" + Arrays.toString(arr));
        mergeSort(arr,0,arr.length-1);
        System.out.println("���������飺" + Arrays.toString(arr));
    }
}
