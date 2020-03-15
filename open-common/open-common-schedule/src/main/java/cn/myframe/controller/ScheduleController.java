package cn.myframe.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

/**
 * 测试注释掉ScheduleTask类的@EnableScheduling
 */
@RestController
@Slf4j
public class ScheduleController {

    private final static Map<String,Task> taskMap = new HashMap<String,Task>();

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        return new ThreadPoolTaskScheduler();
    }

    /**
     * 创建定时任务
     */
    @RequestMapping("/startTask/{id}")
    public String start(@PathVariable("id") String id){
        if(!StringUtils.isEmpty(id) && !taskMap.containsKey(id)){
            Task task = new Task(id);
            ScheduledFuture<?> future = threadPoolTaskScheduler.schedule(task.getRunnable(),
                new Trigger(){
                    public Date nextExecutionTime(TriggerContext triggerContext){
                        return new CronTrigger(task.getCron())
                                .nextExecutionTime(triggerContext);
                    }
                }
            );
            task.setScheduledFuture(future);
            taskMap.put(task.getTaskId(),task);
        }
        return "success";
    }

    /**
     * 更新定时任务的cron
     */
    @RequestMapping("/updateCron/{id}")
    public String update(@RequestBody String cron,@PathVariable("id") String id){
        if(taskMap.containsKey(id)){
            Task task = taskMap.get(id);
            ScheduledFuture<?> future = null;
            try{
                future = threadPoolTaskScheduler.schedule(task.getRunnable(),
                        new Trigger(){
                            public Date nextExecutionTime(TriggerContext triggerContext){
                                return new CronTrigger(cron).nextExecutionTime(triggerContext);
                            }
                        }
                );
            }catch (Exception e){
                return "error";
            }
            task.getScheduledFuture().cancel(true);
            task.setScheduledFuture(future);
        }
        return "success";
    }

    /**
     * 删除定时任务
     */
    @RequestMapping("/stopTask/{id}")
    public String stop(@PathVariable("id") String id){
        if(taskMap.containsKey(id)){
            Task task = taskMap.get(id);
            task.getScheduledFuture().cancel(true);
            taskMap.remove(id);
        }
        return "success";
    }

    @Slf4j
    @Data
    static class Task{
        private String taskId;
        private Runnable runnable;
        private ScheduledFuture scheduledFuture;
        private String cron;
        public Task(String taskId){
            this.taskId = taskId;
            runnable = ()->{
                log.info("任务taskId:"+taskId);
            };
            cron = "0/5 * * * * ?";
        }
    }

}
