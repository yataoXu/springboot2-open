package cn.myframe.controller;

import cn.myframe.event.LogPojo;
import cn.myframe.event.LogSaveEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventController {
    @Autowired
    private ApplicationContext publisher;

    @RequestMapping("/publisher")
    public String publisher(){
        LogSaveEvent event = new LogSaveEvent(this,new LogPojo("tom"));
        publisher.publishEvent(event);
        return "success";
    }
}
