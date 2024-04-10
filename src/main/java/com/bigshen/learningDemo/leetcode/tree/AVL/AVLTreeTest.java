package com.bigshen.learningDemo.leetcode.tree.AVL;

/**
 * @author byj
 * @date 2024/4/3
 * @Description 平衡二叉树
 */
public class AVLTreeTest {
    private static final int[] arr = {3,2,1,4,5,6,7,16,15,14,13,12,11,10,8,9};

    public static void main(String[] args) {
        int i;
        AVLTree<Integer> tree = new AVLTree<>();

        System.out.print("== 依次添加: ");
        for(i=0; i<arr.length; i++) {
            System.out.printf("%d ", arr[i]);
            tree.insert(arr[i]);
        }

        System.out.print("\n== 前序遍历: ");
        tree.preOrder();

        System.out.print("\n== 中序遍历: ");
        tree.inOrder();

        System.out.print("\n== 后序遍历: ");
        tree.postOrder();
        System.out.print("\n");

        System.out.printf("== 高度: %d\n", tree.height());
        System.out.printf("== 最小值: %d\n", tree.minimum());
        System.out.printf("== 最大值: %d\n", tree.maximum());
        System.out.print("== 树的详细信息: \n");
        tree.print();

        i = 8;
        System.out.printf("\n== 删除根节点: %d", i);
        tree.remove(i);

        System.out.printf("\n== 高度: %d", tree.height());
        System.out.print("\n== 中序遍历: ");
        tree.inOrder();
        System.out.print("\n== 树的详细信息: \n");
        tree.print();

        // 销毁二叉树
        tree.destroy();

        /**
         * 测试结果
         * == 依次添加: 3 2 1 4 5 6 7 16 15 14 13 12 11 10 8 9
         * == 前序遍历: 7 4 2 1 3 6 5 13 11 9 8 10 12 15 14 16
         * == 中序遍历: 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16
         * == 后序遍历: 1 3 2 5 6 4 8 10 9 12 11 14 16 15 13 7
         * == 高度: 5
         * == 最小值: 1
         * == 最大值: 16
         * == 树的详细信息:
         *  7 is root
         *  4 is  7's   left child
         *  2 is  4's   left child
         *  1 is  2's   left child
         *  3 is  2's  right child
         *  6 is  4's  right child
         *  5 is  6's   left child
         * 13 is  7's  right child
         * 11 is 13's   left child
         *  9 is 11's   left child
         *  8 is  9's   left child
         * 10 is  9's  right child
         * 12 is 11's  right child
         * 15 is 13's  right child
         * 14 is 15's   left child
         * 16 is 15's  right child
         *
         * == 删除根节点: 8
         * == 高度: 5
         * == 中序遍历: 1 2 3 4 5 6 7 9 10 11 12 13 14 15 16
         * == 树的详细信息:
         *  7 is root
         *  4 is  7's   left child
         *  2 is  4's   left child
         *  1 is  2's   left child
         *  3 is  2's  right child
         *  6 is  4's  right child
         *  5 is  6's   left child
         * 13 is  7's  right child
         * 11 is 13's   left child
         *  9 is 11's   left child
         * 10 is  9's  right child
         * 12 is 11's  right child
         * 15 is 13's  right child
         * 14 is 15's   left child
         * 16 is 15's  right child
         * ------
         * 著作权归@pdai所有
         * 原文链接：https://pdai.tech/md/algorithm/alg-basic-tree-balance.html
         */
    }
}
