package cn.myframe.concurrent;

import java.util.concurrent.locks.*;

/**
 * @Author: ynz
 * @Date: 2019/1/14/014 11:41
 * @Version 1.0
 */
public class LockExample {
    static Lock lock = new ReentrantLock();
    static ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    static StampedLock sl = new StampedLock();

    public static void main(String[] args) throws NoSuchFieldException, InterruptedException {
        rwLock.readLock();
        long readLock = sl.readLock();
        sl.unlockRead(readLock);

        Runnable runnable = ()->{
            try {
                lock.lock();
                Thread.sleep(10_000);
                lock.unlock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        Thread thread = new Thread(runnable);
        Thread thread2 = new Thread(runnable);


        thread.start();
        thread2.start();


    }


}
