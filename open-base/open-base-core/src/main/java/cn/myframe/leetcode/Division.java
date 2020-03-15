package cn.myframe.leetcode;

/**
 * @Author: ynz
 * @Date: 2019/3/18/018 9:03
 * @Version 1.0
 */
public class Division {

    public static void main(String[] args) {
        int[] data = {1,1};
        System.out.println(index(data,1,true));
    }

  /*  public  static int[] searchRange(int[] nums, int target) {



    }*/

    public int[] searchRange(int[] nums, int target) {
        return new int[]{1,3};
    }

    public static int index(int[] nums,int target,boolean first){
        if(nums.length== 0){
            return  -1;
        }
        int left = 0;
        int right = nums.length-1;
        while(left != right-1 && left != right){
            int mid = (left+right)/2;
            if(target < nums[mid] ||(first && target == nums[mid] )){
                right = mid;
            }else{
                left =  mid;
            }
        }
        int index = -1;
        if(nums[left] == target){
            index = left;
        }
        if(nums[right] == target){
            index = right;
        }
        if(nums[right] == nums[left]){
            if(index != -1){
                index= first ? left:right;
            }
        }
        return index;
    }


}
