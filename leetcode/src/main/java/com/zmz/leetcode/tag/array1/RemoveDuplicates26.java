package com.zmz.leetcode.tag.array1;

/**
 * 删除有序数组中的重复项
 */
public class RemoveDuplicates26 {

    /**
     * 给你一个 升序排列 的数组 nums ，请你 原地 删除重复出现的元素，使每个元素 只出现一次 ，返回删除后数组的新长度。元素的 相对顺序 应该保持 一致 。
     * <p>
     * 由于在某些语言中不能改变数组的长度，所以必须将结果放在数组nums的第一部分。更规范地说，如果在删除重复项之后有 k 个元素，那么 nums 的前 k 个元素应该保存最终结果。
     * <p>
     * 将最终结果插入 nums 的前 k 个位置后返回 k 。
     * <p>
     * 不要使用额外的空间，你必须在 原地 修改输入数组 并在使用 O(1) 额外空间的条件下完成。
     */
    public int removeDuplicates(int[] nums) {
        int res = 1;
        // 快慢指针
        // 一个快指针 一个慢指针
        int slowIndex = 0;
        int fastIndex = 0;
        while (fastIndex <= nums.length - 1) {
            if (nums[slowIndex] <= nums[slowIndex + 1]) {
                fastIndex++;
            }

        }


        return res;
    }

    public static void main(String[] args) {

        RemoveDuplicates26 removeDuplicates26 = new RemoveDuplicates26();
        int[] nums = new int[]{1, 2, 3};
        int i = removeDuplicates26.removeDuplicates(nums);
        System.err.println(i);
    }


}
