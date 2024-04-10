package com.bigshen.learningDemo.leetcode.tree.Huffman;

/***
 * @author byj
 * @date 2024/4/3
 * @Description
 */
public class HuffmanNode implements Comparable<Object>, Cloneable {
    protected int key;              // 权值
    protected HuffmanNode left;     // 左孩子
    protected HuffmanNode right;    // 右孩子
    protected HuffmanNode parent;   // 父结点

    protected HuffmanNode(int key, HuffmanNode left, HuffmanNode right, HuffmanNode parent) {
        this.key = key;
        this.left = left;
        this.right = right;
        this.parent = parent;
    }

    @Override
    public Object clone() {
        Object obj = null;

        try {
            obj = super.clone();//Object 中的clone()识别出你要复制的是哪一个对象。
        } catch (CloneNotSupportedException e) {
            System.out.println(e.toString());
        }

        return obj;
    }

    @Override
    public int compareTo(Object obj) {
        return this.key - ((HuffmanNode) obj).key;
    }
}
