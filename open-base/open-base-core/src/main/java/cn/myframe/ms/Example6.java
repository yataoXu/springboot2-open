package cn.myframe.ms;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: ynz
 * @Date: 2019/1/22/022 8:09
 * @Version 1.0
 */
public class Example6 {

    static volatile int anInt = 0;

    static AtomicInteger atomicInteger = new AtomicInteger(0);

    static int threadNum = 2;

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(threadNum);
        long startTime = System.currentTimeMillis();
        for(int i = 0;i<threadNum;i++){
            new Thread(()->{
                for(int j = 0;j<50000000;j++){
                 //   synchronized (Example6.class){
                        Example6.anInt = Example6.anInt+1;
                 //   }

                }
                countDownLatch.countDown();
            }).start();

            /*new Thread(()->{
                for(int j = 0;j<1000000;j++){
                    atomicInteger.getAndIncrement();
                }
                countDownLatch.countDown();
            }).start();*/
        }
        countDownLatch.await();
        System.out.println(Example6.anInt+":"+(System.currentTimeMillis()-startTime));
        System.out.println(atomicInteger.get()+":"+(System.currentTimeMillis()-startTime));
    }
}
