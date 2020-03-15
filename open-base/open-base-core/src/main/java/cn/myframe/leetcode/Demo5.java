package cn.myframe.leetcode;

/**
 * @Author: ynz
 * @Date: 2019/3/15/015 15:29
 * @Version 1.0
 */
public class Demo5 {

    public static void main(String[] args) {
        int[] height = {5,4,1,2};
        int total = 0 ;
        for (int i = 0; i < height.length; i++) {
            int area = 0 ;
            int second = 0;
            int secondArea = 0;
            for (int j = i+1; j < height.length; j++) {
                if(height[i]<height[j]){
                    total += height[i]*(j-i-1)-area;
                    i = j-1;
                    break;
                }
                if(second ==0||height[j]>=height[second]){
                    second = j;
                    secondArea = area;
                }
                if(j == height.length-1){
                    total += height[second]*(second-i-1)-secondArea;
                    i = second-1;
                    break;
                }
                area += height[j];

            }
        }
        System.out.println(total);
    }
}
