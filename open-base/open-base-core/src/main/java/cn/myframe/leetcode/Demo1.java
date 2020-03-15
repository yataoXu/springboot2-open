package cn.myframe.leetcode;

import java.util.HashMap;
import java.util.Map;

/**
 * 给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那 两个 整数，并返回他们的数组下标。
 * 给定 nums = [2, 7, 11, 15], target = 9
 * 因为 nums[0] + nums[1] = 2 + 7 = 9
 * 所以返回 [0, 1]
 *
 * 语法错误:
 * 发现赋值int len= nums.length,i=0;
 * 数组长度nums.length,不是length()
 * map.containsKey(k)
 */
public class Demo1 {

    public int[] twoSum(int[] nums, int target) {
        Map<Integer,Integer> map = new HashMap<Integer,Integer>();
        for(int len= nums.length,i=0;i<len;i++){
            int k = target - nums[i];
            if(map.containsKey(k)){
                int v = map.get(k);
                return new int[]{v,i};
            }
            map.put(nums[i],i);
        }
        return new int[]{0,0};
    }

    public static void main(String[] args) {

    }
}
