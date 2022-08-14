package com.bigshen.learningDemo.leetcode.queue;

import java.util.Random;
import java.util.Stack;

/**
 * @Author BYJ
 * @Date 2021/3/1 21:46
 * @Describe 两个栈实现一个队列
 */
public class MyQueue02<E> {
    private Stack<E> s1=new Stack<E>();
    private Stack<E> s2=new Stack<E>();

    /**
     * 入队
     * @param val
     */
    public void offer(E val){
        s1.push(val);
    }

    /**
     * 出队
     */
    public E poll(){
        while (s2.empty()){
            while (!s1.empty()){
                s2.push(s1.peek());
                s1.pop();
            }
        }
        E val = s2.peek();
        s2.pop();

        //获取出队元素后，再将s2里面的元素放入s1里面
        while (!s2.empty()){
            s1.push(s2.pop());
        }
        return val;
    }

    /**
     * 查看对头元素
     * @return
     */
    public E peek(){
        while (s2.empty()){
            while (!s1.empty()){
                s2.push(s1.peek());
                s1.pop();
            }
        }
        E val = s2.peek();

        //获取出队元素后，再将s2里面的元素放入s1里面
        while (!s2.empty()){
            s1.push(s2.pop());
        }
        return val;
    }

    /**
     * 判断队是否为空
     * @return
     */
    public boolean empty(){
        return s1.empty();
    }

    public static void main(String[] args) {
        MyQueue02<Integer> queue = new MyQueue02<>();
        Random rand = new Random();
        for (int i = 0; i < 5; i++) {
            int data = rand.nextInt(100);
            System.out.print(data + " ");
            queue.offer(data);
        }
        System.out.println();
        System.out.println("出队：");
        while (!queue.empty()) {
            System.out.print(queue.poll()+" ");
        }
    }

}
