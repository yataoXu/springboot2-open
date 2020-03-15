package cn.myframe.concurrent;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: ynz
 * @Date: 2019/1/19/019 14:41
 * @Version 1.0
 */
public class ThreadPool {
    public static void main(String[] args) throws InterruptedException {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10,10,60,TimeUnit.SECONDS,new ArrayBlockingQueue<>(10));
        threadPoolExecutor.getActiveCount();
        AtomicInteger integer = new AtomicInteger();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        //new ThreadPoolExecutor(0, Integer.MAX_VALUE,
        //                                      60L, TimeUnit.SECONDS,
        //                                      new SynchronousQueue<Runnable>());
        ExecutorService excecutorService = Executors.newCachedThreadPool();
        ExecutorService executorService2 = Executors.newFixedThreadPool(10);
        ExecutorService execotorService3 = Executors.newSingleThreadExecutor();
        ScheduledExecutorService executorService4 = Executors.newScheduledThreadPool(10);
        for(int i=0;i<1;i++){
            Runnable runable = ()->{
                integer.getAndIncrement();
                countDownLatch.countDown();
                System.out.println(Thread.currentThread().getName());
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
            executorService2.execute(runable);
          //  executorService4.schedule(runable,2,TimeUnit.SECONDS);
        }
        countDownLatch.await();
        System.out.println(integer.get());
        System.out.println("-----------------------------");


        Runnable runable = ()->{
            integer.getAndIncrement();
            System.out.println(Thread.currentThread().getName());
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        executorService4.schedule(runable,10,TimeUnit.SECONDS);

        System.out.println(System.nanoTime());

        DelayQueue delayQueue = new DelayQueue();
        delayQueue.take();





    }

    private class MyDelayEntity implements Delayed{

        @Override
        public long getDelay(TimeUnit unit) {
            return 0;
        }

        @Override
        public int compareTo(Delayed o) {
            return 0;
        }
    }


}


