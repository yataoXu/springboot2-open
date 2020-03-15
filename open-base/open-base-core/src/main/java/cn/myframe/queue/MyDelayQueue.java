package cn.myframe.queue;

import lombok.Data;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @Author: ynz
 * @Date: 2019/1/19/019 15:41
 * @Version 1.0
 */
@Data
public class MyDelayQueue implements Delayed {

    //毫秒
    private long delayTime;
    private String taskName;
    private long start = System.currentTimeMillis();

    public MyDelayQueue(long delayTime,String taskName){
        this.delayTime = delayTime;
        this.taskName = taskName;
    }

    public  MyDelayQueue(){

    }


    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert((start+delayTime) - System.currentTimeMillis(),TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        MyDelayQueue o1 = (MyDelayQueue) o;
        return (int) (this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
    }

    public static void main(String[] args) throws InterruptedException {
        DelayQueue<MyDelayQueue> delayQueues = new DelayQueue<>();
        for(int i = 0;i<100;i++){
            delayQueues.offer(new MyDelayQueue(1000*i,"name"+i));
        }
        while (true){
            Long startTime = System.currentTimeMillis();
            MyDelayQueue delayQueue = delayQueues.take();
            System.out.println(delayQueue.getTaskName()+":"+(System.currentTimeMillis()-startTime));
        }
    }
}
