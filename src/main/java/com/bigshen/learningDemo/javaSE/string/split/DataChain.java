package com.bigshen.learningDemo.javaSE.string.split;

/**
 * @ClassName DataChain
 * @Description: TODO
 * @Author BYJ
 * @Date 2020/7/15
 * @Version V1.0
 **/
public class DataChain {

    private  DataNode head;

    public DataChain(int size) {
        DataNode head = new DataNode(0);
        DataNode cur = head;
        for (int i = 1; i < size; i++) {
            DataNode tmp = new DataNode(i);
            cur.setNext(tmp);
            cur = tmp;
        }
        this.head = head;
    }

    public DataNode getHead() {
        return head;
    }

    public void setHead(DataNode head) {
        this.head = head;
    }

    public static void printChain(DataNode head) {
        StringBuilder sb = new StringBuilder();
        DataNode cur = head;
        sb.append(cur.getData());
        while (null != cur.getNext()) {
            sb.append(" -> ");
            sb.append(cur.getNext().getData());
            cur = cur.getNext();
        }
        System.out.println(sb.toString());
    }

    public static void main(String... strings) {
        DataChain chain = new DataChain(10);
        printChain(chain.getHead());
    }

}
