package com.zmz.leetcode.tag.array1;

/**
 * 最大子数组和
 */
public class MaxSubArray53 {

    /**
     * 给你一个整数数组 nums ，请你找出一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。
     * <p>
     * 子数组 是数组中的一个连续部分。
     */

    public int maxSubArray(int[] nums) {
        // 解题思路比较直接
        // 考虑当累加至负数时 就已经没有必要再次累加下去了
        // 但是数组是有可能全为负数的 这种情况下需要先比较大小进行赋值  再清空数组 否则就会导致最大值为初始值
        // 这种思路是贪心算法

        int res = nums[0];
        int add = 0;
        for (int num : nums) {
            add += num;
            if (add >= res) {
                res = add;
            }
            if (add < 0) {
                add = 0;
            }
        }
        return res;
    }

    public int maxSubArray2(int[] nums) {
        // 这题也是一个简单的动态规划问题
        // 其实动态规划算法 我的理解是 只要每个子问题都有一个共性，那么就可以利用动态规划算法进行求解
        // 找到子问题的通解 也就是找到状态转移方程

        // 针对本题 只要上个元素的最优解大于0 那么当前元素的最优解就是 累加上当前元素
        // 否则当前元素的最优解就是当前元素本身
        int res = nums[0];
        int[] resNums = new int[nums.length];
        resNums[0] = nums[0];
        for (int i = 1; i < nums.length; i++) {
            if (resNums[i - 1] > 0) {
                resNums[i] = resNums[i - 1] + nums[i];
            } else {
                resNums[i] = nums[i];
            }
            res = Math.max(res, resNums[i]);
        }
        return res;
    }

    public static void main(String[] args) {
        int[] nums = new int[]{-2,1,-3,4,-1,2,1,-5,4};
        MaxSubArray53 maxSubArray53 = new MaxSubArray53();
        int i = maxSubArray53.maxSubArray2(nums);
        System.err.println(i);
    }


}
