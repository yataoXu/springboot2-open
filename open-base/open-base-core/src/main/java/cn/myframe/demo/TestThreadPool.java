package cn.myframe.demo;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: ynz
 * @Date: 2019/7/18/018 9:44
 * @Version 1.0
 */
public class TestThreadPool {

    static volatile int flag = 1;


    static ThreadPoolExecutor threadPoolExecutor =
            new ThreadPoolExecutor(10,10,10L,TimeUnit.SECONDS,new ArrayBlockingQueue<>(200));

    public static void main(String[] args) {

        Runnable runnable = ()->{
            System.out.println(++flag);
            int i = 1/0;
        };

        for (int i = 0; i < 100; i++) {
            threadPoolExecutor.execute(runnable);
        }

        try {
            Thread.sleep(5_000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
