package cn.myframe.test;

import java.io.UnsupportedEncodingException;

/**
 * @Author: ynz
 * @Date: 2019/11/12/012 8:42
 * @Version 1.0
 */
public class Test4 {

    public static void main(String[] args) throws UnsupportedEncodingException {

        Character ch = '我';
        String str = "我";
        System.out.println(str.getBytes("gbk").length);
        System.out.println(ch);
        int a = Character.digit('a',16);
        System.out.println(a);

        System.out.println(cal("a9a",16));
    }

    public static int cal(String value,int radix){
        int result = 0;
        for (int i = 0,len = value.length(); i < len; i++) {
            char ch = value.charAt(i);
            int digit = Character.digit(ch,radix);
            result = result + digit;
            result *= radix;
        }
        return result/radix;
    }

}
