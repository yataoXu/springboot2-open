import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: ynz
 * @Date: 2019/9/25/025 10:28
 * @Version 1.0
 */
public class ThreadStatus {

    static ThreadPoolExecutor pool =
            new ThreadPoolExecutor(3,4,30,TimeUnit.SECONDS,new SynchronousQueue<>());

    public static void main(String[] args) throws InterruptedException {
        System.out.println(".......start");

        ThreadStatus status = new ThreadStatus();

        Object o = new Object();

        //休眠对应的sleep操作
        Runnable r1 = ()->{status.sync(); };
        pool.submit(r1);

        //等待对应的wait
        Runnable r2 = ()->{
            synchronized (o){
                try {
                    o.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        pool.submit(r2);

        //监视对应的synchronized阻塞
        pool.submit(r1);

        //驻留对应的线程池里的空闲线程
        Runnable r3 =()->{
            try {
                Thread.sleep(5_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        pool.submit(r3);



    }

    public synchronized void sync(){
        try {
            Thread.sleep(100_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
