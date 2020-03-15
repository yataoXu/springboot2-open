package cn.myframe.event;

import lombok.Data;
import org.springframework.context.ApplicationEvent;

@Data
public class LogSaveEvent extends ApplicationEvent {
    public LogPojo logPojo;
    public LogSaveEvent(Object source,LogPojo logPojo) {
        super(source);
        this.logPojo = logPojo;
    }
    public LogSaveEvent (Object source) {
        super(source);
    }
}
