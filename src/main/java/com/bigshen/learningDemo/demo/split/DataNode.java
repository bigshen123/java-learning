package com.bigshen.learningDemo.demo.split;

/**
 * @ClassName DataNode
 * @Description: TODO
 * @Author BYJ
 * @Date 2020/7/15
 * @Version V1.0
 **/
public class DataNode {
    private int data;
    private DataNode next;
    public int getData() {
        return data;
    }
    public void setData(int data) {
        this.data = data;
    }
    public DataNode getNext() {
        return next;
    }
    public void setNext(DataNode next) {
        this.next = next;
    }
    public DataNode(int data) {
        this.data = data;
    }
}
