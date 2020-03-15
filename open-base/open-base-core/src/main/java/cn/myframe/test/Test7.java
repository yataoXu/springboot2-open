package cn.myframe.test;

/**
 * @Author: ynz
 * @Date: 2019/11/13/013 15:34
 * @Version 1.0
 */
public class Test7 {
    public static void main(String[] args) {
        test(0);
    }

    public static void test(int count){
        System.out.println(count);
        count++;
        byte[] b = new byte[128*1024];
        test(count);

    }
}
