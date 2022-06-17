package com.zmz.leetcode.daily;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 力扣1447 每日一题
 * <p>
 * 给你一个整数 n ，请你返回所有 0 到 1 之间（不包括 0 和 1）满足分母小于等于  n 的 最简 分数 。
 *  * 分数可以以 任意 顺序返回。
 */


public class LeetCode1447 {
    public static void main(String[] args) {
        List<String> strings = simplifiedFractions(5);
        System.err.println(strings);
    }

    public static List<String> simplifiedFractions(int n) {
        // 特殊判定
        if (n <= 0) {
            return new ArrayList<>();
        }
        List<String> res = new ArrayList<>();
        int loop = n;
        while (loop > 0) {
            int value = 1;
            while (value < loop) {
                if (gcd(loop, value) == 1) {
                    res.add(value + "/" + loop);
                }
                value++;
            }
            loop--;
        }
        return res;
    }

    /**
     * 递归判断是否是最简分数
     */
    public static int gcd(int a, int b) {
        return b != 0 ? gcd(b, a % b) : a;
    }


}
