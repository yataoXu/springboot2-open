package cn.myframe.test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: ynz
 * @Date: 2019/9/23/023 15:37
 * @Version 1.0
 */

public class FunctionTest{

    public static void main(String[] args) {

        FunctionTest fun = new FunctionTest();
        String test = "test";
        Func functionTest = (b) ->{
            return b;
        };
        functionTest.run(true);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3,4,30,TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(1));
        Runnable r1 = ()->{
            fun.t();
        };
        threadPoolExecutor.submit(r1);

        Runnable r2 = ()->{
            synchronized (test.intern()){
                try {
                    test.intern().wait();
                    System.out.println("r2");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        };
        threadPoolExecutor.submit(r2);
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Runnable r3 = ()->{
            fun.t();
        };
        threadPoolExecutor.submit(r3);
        Runnable r4 = ()->{

        };
        threadPoolExecutor.submit(r4);
        threadPoolExecutor.submit(r4);


    }

    public synchronized void t(){
        try {
            Thread.sleep(100_1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

@FunctionalInterface
interface Func{
    public boolean run(boolean b);
}
