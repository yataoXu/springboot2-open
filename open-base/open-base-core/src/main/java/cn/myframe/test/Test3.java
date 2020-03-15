package cn.myframe.test;

import java.util.Arrays;
import java.util.Objects;

/**
 * @Author: ynz
 * @Date: 2019/9/16/016 11:50
 * @Version 1.0
 */
public class Test3 {
    static char c;
    static int i;

    public static void main(String[] args) {
        System.out.println(Math.floorMod(-100,-3));
        System.out.println(-100 % 3);
        char i = '\u123a';
        System.out.println(i);
        short s1 = 4;
        short s2 = 6;
        int a = 5;
        a = a * (a=2);
        System.out.println(a);
        String str = new String(new char[]{'a','b'});

        char[] ch = new char[]{'s','t','r','i','n','g'};
        char[] ch1 = Arrays.copyOf(ch,ch.length);

        short s = 1;
        byte b = 2;
        //short ss = s + b;
    }



}

interface A{
       int a = 0;
}
