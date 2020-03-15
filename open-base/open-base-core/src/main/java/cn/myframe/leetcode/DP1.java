package cn.myframe.leetcode;

/**
 * @Author: ynz
 * @Date: 2019/3/14/014 8:37
 * @Version 1.0
 */
public class DP1 {
    public static void main(String[] args) {
        int[][] dp = {{5},{500,4},{10,4,7},{2,4,100,300}};
        for (int i = dp.length-2; i >= 0; i--) {
            //获取第三行的数组
            int[]array = dp[i];
            for (int j = 0; j < array.length; j++) {
                array[j] = Math.max(array[j]+dp[i+1][j],array[j]+dp[i+1][j+1]);
            }
            
        }
        System.out.println(dp[0][0]);
    }
}
