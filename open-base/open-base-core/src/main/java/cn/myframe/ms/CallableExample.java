package cn.myframe.ms;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @Author: ynz
 * @Date: 2019/2/13/013 14:41
 * @Version 1.0
 */
@Slf4j
public class CallableExample {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        MyCallable myCallable = new MyCallable();
        FutureTask<Integer> futureTask = new FutureTask<Integer>(myCallable);
        Thread thread = new Thread(futureTask);
        thread.start();
        System.out.println(futureTask.get());

        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                return null;
            }
        };

    }


}

class MyCallable implements Callable<Integer>{

    @Override
    public Integer call() throws Exception {
        return 1;
    }
}
