package cn.myframe.asyncTask;


import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

@Component
@Slf4j
public class AsyncTask {
    @Async("myAsync")
    public Future<String> doTask1() throws InterruptedException{
        log.info("Task1 started.");
        long start = System.currentTimeMillis();
        Thread.sleep(5000);
        long end = System.currentTimeMillis();
        log.info("Task1 finished, time elapsed: {} ms.", end-start);
        return new AsyncResult<>("Task1 accomplished!");
    }

    @Async
    public Future<String> doTask2() throws InterruptedException{
        log.info("Task2 started.");
        long start = System.currentTimeMillis();
        Thread.sleep(3000);
        long end = System.currentTimeMillis();
        log.info("Task2 finished, time elapsed: {} ms.", end-start);
        return new AsyncResult<>("Task2 accomplished!");
    }
}
