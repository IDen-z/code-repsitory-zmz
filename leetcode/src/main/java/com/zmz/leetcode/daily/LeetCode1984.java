package com.zmz.leetcode.daily;


import com.sun.scenario.animation.shared.ClipEnvelope;

import java.util.Arrays;

/**
 * leetcode 1984 学生分数的最小差值
 * <p>
 * 给你一个 下标从 0 开始 的整数数组 nums ，其中 nums[i] 表示第 i 名学生的分数。另给你一个整数 k 。
 * <p>
 * 从数组中选出任意 k 名学生的分数，使这 k 个分数间 最高分 和 最低分 的 差值 达到 最小化 。
 * <p>
 * 返回可能的 最小差值 。
 */
public class LeetCode1984 {


    public static void main(String[] args) {

        int[] nums = new int[] {9};
        System.err.println(minimumDifference(nums,2));
    }

    public static int minimumDifference(int[] nums, int k) {
        if (nums.length <= 0 || k == 1) {
            return 0;
        }
        // 排序
        Arrays.sort(nums);
        if (nums.length <= k) {
            return nums[nums.length - 1] - nums[0];
        }
        // 根据k作为间隔进行遍历  滑动窗口思想
        int res = nums[nums.length-1];
        int index = k - 1;
        while (index < nums.length) {
            res = Math.min(res, nums[index] - nums[index - (k - 1)]);
            index++;
        }
        return res;
    }


}
