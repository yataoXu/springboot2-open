package cn.myframe.demo;

/**
 * @Author: ynz
 * @Date: 2019/5/31/031 8:57
 * @Version 1.0
 */
public class D2 {
    public static void main(String[] args) {
        String str = "hello";
        char[] chars = str.toCharArray();
        String bin = "";
        for (int i = 0; i < chars.length; i++) {
            bin +=  Integer.toBinaryString(chars[i]);
        }
        System.out.println(bin+bin.length());
    }
}
