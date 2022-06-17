package com.zmz.leetcode.daily;

public class LeetCode1020 {

    /**
     * LeetCode 1020 飞地的数量
     * <p>
     * <p>
     * 给你一个大小为 m x n 的二进制矩阵 grid ，其中 0 表示一个海洋单元格、1 表示一个陆地单元格。
     * <p>
     * 一次 移动 是指从一个陆地单元格走到另一个相邻（上、下、左、右）的陆地单元格或跨过 grid 的边界。
     * <p>
     * 返回网格中 无法 在任意次数的移动中离开网格边界的陆地单元格的数量。
     */

    public static void main(String[] args) {
        int[][] grid = {{0, 0, 0, 0}, {1, 0, 1, 0}, {0, 1, 1, 0}, {0, 0, 0, 0}};
        int i = numEnclaves(grid);
        System.err.println(i);
    }

    public static int numEnclaves(int[][] grid) {
        int res = 0;
        // grid
        for (int i = 1; i < grid[0].length - 1; i++) {
            for (int j = 1; j < grid.length - 1; j++) {
                if (grid[i][j] == 1 && boundaryMatch(i, j, grid)) {
                    res++;
                }
            }
        }
        return res;
    }

    private static boolean boundaryMatch(int i, int j, int[][] grid) {
        // 递归终止条件
        if (grid[i][j] == 1 && (i == 0 || j == 0 || i == grid[0].length - 1 || j == grid.length - 1)) {
            return true;
        }

        // 递归匹配
        return boundaryMatch(i--, j, grid) || boundaryMatch(i++, j, grid)
                || boundaryMatch(i, j--, grid) || boundaryMatch(i, j++, grid);
    }


}
