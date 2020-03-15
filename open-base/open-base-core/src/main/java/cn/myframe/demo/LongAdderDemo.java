package cn.myframe.demo;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * @Author: ynz
 * @Date: 2019/2/27/027 8:43
 * @Version 1.0
 */
public class LongAdderDemo {

   static AtomicLong atomicLong = new AtomicLong();
   static  LongAdder longAdder = new LongAdder();
   static int threadNum = 5;
   static CountDownLatch countDownLatch = new CountDownLatch(threadNum);


    public static void main(String[] args) throws InterruptedException {

        LongAdderDemo longAdderDemo = new LongAdderDemo(){

        };
        long a = 42;
        Float b = 42f;
        System.out.println(a == b);
        Object o = null;
        long time = System.currentTimeMillis();
        for (int i = 0; i <threadNum; i++) {
            Runnable runnable = ()->{
                for (int j = 0; j < 10000; j++) {
                    atomicLong.incrementAndGet();
                  //  longAdder.increment();
                }
                countDownLatch.countDown();

            };
            new Thread(runnable).start();

        }
        countDownLatch.await();
        System.out.println(atomicLong.longValue()+":"+(System.currentTimeMillis()-time));
    }
}
