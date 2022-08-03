package com.bigshen.learningDemo.leetcode.linkedList;

/**
 * @ClassName ListNode
 * @Description: TODO
 * @Author BYJ
 * @Date 2020/5/24
 * @Version V1.0
 **/
public class ListNode {
    int val;
    ListNode next;

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public ListNode getNext() {
        return next;
    }

    public void setNext(ListNode next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return "ListNode{" +
                "val=" + val +
                ", next=" + next +
                '}';
    }

    public ListNode(int x) {
        val = x;
    }

}
