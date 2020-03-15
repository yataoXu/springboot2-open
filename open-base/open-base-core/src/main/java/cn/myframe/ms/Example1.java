package cn.myframe.ms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * (1)泛型T不支持基本数据类型
 * (2)Arrays.asList(),调用Arrays的内部类ArrayList,没有实现add()方法
 *
 * @Author: ynz
 */
public class Example1 {

    public static void main(String[] args) {
        int[] test = new int[]{1,2,3,4};
        List list = Arrays.asList(test);
        //输出什么?
        System.out.println(list.size());
        //为什么不能添加元素?
        //list.add(5);

        Example1 example1 = new Example1();

        System.out.println(example1.hashCode());

        Integer integer = new Integer(10);

    }

}

