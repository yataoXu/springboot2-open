package cn.myframe.test;

import cn.myframe.demo.Test;

/**
 * @Author: ynz
 * @Date: 2019/11/20/020 14:16
 * @Version 1.0
 */
public class Test9 {

    private long count = 0;

    private void add10K() {
        int idx = 0;
        while(idx++ < 1000000000) {
            count += 1;
        }
    }

    public static long calc() throws InterruptedException {
        final Test9 test = new Test9();
        // 创建两个线程，执行 add() 操作

        Thread th1 = new Thread(()->{
            test.add10K();
        });

        Thread th2 = new Thread(()->{
            test.add10K();
        });

        Thread th3 = new Thread(()->{
            test.add10K();
        });

        Thread th4 = new Thread(()->{
            test.add10K();
        });

        Thread th5 = new Thread(()->{
            test.add10K();
        });

        // 启动两个线程
        th1.start();
        th2.start();
        th3.start();
        th4.start();
        th5.start();

        // 等待两个线程执行结束
        th1.join();
        th2.join();
        th3.join();
        th4.join();
        th5.join();
        return test.count;

    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println(calc());
    }

}
