package com.zmz.leetcode.daily;

import java.util.Arrays;

public class LeetCode1189 {
    public static void main(String[] args) {
        String s= "loonbalxballpoon";
        System.err.println(maxNumberOfBalloons(s));
    }

    public static int maxNumberOfBalloons(String text) {

        int[] cnt = new int[5];
        for (int i = 0; i < text.length(); ++i) {
            char ch = text.charAt(i);
            if (ch == 'b') {
                cnt[0]++;
            } else if (ch == 'a') {
                cnt[1]++;
            } else if (ch == 'l') {
                cnt[2]++;
            } else if (ch == 'o') {
                cnt[3]++;
            } else if (ch == 'n') {
                cnt[4]++;
            }
        }
        cnt[2] /= 2;
        cnt[3] /= 2;
        Arrays.sort(cnt);
        return cnt[0];

    }

}
