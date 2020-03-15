package cn.myframe.ms;

import java.util.concurrent.CountDownLatch;

/**
 * @Author: ynz
 * @Date: 2019/1/17/017 9:35
 * @Version 1.0
 */
public class Example4 {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(3);

        new Thread(()->{
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            countDownLatch.countDown();
            countDownLatch.countDown();
            countDownLatch.countDown();
            countDownLatch.countDown();

        }).start();
        countDownLatch.await();
        countDownLatch.await();
        countDownLatch.await();
        countDownLatch.await();
        countDownLatch.await();


    }
}
