package com.bigshen.learningDemo.javaSE.split;

import static com.bigshen.learningDemo.javaSE.split.DataChain.printChain;

/**
 * @ClassName reverse2
 * @Description: TODO
 * @Author BYJ
 * @Date 2020/7/15
 * @Version V1.0
 **/
public class reverse {
    public static void main(String... strings) {
        int size = 10;

        DataChain chain2 = new DataChain(size);
        printChain(chain2.getHead());
        long reverse2_start = System.currentTimeMillis();
        DataNode reNode2 = reverse2(chain2.getHead());
        long reverse2_cost = System.currentTimeMillis() - reverse2_start;
        printChain(reNode2);
        System.out.println("reverse2 cost time is ["+reverse2_cost+"]ms");


    }

    public static DataNode reverse2(DataNode head) {
        if (null == head || null == head.getNext())
            return head;
        DataNode pre = head;
        DataNode cur = head.getNext();
        while (null != cur.getNext()) {
            DataNode tmp = cur.getNext();
            cur.setNext(pre);
            pre = cur;
            cur = tmp;
        }
        cur.setNext(pre);
        head.setNext(null);
        return cur;
    }

}
