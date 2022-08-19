package com.bigshen.learningDemo.leetcode.string;

/**
 * Alphabetical order is a system whereby character strings are placed in order
 * based on the position of the characters in the conventional ordering of an
 * alphabet. Wikipedia: https://en.wikipedia.org/wiki/Alphabetical_order
 */
class Alphabetical {

    public static void main(String[] args) {
        System.out.println(!isAlphabetical("123abc"));
        System.out.println(isAlphabetical("aBC"));
        System.out.println(isAlphabetical("abc"));
        System.out.println(!isAlphabetical("xyzabc"));
        System.out.println(isAlphabetical("abcxyz"));
    }

    /**
     * Check if a string is alphabetical order or not
     *
     * @param s a string
     * @return {@code true} if given string is alphabetical order, otherwise
     * {@code false}
     */
    public static boolean isAlphabetical(String s) {
        s = s.toLowerCase();
        for (int i = 0; i < s.length() - 1; ++i) {
            if (!Character.isLetter(s.charAt(i)) || s.charAt(i) > s.charAt(i + 1)) {
                return false;
            }
        }
        return true;
    }
}
