package cn.myframe.test;

import lombok.Synchronized;

/**
 * @Author: ynz
 * @Date: 2019/8/15/015 10:13
 * @Version 1.0
 */
public class ThreadTest {

    static Object o = new Object();

    public synchronized void wait1() throws InterruptedException {
        wait();
        System.out.println("wait");
    }



    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(()->{
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        synchronized (thread){
            thread.wait();
        }


        System.out.println("end");
    }
}

class T1 extends  Thread{
    public void run(){
        System.out.println("1");
    }
}

class T2 extends  Thread{
    public void run(){
        System.out.println("2");
    }
}
