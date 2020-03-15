package cn.myframe.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LogEvent2Listener implements ApplicationListener<LogSaveEvent> {
    @Async
    @Override
    public void onApplicationEvent(LogSaveEvent logSaveEvent) {
        log.info("LogEvent2Listener:"+logSaveEvent.getLogPojo().getLogName());
    }
}
