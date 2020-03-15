package cn.myframe.ms;

import java.util.*;

/**
 * @Author: ynz
 * @Date: 2019/1/16/016 10:10
 * @Version 1.0
 */
public class Example2 {
    public static void main(String[] args) throws ClassNotFoundException {
        //三种获取时间截的方法
        System.out.println(Calendar.getInstance().getTimeInMillis());
        System.out.println(System.currentTimeMillis());
        System.out.println(new Date().getTime());

        //获取字节码的三种方式
        Class c1 = Example2.class;
        Class c2 = new Example2().getClass();
        Class c3 = Class.forName("cn.myframe.ms.Example2");
        System.out.println(c1);


        //返回false,'=='无论equals(),hashCode()相不相等,都只比较两个对象的地址是否相等
        System.out.println(new Example2() == new Example2());
        //返回true
        System.out.println(new Example2().equals(new Example2()));

        Set<Example2> set = new HashSet<>();
        set.add(new Example2());
        set.add(new Example2());
        //在散列表中必须同时满足同时满足hashCode()和equals()相等才会认为对象相等
        System.out.println(set.size());

    }

    public int hashCode(){
       //set.size()为等于2
       // return new Random().nextInt(Integer.MAX_VALUE);
        //set.size()为等于1
       return 1;
    }

    public int[] twoSum(int[] nums, int target) {
        Map<Integer,Integer> map = new HashMap<Integer,Integer>();
        for(int len= nums.length,i=0;i<len;i++){
            int k = target - nums[0];
            if(map.containsKey(k)){
                int v = map.get(k);
                return new int[]{i,v};
            }
            map.put(nums[i],i);
        }
        return new int[]{0,0};
    }

    @Override
    public boolean equals(Object obj) {
        return true;
    }
}
