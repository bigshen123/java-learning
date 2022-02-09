package com.bigshen.chatDemoService.leetcode.sort;

/**
 * @ClassName HandleLetterAndNumber
 * @Description 对字符串进行排序
  * 要求: 小写字母的放到前面，大写字母放在最后，数字放在中间，不用排序算法，要怎么解决呢？
  * 运用双指针进行处理, 首先对小写字母和数字与大写字母进行区分,然后在对小写字母与数字进行区分
  * 而双指针的终止条件是头尾指针相遇
 * @Author BYJ
 * @Date 2020/8/30
 * @Version V1.0
 **/

import java.util.Arrays;
import java.util.function.Predicate;

public class HandleLetterAndNumber {

    private static Character[] handleLetterAndLetter(Character[] originalArray) {
        // 小写字母和数字与大写字母进行区分, 并获取两种数据的分界线
        int tail = handle(originalArray, c -> Character.isLowerCase(c) || Character.isDigit(c), originalArray.length - 1);
        // 小写字母与数字进行区分
        handle(originalArray, Character::isLowerCase, tail);
        return originalArray;
    }

    private static <T> int handle(T[] originalArray, Predicate<T> conditions, int tail) {

        int head = 0;
        while (head != tail) {
            if (!conditions.test(originalArray[head])) {
                if (conditions.test(originalArray[tail])) {
                    swap(head, tail, originalArray);
                } else {
                    tail--;
                }
            } else {
                head++;
            }
        }
        return tail;
    }

    private static <T> void swap(int beforeSwapIndex, int afterSwapIndex, T[] swappingArray) {
        T temp = swappingArray[afterSwapIndex];
        swappingArray[afterSwapIndex] = swappingArray[beforeSwapIndex];
        swappingArray[beforeSwapIndex] = temp;
    }

    public static void main(String[] args) {
        Character[] afterHandleLetterAndLetter = handleLetterAndLetter(new Character[]{'a', 'A', '1', 'b', 'B', 'g', '6', 't', '8', 'L', 'd'});
        System.out.println(Arrays.toString(afterHandleLetterAndLetter));// [a, d, t, b, g, 8, 6, 1, B, L, A]
    }
}
