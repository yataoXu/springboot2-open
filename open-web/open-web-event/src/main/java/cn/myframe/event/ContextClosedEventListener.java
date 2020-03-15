package cn.myframe.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ContextClosedEventListener  implements ApplicationListener<ContextStartedEvent> {
    @Override
    public void onApplicationEvent(ContextStartedEvent contextClosedEvent) {
        log.info("----------------------ContextStartedEvent:"+System.currentTimeMillis());
    }
}
