package cn.myframe.leetcode;

/**
 * @Author: ynz
 * @Date: 2019/3/13/013 17:11
 * @Version 1.0
 */
public class Demo4 {

    public static void main(String[] args) {
        System.out.println(time(15));
    }


    public static int time(int max){
        int a[] = {9,5,3,1};
        int dp[] = new int[max+1];
        dp[0] = 0;
        for (int i = 1; i <= max; i++) {
            for (int j = 0; j < a.length; j++) {
                if(i>=a[j]){
                    dp[i] = dp[i-a[j]] +1;
                    break;
                }

            }
        }
        return dp[max];
    }
}
