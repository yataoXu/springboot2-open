package cn.myframe.leetcode;

/**
 * @Author: ynz
 * @Date: 2019/3/13/013 16:27
 * @Version 1.0
 */
public class Demo3 {

    public static int rob(int[] nums) {
        int n = nums.length;
        if (n == 0) {
            return 0;
        }
        // memo[i] 表示考虑抢劫 nums[i...n-1] 所能获得最大收益（不是说一定从 i 开始抢劫）
        int[] memo = new int[n];
        // 先考虑最简单的情况
        memo[n - 1] = nums[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            // memo[i] 的取值在考虑抢劫 i 号房子和不考虑抢劫之间取最大值
            memo[i] = Math.max(nums[i] + (i + 2 >= n ? 0 : memo[i + 2]), nums[i + 1] + (i + 3 >= n ? 0 :memo[i + 3]));
        }

        return memo[0];

    }

    public static void main(String[] args) {
        int[] nums = {9,1,1,10,2,1,6};
        System.out.println(rob( nums));
    }
}
