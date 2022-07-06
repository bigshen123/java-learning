package com.bigshen.chatDemoService.leetcode.linkedList;


/**
 * @ClassName ReverseList
 * @Description: TODO
 * 链表练习：
 * 1.单链表反转
 * * 示例:
 *  * 输入: 1->2->3->4->5->NULL
 *  * 输出: 5->4->3->2->1->NULL
 *
 * 2.链表中环的检测
 * 3.两个有序链表合并
 * 4.删除链表倒数第n个节点
 * 5.求链表的中间节点
 *
 * @Author BYJ
 * @Date 2020/5/31
 * @Version V1.0
 **/
public class LinkedListDemo {

    public static class Node {
        public char c;
        public Node next;

        public Node(char c) {
            this.c = c;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "c=" + c +
                    ", next=" + next +
                    '}';
        }
    }

    /**
     * 单链表反转：递归解法
     * 时间复杂度O（n） 空间复杂度O（n）
     * @param head 链表
     * @return 反转后的链表
     */
    public ListNode reverseList(ListNode head) {
        if(head == null || head.next == null) {
            return head;
        }
        ListNode newHead = reverseList(head.next);
        head.next.next = head;
        head.next = null;
        return newHead;
    }

    /**
     * 单链表反转：迭代解法
     * 时间复杂度O（n） 空间复杂度O（1）
     * @param head
     * @return
     */
    private static Node reverseList(Node head) {
        //前指针节点
        Node prev = null;
        //当前指针节点
        Node curr = head;
        //每次循环，都将当前节点指向它前面的节点，然后当前节点和前节点后移
        while (curr != null) {
            //临时节点，暂存当前节点的下一节点，用于后移
            Node nextTemp = curr.next;
            //将当前节点指向它前面的节点
            curr.next = prev;
            //前指针后移
            prev = curr;
            //当前指针后移
            curr = nextTemp;
        }
        return prev;
    }

    private static ListNode buildListNode(int[] input){
        ListNode first = null,last = null,newNode;
        int num;
        if(input.length>0){
            for(int i=0;i<input.length;i++){
                newNode=new ListNode(input[i]);
                newNode.next=null;
                if(first==null){
                    first=newNode;
                    last=newNode;
                }
                else{
                    last.next=newNode;
                    last=newNode;
                }

            }
        }
        return first;
    }
    private static Node buildNode(char[] input){
        Node first = null,last = null,newNode;
        int num;
        if(input.length>0){
            for(int i=0;i<input.length;i++){
                newNode=new Node(input[i]);
                newNode.next=null;
                if(first==null){
                    first=newNode;
                    last=newNode;
                }
                else{
                    last.next=newNode;
                    last=newNode;
                }

            }
        }
        return first;
    }


    /**
     * 链表反转
     * @param head
     * @return
     */
    public static Node reverse(Node head) {
        if(head == null || head.next == null) {
            return head;
        }

        Node prev = null;
        Node cur = head;
        Node next = head.next;

        while(next != null) {
            cur.next = prev;
            prev = cur;
            cur = next;
            next = cur.next;
        }
        cur.next = prev;
        return cur;
    }

    /**
     * 链表中环检测
     * @param head
     * @return
     */
    public static boolean existsCircle(Node head) {
        Node slow = head;
        Node fast = head;
        while(fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if(slow == fast) {
                return true;
            }
        }
        return false;
    }

    /**
     * 两个有序链表合并
     * @param head1
     * @param head2
     * @return
     */
    public static Node merge(Node head1, Node head2) {

        Node guard = new Node('/');
        Node cur = guard;

        while(head1 != null && head2 != null) {
            if(head1.c <= head2.c) {
                while(head1 != null && head1.c <= head2.c) {
                    cur.next = head1;
                    cur = cur.next;
                    head1 = head1.next;

                }
            } else {
                while(head2 != null && head1.c > head2.c) {
                    cur.next = head2;
                    cur = cur.next;
                    head2 = head2.next;

                }
            }
        }

        if(head1 != null) {
            cur.next = head1;
        }
        if(head2 != null) {
            cur.next = head2;
        }

        return guard.next;

    }

    /**
     * 删除链表倒数第n个的节点
     * @param head
     * @param n
     * @return
     */
    public static Node deleteLastN(Node head, int n) {
        if(n < 1 || head == null) {
            return head;
        }
        Node guard = new Node('/');
        guard.next = head;

        Node slow = guard;
        Node fast = guard;

        for(int i = 0; i < n; i++) {
            if(fast != null) {
                fast = fast.next;
            }
        }
        while(fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next;
        }
        slow.next = slow.next.next;
        return guard.next;
    }
    private static ListNode removeElements(ListNode head,int val){
        if(head==null){
            return null;
        }
        ListNode p=head,q=head.next;
        while(q!=null){
            if(q.val==val){
                p.next=q.next;
                q=q.next;
            }else{
                p=p.next;
                q=q.next;
            }
        }
        if(head.val==val){
            return head.next;
        }
        return head;

    }

    /**
     * 求链表中间节点
     * @param head
     * @param n
     * @return
     */
    public static Node getMiddle(Node head, int n) {
        Node slow = head;
        Node fast = head;

        while(fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        return slow;
    }

    /**
     * main
     * @param args
     */
    public static void main(String[] args) {
        char[] input1=new char[]{'a','b','c'};
        char[] input2=new char[]{'c','d'};
        Node node1 = buildNode(input1);
        Node node2 = buildNode(input2);
        Node node = reverseList(node1);
        System.out.println(node);
        //ListNode listNode1 = reverseList(listNode);
        //System.out.println(listNode1);
        //Node merge = merge(node1, node2);
        //System.out.println(merge);
        //reverseList()
    }
}
