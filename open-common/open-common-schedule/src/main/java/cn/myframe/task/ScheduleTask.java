package cn.myframe.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 测试串行并行时，去掉@EnableScheduling的注释
 * 测试动态定时任务，注释@EnableScheduling
 */
@Slf4j
/*@Component*/
/*@EnableScheduling*/
public class ScheduleTask {
    @Scheduled(cron = "0/2 * * * * ?")
    public void task() throws InterruptedException {
        log.info("执行定时任务1");
        Thread.sleep(60_000);
    }

    @Scheduled(cron = "0/2 * * * * ?")
    public void task2() throws InterruptedException {
        log.info("执行定时任务2");
        Thread.sleep(5_000);
    }
}
