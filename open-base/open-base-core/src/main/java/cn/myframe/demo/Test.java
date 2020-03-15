package cn.myframe.demo;

import lombok.Cleanup;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @Author: ynz
 * @Date: 2019/7/1/001 13:50
 * @Version 1.0
 */
public class Test {

    public static void main(String[] args) {
        //CompletableFuture<String> stringCompletableFuture = new CompletableFuture<>();

        long startTime = System.currentTimeMillis();
        CompletableFuture completableFuture = CompletableFuture.runAsync(()->{
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("123");
        });
        CompletableFuture completableFuture2 = CompletableFuture.runAsync(()->{
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("123");
        });
        try {
            completableFuture.get();
            completableFuture2.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println(System.currentTimeMillis() - startTime);

    }

    public void close() {
        System.out.println("执行此方法close");
    }

    public void otherClose() {
        System.out.println("执行此方法otherClose");
    }
}
