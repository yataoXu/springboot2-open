package cn.myframe.ms;

import cn.myframe.utils.UnsafeUtils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: ynz
 * @Date: 2019/1/18/018 9:36
 * @Version 1.0
 */
public class Example5 {

    public volatile int count = 0;

    public static void main(String[] args) throws NoSuchFieldException {
        CountDownLatch countDownLatch = new CountDownLatch(100);
        Example5 example5 = new Example5();
        long countOffset = UnsafeUtils.getUnsafe().objectFieldOffset
                (Example5.class.getDeclaredField("count"));
        for(int j=0;j<100;j++){
            new Thread(()->{
                for(int i = 0; i<100;i++){
                    int c = example5.count;
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    UnsafeUtils.getUnsafe().compareAndSwapInt(example5,countOffset,c,c+1);
                    /*while(!UnsafeUtils.getUnsafe().compareAndSwapInt(example5,countOffset,c,c+1)){
                         c = example5.count;
                    };*/
                }
                countDownLatch.countDown();
            }).start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(example5.count);

    }
}
