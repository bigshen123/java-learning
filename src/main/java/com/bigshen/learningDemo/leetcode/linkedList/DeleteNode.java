package com.bigshen.learningDemo.leetcode.linkedList;

/**
 * @ClassName DeleteNode
 * @Description: TODO
 * 实现一种算法，删除单向链表中间的某个节点（即不是第一个或最后一个节点），假定你只能访问该节点。
 * @Author BYJ
 * @Date 2020/5/24
 * @Version V1.0
 **/
public class DeleteNode {
    public static void deleteNode(ListNode node) {
        node.val=node.next.val;
        node.next=node.next.next;
    }

    public static void main(String[] args) {
        ListNode node =new ListNode(0);
        node.setNext(node);
        deleteNode(node);
    }
}
