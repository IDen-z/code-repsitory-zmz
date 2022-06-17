package com.zmz.leetcode.hot;

import java.util.HashMap;
import java.util.Objects;

public class LeetCode1 {


    /**
     * @Description: LeetCode-两数之和-1
     * @Author: Zhu Mengze
     * @Date: 2021/9/13 15:01
     */
    public static void main(String[] args) {

    }

    /**
     * 给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出 和为目标值 target  的那两个整数，并返回它们的数组下标。
     * 你可以假设每种输入只会对应一个答案。但是，数组中同一个元素在答案里不能重复出现。
     * 你可以按任意顺序返回答案。
     */
    public int[] twoSum(int[] nums, int target) {
        if (Objects.isNull(nums) || nums.length == 0) {
            return new int[0];
        }
        int[] res = new int[2];
        //使用map记录
        //key 下标  value  值
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            map.put(nums[i], i);
        }
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(target - nums[i]) && i != map.get(target - nums[i])) {
                res[0] = i;
                res[1] = map.get(target - nums[i]);
                return res;
            }
        }
        return res;
    }

    /**
    *  最优解
     *
     *  解题的关键是避免On方的循环，也就是用空间换时间
     *
     *  利用map 的key无法重复的特性，将数值大小作为key，下标作为值有条件的存入map中
    */
    public int[] twoSum2(int[] nums, int target) {
        if (Objects.isNull(nums) || nums.length == 0) {
            return new int[0];
        }
        int[] res = new int[2];
        //使用map记录
        //key 下标  value  值
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(target-nums[i])){
                res[0]=i;
                res[1]=map.get(target-nums[i]);
                return res;
            }
            map.put(nums[i],i);
        }
        return res;
    }


}
