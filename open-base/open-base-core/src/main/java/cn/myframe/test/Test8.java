package cn.myframe.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: ynz
 * @Date: 2019/11/18/018 14:06
 * @Version 1.0
 */
public class Test8 {

    static List<BAC> list = new ArrayList<BAC>();


    public static void main(String[] args) {

        ExecutorService exec = Executors.newFixedThreadPool(4);

        for (int i = 0; i < 2000000; i++) {
            exec.execute(()->{
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
              //  list.add( new BAC());
            });
        }

/*        exec.execute(new MyThread());
        exec.execute(new MyThread());
        exec.execute(new MyThread());
        exec.execute(new MyThread());*/

    }

}

class MyThread implements Runnable {
    public void run() {
        for(int i =0; i<1000;i++){
         /*   try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            byte[] b  = new byte [1024*1024*10];*/
            BAC bac = new BAC();
        }

       // a = null;
    }
}


