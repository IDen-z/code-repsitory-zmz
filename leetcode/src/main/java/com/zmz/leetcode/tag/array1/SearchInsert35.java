package com.zmz.leetcode.tag.array1;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 搜索插入位置
 */
public class SearchInsert35 {

    /**
     * 给定一个排序数组和一个目标值，在数组中找到目标值，并返回其索引。如果目标值不存在于数组中，返回它将会被按顺序插入的位置。
     * <p>
     * 请必须使用时间复杂度为 O(log n) 的算法。
     */
    public int searchInsert(int[] nums, int target) {
        // 这种方式是直接便利 解法一
        for (int i = 0; i < nums.length; i++) {
            // 遍历数组
            if (nums[i] >= target) {
                return i;
            }
        }
        return nums.length;
    }

    /**
     * 官方题解
     */
    public int searchInsertOfficial(int[] nums, int target) {
        // 官方希望考察的知识点是二分查找法
        // 重点是有序数组 并且是查找 那么就应该想到二分
        int length = nums.length;
        int left = 0;
        int right = length - 1;
        int res = length;
        while (left <= right) {
            int mid = (right - left) / 2 + left;
            if (nums[mid] >= target) {
                res = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return res;
    }


    public static void main(String[] args) {
        SearchInsert35 searchInsert35 = new SearchInsert35();
        int[] nums = new int[]{1, 3, 5, 6};
        int i = searchInsert35.searchInsertOfficial(nums, 0);
        System.err.println(i);
    }


}
