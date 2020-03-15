package cn.myframe.test;

/**
 * @Author: ynz
 * @Date: 2019/5/30/030 8:04
 * @Version 1.0
 */
public class TL {

    int abc = 0;

    public static void main(String[] args) throws InterruptedException {
        TL tl = new TL();
        for (int i = 0; i < 30; i++) {
            T thread = new T(tl);
            thread.start();
        }
        Thread.sleep(3000);
        System.out.println(tl.abc);

    }
}

class T extends Thread{
    private static final ThreadLocal<TL> tlThreadLocal = new ThreadLocal<>();
    public TL tl;
    public T(TL tl){
        this.tl = tl;
    }
    public void run(){
        tlThreadLocal.set(tl);
        System.out.println(Thread.currentThread().getName());
        TL tl = tlThreadLocal.get();
        for (int i = 0; i < 10000; i++) {
            tl.abc = tl.abc+1;
        }
    }
}

