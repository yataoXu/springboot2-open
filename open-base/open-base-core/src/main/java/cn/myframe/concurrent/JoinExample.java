package cn.myframe.concurrent;

/**
 * @Author: ynz
 * @Date: 2019/1/14/014 8:39
 * @Version 1.0
 */
public class JoinExample {

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(()->{
            System.out.println("this is T1");
        });
        t1.start();
        //这里阻塞主线程,直到t1线程执行完,继续执行主线程(一个线程执行结束后会执行该线程自身对象的notifyAll方法)
        t1.join();
        Thread t2 = new Thread(()->{
            System.out.println("this is T2");
        });
        t2.start();
    }
}
