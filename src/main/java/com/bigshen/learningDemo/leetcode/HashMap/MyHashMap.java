package com.bigshen.learningDemo.leetcode.HashMap;

import java.util.LinkedList;
import java.util.List;

/**
 * @Author BYJ
 * @Date 2021/3/14 20:26
 * @Describe 不使用任何内建的哈希表库设计一个哈希映射（HashMap）。
 *
 * 实现 MyHashMap 类：
 *
 * MyHashMap() 用空映射初始化对象
 * void put(int key, int value) 向 HashMap 插入一个键值对 (key, value) 。如果 key 已经存在于映射中，则更新其对应的值 value 。
 * int get(int key) 返回特定的 key 所映射的 value ；如果映射中不包含 key 的映射，返回 -1 。
 * void remove(key) 如果映射中存在 key 的映射，则移除 key 和它所对应的 value 。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/design-hashmap
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class MyHashMap {

    //定义数据结构存储
    class Node{
        private int key;
        private int value;
        public Node(int key,int value){
            this.key = key;
            this.value = value;
        }
    }
    private List<Node>[] map;
    private static final int capacity = 857;

    //构造函数
    public MyHashMap() {
        map = new LinkedList[capacity];
    }

    //计算hash
    public int hash(int key){
        return key % capacity;
    }

    //put元素
    public void put(int key, int value) {
        int myHashCode = hash(key);
        if(map[myHashCode] == null){
            List<Node> list = new LinkedList<>();
            list.add(new Node(key,value));
            map[myHashCode] = list;
        }else{
            List<Node> list = map[myHashCode];
            for(Node m : list){
                if(m.key == key){
                    m.value = value;
                    return;
                }
            }
            list.add(new Node(key,value));
        }
    }

    //get元素
    public int get(int key) {
        int myHashCode = hash(key);
        if(map[myHashCode] == null){
            return -1;
        }
        List<Node> list = map[myHashCode];
        int res = -1;
        for(Node m : list){
            if(m.key == key){
                res = m.value;
                break;
            }
        }
        return res;
    }


    //删除元素
    public void remove(int key) {
        int myHashCode = hash(key);
        if(map[myHashCode] == null){
            return;
        }
        List<Node> list = map[myHashCode];
        for(Node m : list){
            if(m.key == key){
                list.remove(m);
                return;
            }
        }
    }
}

