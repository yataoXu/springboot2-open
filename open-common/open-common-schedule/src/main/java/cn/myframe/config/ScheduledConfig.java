package cn.myframe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 *  注释Configuration时，定时任务串行执行
 *  去掉注释时，定时任务并行执行
 */
@Configuration
public class ScheduledConfig  implements SchedulingConfigurer {
    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(setTaskExecutors());
    }
    @Bean
    public Executor setTaskExecutors(){
        return Executors.newScheduledThreadPool(3); // 3个线程来处理。
    }
}
