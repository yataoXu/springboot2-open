package cn.myframe.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LogEventListener {
    @EventListener
    @Async
    public void saveLog(LogSaveEvent event) throws InterruptedException{
        log.info("LogEventListener:"+ event.getLogPojo().getLogName());
    }
}
